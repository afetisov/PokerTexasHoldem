package server.game;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import server.dao.DatabaseService;

import commons.game.Game;
import commons.game.GameClient;
import commons.game.GameContext;
import commons.game.TableOccupancyType;
import commons.game.events.EventType;
import commons.game.events.GameEvent;
import commons.game.poker.ActionType;
import commons.game.poker.Card;
import commons.game.poker.Deck;
import commons.game.poker.HandRound;
import commons.game.poker.Player;
import commons.game.poker.PlayerHand;
import commons.game.poker.Pot;
import commons.game.poker.eval.SevenCardHandEvaluator;
import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.Hand;
import commons.model.HandPlayer;
import commons.model.HandPlayer;
import commons.model.Session;
import commons.model.TableInfo;
import commons.util.MiscUtils;

@SuppressWarnings("serial")
public class GameImpl extends UnicastRemoteObject implements Game {

	enum GameState {
		IDLE,
		BEGIN_HAND,
		BETTING,
		FINISH_HAND,
		CLEANUP,
		EXIT
	}
	
	private boolean stopGameRequested;
	
	private GameState gameState = GameState.IDLE;
	
	private Timer buyinTimer = new Timer();
	//The hash map of all connected clients: active players and observers.
	//Key: sessionID
	//Value: GameClient
	//private Map<String,GameClient> clients = new ConcurrentHashMap<String,GameClient>();
	private Set<String> clients = new HashSet<String>();
	
	//The hash map of all poker players that have a seat. Used to get player by session id.
	//Key: sessionID
	//Value : commons.game.poker.Player
	private Map<String,Player> players = new ConcurrentHashMap<String, Player>(TableOccupancyType.MAX_PLAYERS);

	//The hash map of all db data players that have a seat. Used to get player by session id.
	//Key: sessionID
	//Value : commons.model.Player
	private Map<String,commons.model.Player> dbPlayers = new ConcurrentHashMap<String, commons.model.Player>(TableOccupancyType.MAX_PLAYERS);
	
	private Map<String,LinkedBlockingQueue<GameEvent>> clientQueue = new ConcurrentHashMap<String, LinkedBlockingQueue<GameEvent>>();
	
	private GameContext gameContext = new GameContext();
	
	//private LinkedBlockingQueue<GameEvent> eventQueue = new LinkedBlockingQueue<GameEvent>();
	
	//private EventHandler eventHandler;
	
	private SevenCardHandEvaluator handEvaluator = new SevenCardHandEvaluator();
	
	public GameImpl() throws RemoteException {
		super();
	}
	
	public GameImpl(TableInfo table) throws RemoteException {
		super();

		gameContext.table = table;
		
		//eventHandler = new EventHandler();
	}


	@Override
	public boolean takeSeat(String sessionID, int seatNumber, BigDecimal buyIn)
			throws RemoteException {
		
		if(sessionID == null || sessionID.isEmpty() 
				|| seatNumber < 1 || seatNumber > TableOccupancyType.MAX_PLAYERS
				|| buyIn == null || buyIn.equals(BigDecimal.ZERO) ) 
			return false;
		
		if(players.get(sessionID) != null) return false; //player has a seat already
		
		BigDecimal minBuyIn = gameContext.table.getMinBayin();
		
		BigDecimal maxBuyIn = gameContext.table.getMaxBayin();
		
		if(buyIn.compareTo(minBuyIn) < 0) return false;
		
		if(maxBuyIn != null) {
			if(buyIn.compareTo(maxBuyIn) > 0) return false;
		}
		
		Session session = DatabaseService.getPlayersDAO().findSession(sessionID);
		
		if(session == null) return false;
		
		BigDecimal balance = session.getPlayer().getBalance();
		
		if( balance == null || balance.equals(BigDecimal.ZERO) 
				|| balance.compareTo(minBuyIn) < 0 ) return false;
		
		BigDecimal chipsAmount = MiscUtils.min(balance, buyIn);
		
		balance = balance.subtract(chipsAmount);
		
		Player player = new Player(session.getPlayer().getName(), chipsAmount, balance, seatNumber, sessionID);		
		
		synchronized (gameContext.activePlayers) {
			
			if(gameContext.activePlayers[seatNumber - 1] == null) {
			
				gameContext.activePlayers[seatNumber - 1] = player;
			
			}else {
			
				return false;
			}
		}
		
		synchronized (players) {
			
			players.put(sessionID, player);
			
			dbPlayers.put(sessionID, session.getPlayer());
			
			try {

				DatabaseService.getTablesDAO().updateTableOccupancy(gameContext.table.getId(), (short)players.size());
			
			}catch(Exception exc) {
			
				Logger.write(LoggingLevel.ERROR, "Failed to update database with players number at table # " + gameContext.table.getId(), exc);
			
			}
			
			players.notify();
		}
		
		GameEvent event = new GameEvent(EventType.PLAYER_SEATED, seatNumber);
		
		sendBroadcastEvent(event, null);
		
		return true;
	}

	@Override
	public void sitOut(String sessionID) throws RemoteException {

		if(sessionID == null || sessionID.isEmpty()) return;
			
		Player player = players.get(sessionID);
		
		if(player == null) return;

		player.setSittingOut(true);
		
		int seatNumber = player.getSeatNumber();
		
		synchronized (gameContext.activePlayers) {
			gameContext.activePlayers[seatNumber - 1] = null;
		}
		
		//players.remove(sessionID);		
	}
	
	
	public void start() {
		
		boolean skipRound = false;
		
		boolean handIsFinished = false;
		
		Hand handEntity = null;

		game_loop:
			
		while(!isGameStopped()) {
			
			switch(gameState) {
			case IDLE:

				//check for number of players and wait until 2 or more players have seated
				synchronized (players) {
					if(players.size() < 2) {
						try {
							players.wait();
						}catch(InterruptedException e) {
							if(stopGameRequested) gameState = GameState.EXIT;
						}
					} else {
						gameState = GameState.BEGIN_HAND;
					}
				}

				break;
			case BEGIN_HAND:
				
				initHand();
				
				if(gameContext.getActivePlayersCount() < 2) {
					gameState = GameState.IDLE;
					break;
				}
				
				if(gameContext.isHeadsUp()) {
					gameContext.headsUp = true;
				}
				
				gameContext.dealer = getNextDealer();
				
				if(gameContext.dealer < 0) {
					gameState = GameState.IDLE;
					break;
				}
				
				gameContext.smallBlindPlayer = getSmallBlindPlayer();
				
				if(gameContext.smallBlindPlayer < 0) {
					gameState = GameState.IDLE;
					break;
				}
				
				gameContext.bigBlindPlayer = getBigBlindPlayer();
				
				if(gameContext.bigBlindPlayer < 0) {
					gameState = GameState.IDLE;
					break;
				}
				
				
				Player smallBlindPlayer = gameContext.handPlayers[gameContext.smallBlindPlayer];
				
				if (smallBlindPlayer == null) {
					gameState = GameState.IDLE;
					break;
				}
				
				
				Player bigBlindPlayer = gameContext.handPlayers[gameContext.bigBlindPlayer];
				
				if (bigBlindPlayer == null) {
					gameState = GameState.IDLE;
					break;
				}
				
				BigDecimal bigBlindAmount = gameContext.table.getBigBlind();
				
				BigDecimal smallBlindAmount = MiscUtils.getSmallBlind(bigBlindAmount);
				
				makeBet(smallBlindPlayer, MiscUtils.min(smallBlindAmount, smallBlindPlayer.getChipsStack()));
				
				if(smallBlindPlayer.getChipsStack().compareTo(BigDecimal.ZERO) == 0) {
					smallBlindPlayer.setLastAction(ActionType.ALLIN);
					smallBlindPlayer.setAllIn(true);
				}

				makeBet(bigBlindPlayer, MiscUtils.min(bigBlindAmount, bigBlindPlayer.getChipsStack()));

				if(bigBlindPlayer.getChipsStack().compareTo(BigDecimal.ZERO) == 0) {
					bigBlindPlayer.setLastAction(ActionType.ALLIN);
					bigBlindPlayer.setAllIn(true);
				}

				
				gameContext.lastBetAmount = bigBlindAmount;
				
				gameContext.lastBetPlayer = gameContext.bigBlindPlayer;
				
				gameContext.raisesCounter++;
				
				
				sendBroadcastEvent(new GameEvent(EventType.HAND_STARTED), null);
				
				handEntity = new Hand();
				
				sleep(1000);
				
				TexasHoldemGameHelper.initCardPack(gameContext);
				
				TexasHoldemGameHelper.dealHoleCards(gameContext);
				
				sendBroadcastEvent(new GameEvent(EventType.HOLE_CARDS_DEALED), null);
				
				sleep(1000);
				
				gameState = GameState.BETTING;
				
				break;
			case BETTING:

				skipRound = false;
				
				for(HandRound round : HandRound.values()) {

					gameContext.bettingRound = round;

					Logger.write(LoggingLevel.TRACE, "Current hand round: " + round.toString());

					if(round == HandRound.SHOWDOWN) {
						
						gameState = GameState.FINISH_HAND;

						break;
					}
					
					switch(round) {
					case FLOP:
						dealBoardCards(3);
						sendBroadcastEvent(new GameEvent(EventType.BOARD_CARDS_DEALED), null);
						break;
					case TURN:
					case RIVER:
						dealBoardCards(1);
						sendBroadcastEvent(new GameEvent(EventType.BOARD_CARDS_DEALED),null);
						break;
					}
					
					if(skipRound || (skipRound = gameContext.skipBettingRound())) {
						continue;
					}
					
					sleep(1000);
					
					sendBroadcastEvent(new GameEvent(EventType.BETTING_ROUND_STARTED), null);
					
					boolean hasNext = nextTurnPlayer();
					
					while(hasNext) {
					
						Player player = gameContext.handPlayers[gameContext.currentPlayer];
						
						player.setLastAction(null);
						
						sendBroadcastEvent(new GameEvent(EventType.NEXT_PLAYER_TURN), null);
						
						synchronized (player) {
							try {
								player.wait(31000);
								//player.wait();
								
							}catch(InterruptedException exc) {
								
								if (gameContext.getActivePlayersCount() < 2) {
									break;
								}
							}
							
							if(player.getLastAction() == null) {
							
								player.setLastAction(ActionType.FOLD);
								
								player.setHoleCards(null);
								
								player.setFolded(true);
							}
						}
												
						hasNext = nextTurnPlayer();
						
					}

					sendBroadcastEvent(new GameEvent(EventType.BETTING_ROUND_FINISHED),null);
					
					sleep(1000);
					
					gameContext.resetRound();
				}

				gameState = GameState.FINISH_HAND;
				
				break;
				
			case FINISH_HAND:
				
				if (gameContext.bettingRound  == HandRound.SHOWDOWN) {
					
					List<Player> players = new ArrayList<Player>(gameContext.handPlayers.length);
					
					//select only active players for pot splitting
					for(int i = 0; i < gameContext.handPlayers.length; i++) {
						
						Player p = gameContext.handPlayers[i];
						
						if(p == null || p != gameContext.activePlayers[i] || p.isFolded()) continue;
						
						players.add(p);
						
						Logger.write(LoggingLevel.TRACE, "Active players list: " + p.getNickname());
						
						Set<Card> cards = new HashSet<Card>(gameContext.board);
						
						cards.addAll(new HashSet<Card>(p.getHoleCards()));
						
						PlayerHand playerHand = handEvaluator.evaluate(cards);
						
						gameContext.handValues[i] = playerHand;

						Logger.write(LoggingLevel.TRACE, "Active player hand: " + playerHand.getHandRank().toString());
					}
					
					//sort players by bet amount
					Collections.sort(players, new Comparator<Player> () {

						@Override
						public int compare(Player player1, Player player2) {
							return player1.getTotalBetAmount().compareTo(player2.getTotalBetAmount());
						}
					});
					
					
					List<Pot> pots = new ArrayList<Pot>();
					
					//assign players to pots
					for(int i = 0; i < players.size(); i++) {
						
						Player curPlayer = players.get(i);
						
						Logger.write(LoggingLevel.TRACE, "Current pot player: " + curPlayer.getNickname() + " with bets: " + curPlayer.getTotalBetAmount().toPlainString());
						
						if(curPlayer.getTotalBetAmount().compareTo(BigDecimal.ZERO) == 0) continue;

						Pot pot = new Pot();
						
						pots.add(pot);

						BigDecimal curAmount = curPlayer.getTotalBetAmount();
						
						for(int j = 0; j < players.size(); j++) {
							
							Player p = players.get(j);
							
							if(p.getTotalBetAmount().compareTo(BigDecimal.ZERO) == 0) continue;
							
							pot.setAmount(pot.getAmount().add(curAmount));
							
							p.setTotalBetAmount(p.getTotalBetAmount().subtract(curAmount));
							if(p.getHoleCards() != null && !p.isFolded()) {
								pot.getPlayers().add(p);
							}
						}
					}
					
					//determine winners of each pot starting with the last
					for(int i = pots.size() - 1; i >= 0; i--) {
						
						Set<Player> winners = new HashSet<Player>();
						
						PlayerHand lastWinner = null;	
					
						Pot pot = pots.get(i);
						
						for(Player p : pot.getPlayers()) {
	
							PlayerHand hand = gameContext.handValues[p.getSeatNumber() - 1];
							
							if(hand == null) continue;
							
							if(lastWinner == null) {
							
								lastWinner = hand;
								
								winners.add(p);
								
								continue;
							}
							
							int com = lastWinner.compareTo(hand);
							
							if(com > 0) {
								
								lastWinner = hand;
								
								winners.clear();
								
								winners.add(p);
							
							} else if ( com == 0) {
							
								winners.add(p);
							
							}
						}
						
						BigDecimal potAmount = pot.getAmount().subtract(MiscUtils.getRake(pot.getAmount()));
						
						if(winners.size() == 1) {
							
							Player winner = winners.iterator().next();
							
							winner.setChipsStack(winner.getChipsStack().add(potAmount));
							
							winner.setWonChipsAmount(potAmount);
							
							winner.setHand(gameContext.handValues[winner.getSeatNumber() - 1]);
							
							winner.getHand().setHoleCards(winner.getHoleCards().toArray(new Card[] {}));

							
						} else if (winners.size() > 0) {
							
							long splitPotAmount = potAmount.longValue() / winners.size();
							
							long rest = potAmount.longValue() - splitPotAmount * winners.size();	
						
							for(Player p : winners) {
								
									BigDecimal wonStack = new BigDecimal(splitPotAmount);
									
									p.setChipsStack(p.getChipsStack().add(wonStack));
									
									p.setWonChipsAmount(wonStack);
									
									p.setHand(gameContext.handValues[p.getSeatNumber() - 1]);
									
									p.getHand().setHoleCards(p.getHoleCards().toArray(new Card[] {}));

							}
							
							if(rest > 0) {
								
								int sb = getNextLeftPlayer(gameContext.dealer);
								
								if(sb >= 0) {
								
									Player p = gameContext.handPlayers[sb];
									
									if(p != null) {
										
										p.setChipsStack(p.getChipsStack().add(new BigDecimal(rest)));
										
										p.setWonChipsAmount(p.getWonChipsAmount().add( new BigDecimal(rest)));
									}
								}
							}
						}
					}

					
					if(gameContext.lastBetPlayer >= 0) {
						
						Player p = gameContext.handPlayers[gameContext.lastBetPlayer];
						
						if(p != null && p.getHand() == null) {
							
							p.setHand(gameContext.handValues[p.getSeatNumber() - 1]);
							
							if(p.getHand() != null && p.getHoleCards() != null) {
								p.getHand().setHoleCards(p.getHoleCards().toArray(new Card[] {}));
							}
						}
					}

					sendBroadcastEvent(new GameEvent(EventType.SHOWDOWN),null);
					
				} else {
					
					int playersCount = gameContext.getActivePlayersCount();
					
					if (playersCount == 0) {
						
						gameContext.resetHand();
						
						gameState = GameState.IDLE;
						
						break;
					} else if (playersCount < 2) {
						
						//TODO finish hand and reward last player
						int lastPlayer = gameContext.getLastActivePlayer();
						
						if(lastPlayer >= 0) {
							
							gameContext.onePlayerLeft = true;
							
							gameContext.currentPlayer = -1;
							
							Player p = gameContext.handPlayers[lastPlayer];
							
							BigDecimal pot = gameContext.pot.subtract(gameContext.rake);
							
							if(p != null) {
									
								p.setChipsStack(p.getChipsStack().add(pot));
									
								p.setWonChipsAmount(pot);
								
								gameContext.pot = BigDecimal.ZERO;
								
								sendBroadcastEvent(new GameEvent(EventType.DECLARE_WINNERS), null);
								
								sleep(5000);
							}
						}

						for(Player pl : gameContext.handPlayers) {
							if(pl != null) {
								pl.reset(true);
							}
						}

						gameContext.resetHand();
					}
				}
				
				//Save hand's data
				handEntity.setFinishDateTime(new Date());
				
				handEntity.setLastRound(gameContext.bettingRound);
				
				handEntity.setBoard(MiscUtils.cards2str(gameContext.board));
				
				handEntity.setRake(gameContext.rake);
				
				handEntity.setTotalPot(gameContext.pot);
				
				handEntity.setTable(gameContext.table);

				for(int i = 0; i < gameContext.handPlayers.length;i++) {
					
					Player p = gameContext.handPlayers[i]; 
					
					if(p == null || p != gameContext.activePlayers[i] || dbPlayers.get(p.getSessionID()) == null) continue;
				
					System.out.println("Hand ID : " + handEntity.getId());
					
					String handCards = null;
					
					if(p.getHand() != null && p.getHand().getHandCards() != null && !p.getHand().getHandCards().isEmpty()) {
						handCards = MiscUtils.cards2str(p.getHand().getHandCards());
					}
					
					HandPlayer hp = new HandPlayer(handEntity, dbPlayers.get(p.getSessionID()), MiscUtils.cards2str(p.getHoleCards()), p.getTotalBetAmount(), p.getWonChipsAmount(), handCards);
										
					handEntity.getHandPlayers().add(hp);
					
				}
				
				try {
					
					DatabaseService.getGameDAO().saveHandHistory(handEntity);
				
				}catch(Exception exc) {
					Logger.write(LoggingLevel.ERROR, "Failed to save hand history into a database.", exc);
				}

				sleep(5000);
				
				gameState = GameState.CLEANUP;
				
				break;
			case CLEANUP:

				gameContext.resetHand();
				
				for(Player p : gameContext.handPlayers) {
					if(p != null) {
						p.reset(true);
					}
				}

				sendBroadcastEvent(new GameEvent(EventType.HAND_FINISHED),null);
				
				gameState = GameState.IDLE;
				
				break;
			case EXIT:
				
				break game_loop;
			}
			
			sleep(1000);
		}
		
		System.out.println("Game is finished");
	}
	
	private void  sleep(long time) {
		
		try {
		
			Thread.sleep(time);
		
		}catch(InterruptedException exc) {}		
	}
	
	private void makeBet(Player player, BigDecimal amount) {
		
		player.setChipsStack(player.getChipsStack().subtract(amount));
		
		player.setTotalBetAmount(player.getTotalBetAmount().add(amount));
		
		player.setCurrentBetAmount(player.getCurrentBetAmount().add(amount));
		
		gameContext.pot = gameContext.pot.add(amount);
		
		gameContext.rake = MiscUtils.getRake(gameContext.pot);
	}
	
	private int getNextDealer() {
		
		int next = (gameContext.dealer + 1) % gameContext.handPlayers.length;
		
		while(true) {
			
			if((gameContext.dealer == -1 && next == TableOccupancyType.MAX_PLAYERS - 1 ) || ( gameContext.dealer == next)) return -1;
			
			if(gameContext.handPlayers[next] == null || gameContext.activePlayers[next] == null || gameContext.handPlayers[next] != gameContext.activePlayers[next]) {

				next = (next + 1) % gameContext.handPlayers.length;
				
				continue;
			}
			
			return next;
		}
	}
	

	private int getSmallBlindPlayer() {
		
		if(!gameContext.headsUp) {
			return getNextLeftPlayer(gameContext.dealer);
		} else {
			return gameContext.dealer;
		}
	}
	
	private int getBigBlindPlayer() {
		return getNextLeftPlayer(gameContext.smallBlindPlayer);		
	}
	
	private int getNextLeftPlayer(int start) {

		int next = (start + 1) % gameContext.handPlayers.length;
		
		while(true) {
			
			if(start == next) return -1;
			
			if(gameContext.handPlayers[next] == null || gameContext.activePlayers[next] == null 
					|| gameContext.handPlayers[next] != gameContext.activePlayers[next]
					|| gameContext.handPlayers[next].isFolded()
					|| gameContext.handPlayers[next].getLastAction() == ActionType.ALLIN) {
				
				next = (next + 1) % gameContext.handPlayers.length;
				continue;
			}
			
			return next;
		}
	}

	/* В данном методе заложена очень хитрая логика по вычислению номера игрока, 
	 * который должен ходить следующим. Ниже приведен краткий алгоритм:
	 *  1. Если раунд только начался и еще не было ни одного хода, то
	 *   	1.1 Если текущий раунд - ПРЕФЛОП, то право первого хода получает игрок слева от большого блайнда
	 *   	1.2 В противном случае право первого хода получает игрок сидящий слева от дилера
	 *   	1.3 Если не найдено ни одного активного игрока слева от большого блайнда или дилера, то раунд - завершен
	 *  2. Если же текущий раунд продолжается, то
	 *  	2.1 Если не найден никакой другой игрок кроме текущего, то раунд завершается
	 *  	2.2 Если следующий игрок имеет меньшую ставку, чем предыдуший, то отдаем ему ход
	 *  	2.3 Если же ставки равны, то
	 *  		2.3.1 Если раунд - ПРЕФЛОП, то 
	 *  			2.3.1.1 Если первая и последняя ставка была Большой блайнд, то, если следующий игрок - Большой блайнд, то раунд завершается 
	 *  			2.3.1.2 Если последняя ставка не Большой блайнд и следующий игрок тот, который сделал последнюю ставку, то раунд заканчивается
	 *  		2.3.2 В противном случае если следующий игрок тот, который сделал последнюю ставку, то раунда завершается
	 * 
	 */
	private boolean nextTurnPlayer() {
		
		if (gameContext.currentPlayer < 0) {
			
			int nextPlayer = -1;
			
			if(gameContext.bettingRound.ordinal() < HandRound.FLOP.ordinal()) {
			
				nextPlayer = getNextLeftPlayer(gameContext.bigBlindPlayer);
				
			} else {
				
				nextPlayer = getNextLeftPlayer(gameContext.dealer);
				
			}
			
			if(nextPlayer < 0) {
	
				gameContext.currentPlayer = -1;
				return false;
			}
			
			gameContext.currentPlayer = nextPlayer;
			return true;
			
		} else {
			
			int nextPlayer = -1;
			
			nextPlayer = getNextLeftPlayer(gameContext.currentPlayer);
			
			if(nextPlayer < 0) {
				
				gameContext.currentPlayer = -1;
				
				return false;
			}

			
			Player p = gameContext.handPlayers[nextPlayer];
			
			if (p == null) {
				gameContext.currentPlayer = -1;
				
				return false;
			}

			if (p.getCurrentBetAmount().compareTo(gameContext.lastBetAmount) < 0) {
				
				gameContext.currentPlayer = nextPlayer;
				
				return true;
			}
			
			if(gameContext.bettingRound.ordinal() < HandRound.FLOP.ordinal()) {
				
				if(gameContext.bigBlindPlayer == gameContext.lastBetPlayer && gameContext.raisesCounter == 1) {

					if(nextPlayer == gameContext.bigBlindPlayer) {
						
						gameContext.currentPlayer = nextPlayer;
						
						return true;
					
					} else if (nextPlayer == getNextLeftPlayer(gameContext.bigBlindPlayer)) {
						
						gameContext.currentPlayer = -1;
						
						return false;
					}
				}
			}
			
			if( nextPlayer == gameContext.lastBetPlayer) {
					
				gameContext.currentPlayer = -1;
				
				return false;
			}
			
			gameContext.currentPlayer = nextPlayer;
			return true;
		}
	}
			
	private void dealBoardCards(int count) {
		//burn top card in the pack
		gameContext.cardPack.pop();
		
		for(int i = 0; i < count; i++) {
			gameContext.board.add(gameContext.cardPack.pop());
		}
	}
	
	public void setStopGameRequested(boolean value) {
		stopGameRequested = value;
	}
	
	public void addClient(String sessionID) throws Exception {
		clients.add(sessionID);
		clientQueue.put(sessionID, new LinkedBlockingQueue<GameEvent>());
	}
	
	private boolean isGameStopped() {
		return stopGameRequested && Thread.interrupted();
	}

	
	@Override
	public Set<Card> getHoleCards(String sessionID){
		
		Player p = players.get(sessionID);
		
		if(p == null) return null;
		
		return p.getHoleCards();
	}

	@Override
	public GameContext getGameContext() throws RemoteException {
		return gameContext;
	}
	
	@Override
	public void goLobby(String sessionID) throws RemoteException {

		if(sessionID == null || sessionID.isEmpty()) return;
		
		Player player = players.get(sessionID);
		
		if(player == null) return;
		
		int seatNumber = player.getSeatNumber();
		
		synchronized (gameContext.activePlayers) {
			gameContext.activePlayers[seatNumber - 1] = null;
		}
		
		synchronized (players) {

			players.remove(sessionID);
			
			dbPlayers.remove(sessionID);
			
			try {

				DatabaseService.getTablesDAO().updateTableOccupancy(gameContext.table.getId(), (short)players.size());
			
			}catch(Exception exc) {
			
				Logger.write(LoggingLevel.ERROR, "Failed to update database with players number at table # " + gameContext.table.getId(), exc);
			
			}
		}
		
		clients.remove(sessionID);
		clientQueue.remove(sessionID);

		Player p = gameContext.handPlayers[seatNumber - 1];
		
		if(p != null) {
			synchronized (p) {
				p.reset(false);
				p.setFolded(true);
				p.notify();
			}
		}
		
		sendBroadcastEvent(new GameEvent(EventType.PLAYER_LEFT), sessionID);
	}

	@Override
	public void action(String sessionID, ActionType actionType,
			BigDecimal amount) throws RemoteException {
		
		Player player = players.get(sessionID);
		
		if(gameContext.currentPlayer != player.getSeatNumber() - 1) return;
		
		switch(actionType) {
		case FOLD:
			
			synchronized (player) {
				
				player.setHoleCards(null);
	
				player.setLastAction(actionType);
	
				player.setFolded(true);
				
				player.notify();
			}

			break;
		case CHECK:

			synchronized (player) {
				if(!player.isFolded()) {

					if (gameContext.lastBetPlayer == -1) {
			
						gameContext.lastBetPlayer = player.getSeatNumber() - 1;
						
						gameContext.lastBetAmount = BigDecimal.ZERO;						
					}
					
					player.setLastAction(actionType);

					player.notify();
				}
			}
			
			break;
		case CALL:
			synchronized (player) {
				if(!player.isFolded()) {
					
					makeBet(player, MiscUtils.min(gameContext.lastBetAmount.subtract(player.getCurrentBetAmount()), player.getChipsStack()));

					if(player.getChipsStack().compareTo(BigDecimal.ZERO) == 0) {
						
						player.setAllIn(true);
						
						player.setLastAction(ActionType.ALLIN);
					
					} else {
					
						player.setLastAction(actionType);
					
					}

					player.notify();
				}
			}
			
			break;
		case BET:
			
			synchronized (player) {
				if(!player.isFolded()) {

					BigDecimal bet = MiscUtils.min(amount, player.getChipsStack());
					
					makeBet(player, bet);
			
					gameContext.lastBetAmount = player.getCurrentBetAmount();
			
					gameContext.lastBetPlayer = player.getSeatNumber() - 1;
			
					gameContext.raisesCounter++;

					
					if(player.getChipsStack().compareTo(BigDecimal.ZERO) == 0) {
						
						player.setAllIn(true);
						
						player.setLastAction(ActionType.ALLIN);
					
					} else {
					
						player.setLastAction(actionType);
					
					}

					player.notify();
				}
			}
			
			break;
		case RAISE:
			
			synchronized (player) {
				if(!player.isFolded()) {

					BigDecimal call = gameContext.lastBetAmount.subtract(player.getCurrentBetAmount());
					
					BigDecimal bet = MiscUtils.min(amount.add(call), player.getChipsStack());
			
					makeBet(player, bet);

					gameContext.lastBetAmount = player.getCurrentBetAmount();
			
					gameContext.lastBetPlayer = player.getSeatNumber() - 1;
			
					gameContext.raisesCounter++;

					if(player.getChipsStack().compareTo(BigDecimal.ZERO) == 0) {
						
						player.setAllIn(true);
						
						player.setLastAction(ActionType.ALLIN);
					
					} else {
					
						player.setLastAction(actionType);
					
					}
					
					player.notify();
				}
			}
			
			break;
		default:
			return;
		}
	}

	private void sendBroadcastEvent(GameEvent event, String excludeSessionID) {
		
		for(String sesID : clients) {
			
			if (excludeSessionID != null && excludeSessionID.equals(sesID)) continue;
			
			LinkedBlockingQueue<GameEvent> eventQueue = clientQueue.get(sesID);
			
			if (eventQueue == null) continue;
			
			try {
				eventQueue.add(event);
			}catch(Exception exc) {
				Logger.write(LoggingLevel.ERROR, "Failed to queue a game event.", exc);
			}
		}
	}
	
	private void sendEvent(GameEvent event, String sessionID) {

		LinkedBlockingQueue<GameEvent> eventQueue = clientQueue.get(sessionID);
		
		if (eventQueue == null) return;
		
		try {
			eventQueue.add(event);
		}catch(Exception exc) {
			Logger.write(LoggingLevel.ERROR, "Failed to queue a game event.", exc);
		}
	}
	
/*
	class EventHandler extends Thread {

		@Override
		public void run() {
			
			while(true) {
				
				try {
					
					final GameEvent event = eventQueue.take();
				
					if(event == null) continue;
				
					for(final GameClient client : clients.values()) {
						
							Thread t = new Thread() {
	
								@Override
								public void run() {
									try {
									
										client.postEvent(event);
									
									}catch(RemoteException exc) {
										//TODO add proper handling
										exc.printStackTrace();
									}
								}
							};
							
							t.start();
							
							t.join(500);
							
					}
				}catch(InterruptedException exc) {
					return;
				}
			}
		}
	}*/

	@Override
	public PlayerHand getHand(String sessionID) throws RemoteException {
		
		if (sessionID == null) return null;
		
		Player p = players.get(sessionID);
		
		if(p == null) return null;
		
		return gameContext.handValues[p.getSeatNumber() - 1];
		
	}
	
	private void initHand() {
		//select players to play a new hand
		for(int i = 0; i < gameContext.activePlayers.length; i++) {
			
			Player p = gameContext.activePlayers[i];

			if(p != null && p.isSittingOut()) {

				synchronized (players) {

					players.remove(p.getSessionID());
					dbPlayers.remove(p.getSessionID());
					
					try {

						DatabaseService.getTablesDAO().updateTableOccupancy(gameContext.table.getId(), (short)players.size());
					
					}catch(Exception exc) {
					
						Logger.write(LoggingLevel.ERROR, "Failed to update database with players number at table # " + gameContext.table.getId(), exc);
					
					}
				}
				
				gameContext.activePlayers[i] = null;
				
				gameContext.handPlayers[i] = null;

				gameContext.handValues[i] = null;
				
				if(p != null) {
					p.reset(true);
				}
				
				p.setSeatNumber(-1);
				
				continue;
			}
			
			if(p != null && (p.isAllIn() || p.getChipsStack().equals(BigDecimal.ZERO)) && !p.isWaitingForBuyIn()) {
				
				System.out.println("Player " + p.getNickname() + " is waiting for buy in");
				
				p.setWaitingForBuyIn(true);
			
				sendEvent(new GameEvent(EventType.BUY_CHIPS), p.getSessionID());
				
				new Timer().schedule(new FreeSeatTask(p), 60000);
			
				gameContext.handPlayers[i] = null;
				
				p.reset(true);
			
				continue;
			}

			gameContext.handPlayers[i] = p;
			
			if(p != null) {
				p.reset(true);
			}
			
			gameContext.handValues[i] = null;
		}
		
		gameContext.cardPack = null;
		
		gameContext.board.clear();
		
		gameContext.bettingRound = null;
		
		gameContext.pot = BigDecimal.ZERO;
		
		gameContext.raisesCounter = 0;
		
		gameContext.smallBlindPlayer = -1;
		
		gameContext.bigBlindPlayer = -1;
		
		gameContext.lastBetPlayer = -1;
		
		gameContext.firstBetPlayer = -1;
		
		gameContext.currentPlayer = -1;
		
		gameContext.lastBetAmount = BigDecimal.ZERO;
		
		gameContext.rake = BigDecimal.ZERO;
		
		gameContext.headsUp = false;
		
		gameContext.onePlayerLeft = false;
		
	}

	private void standUp(Player player) {

		synchronized (players) {

			players.remove(player.getSessionID());
			
			dbPlayers.remove(player.getSessionID());
			
			try {

				DatabaseService.getTablesDAO().updateTableOccupancy(gameContext.table.getId(), (short)players.size());
			
			}catch(Exception exc) {
			
				Logger.write(LoggingLevel.ERROR, "Failed to update database with players number at table # " + gameContext.table.getId(), exc);
			
			}
		}
		
		gameContext.activePlayers[player.getSeatNumber()] = null;
		
		gameContext.handPlayers[player.getSeatNumber()] = null;

		gameContext.handValues[player.getSeatNumber()] = null;
		
		if(player != null) {
			player.reset(true);
		}
		
		player.setSeatNumber(-1);

		GameEvent event = new GameEvent(EventType.PLAYER_LEFT);
		
		sendBroadcastEvent(event, null);
	}
	
	@Override
	public GameEvent getGameEvent(String sessionID) throws RemoteException {
		
		if (sessionID == null) return null;
		
		LinkedBlockingQueue<GameEvent> queue = clientQueue.get(sessionID);
		
		if(queue == null) return null;
		
		return queue.poll();
	}
	
	class FreeSeatTask extends TimerTask {

		private Player player;
		
		FreeSeatTask(Player p){
			this.player = p;
		}
		
		@Override
		public void run() {
			player.setWaitingForBuyIn(false);
			standUp(player);
			player = null;
		}
		
	}
}
