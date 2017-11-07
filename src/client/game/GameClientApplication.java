package client.game;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.PropertyConfigurator;

import commons.game.Game;
import commons.game.GameContext;
import commons.game.events.EventType;
import commons.game.events.GameEvent;
import commons.game.poker.ActionType;
import commons.game.poker.Card;
import commons.game.poker.HandRound;
import commons.game.poker.Player;
import commons.game.poker.PlayerHand;
import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.Session;
import commons.model.TableInfo;
import commons.util.MiscUtils;

import client.game.gui.BuyinDialog;
import client.game.gui.GamePanel;
import client.game.gui.LobbyPanel;
import client.game.gui.components.ActionButton;
import client.game.gui.components.BetAmountLabel;
import client.game.gui.components.BetAmountSlider;
import client.game.gui.components.ActionButtonType;
import client.game.gui.components.PlayerInfoBoxPanel;
import client.game.gui.components.SeatButton;
import client.game.misc.ApplicationContext;
import client.game.misc.ApplicationGlobals;
import client.game.misc.GuiEvent;
import client.game.misc.ResourceManager;
import client.game.remote.GameRemoteService;
import client.gui.GUIUtils;
import client.gui.LoginPanel;

@SuppressWarnings("serial")
public class GameClientApplication extends JFrame {

	enum DisplayMode {
		LOGIN,
		LOBBY,
		PROFILE,
		GAME
	}
	
	private final static long UPDATE_FREQUENCY = 10000;
	
	private DisplayMode currentMode = DisplayMode.LOGIN;
	
	private JPanel screens;
	
	private LoginPanel loginPanel;
	
	private LobbyPanel lobbyPanel;
	
	private GamePanel gamePanel;
	
	private GamePanelMediator gamePanelMediator;
	
	private boolean firstTime = true;
	
	private Timer lobbyUpdateTimer;
	
	private Game pokerGame;
	
	private LinkedBlockingQueue<GuiEvent> guiActionsQueue = new LinkedBlockingQueue<GuiEvent>();
	
	private LinkedBlockingQueue<GameEvent> gameEventQueue = new LinkedBlockingQueue<GameEvent>();
	
	private GuiEventHandler guiEventHandler;
	
	private GameEventHandler gameEventHandler;
	
	private String playerName;
	
	private static final String TITLE = "Poker Texas Holdem";
	
	private void createGUI() {
		
		setTitle(TITLE);
		
		setPreferredSize(new Dimension(900,745));
		
		setMinimumSize(new Dimension(900,745));
		
		setMaximumSize(new Dimension(900,745));
		
		setResizable(false);
		
		setLocationRelativeTo(null);
		
		
		screens = new JPanel(new CardLayout());
		
		getContentPane().add(screens);
		
		loginPanel = new LoginPanel();
		
		loginPanel.setNameLabelText("Player ID:");
		
		loginPanel.assignLoginButtonActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String userID = loginPanel.getUserName();
				
				String password = loginPanel.getPassword();
				
				try {
					
					if(userID == null || userID.isEmpty()) {
						throw new Exception("Enter player ID");
					}
					
					if(password == null || password.isEmpty()) {
						throw new Exception("Enter password");
					}

					Session session = null;
					
					loginPanel.saveSettings();
					
					try {

						session = GameRemoteService.getGameServer().loginPlayer(userID, password);
						
					}catch(Exception exc) {
						
						Logger.write(LoggingLevel.ERROR, "Failed to login.", exc);
						
						throw new Exception("Error occurred. Please see a log file for details.");
					}
					
					if(session == null) {
						throw new Exception("Incorrect player ID and/or password");
					}						
					
					ApplicationContext.set(ApplicationGlobals.SESSION.name(), session);
					
					playerName = session.getPlayer().getName();
					
					lobbyPanel.setCurrentBalance(session.getPlayer().getBalance());
					
					swapScreen(DisplayMode.LOBBY);
						
				} catch (Exception exc) {

					String [] opts = {"Close"};

					JOptionPane.showOptionDialog(GameClientApplication.this, exc.getMessage(), "Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, opts, opts[0]);					
				}
			}
		});
		
		lobbyPanel = new LobbyPanel();
		lobbyPanel.addTablesMouseListener(mouseClickAdapter);
		
		gamePanel = new GamePanel();
		gamePanelMediator = new GamePanelMediator(gamePanel);
		
		screens.add(loginPanel, DisplayMode.LOGIN.name());
		screens.add(lobbyPanel, DisplayMode.LOBBY.name());
		screens.add(gamePanel, DisplayMode.GAME.name());
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				
				Session session = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());
				
				switch(currentMode) {
				case PROFILE:
				case LOBBY:
					
					cancelTimer();
					
					try {
						
						GameRemoteService.getGameServer().logoutPlayer(session.getId());
					
					} catch (RemoteException exc) {
						Logger.write(LoggingLevel.ERROR, "Failed to logout player.", exc);
					}
					
					break;
				case GAME:
					
					try {
						if(pokerGame != null) {
							pokerGame.goLobby(session.getId());
						}
					
					}catch(RemoteException exc) {
						Logger.write(LoggingLevel.ERROR, "Failed to disconnect player from game.", exc);
					}

					pokerGame = null;
					
					try {
						
						GameRemoteService.getGameServer().logoutPlayer(session.getId());
					
					} catch (RemoteException exc) {
						Logger.write(LoggingLevel.ERROR, "Failed to logout player.", exc);
					}
					
					break;
				default:
					break;
				}
				
				currentMode = null;
				
				if(gameEventHandler != null) {
					gameEventHandler.interrupt();
				}
				
				if(guiEventHandler != null) {
					guiEventHandler.interrupt();
				}
				
				System.exit(0);
			}
		});
		
		setVisible(true);
	}
	
	public GameClientApplication() {
		createGUI();
	}
	
	public static void main(String[] args) {

		PropertyConfigurator.configure("log4j.properties");
		
		if(System.getSecurityManager() == null){
			
			System.setSecurityManager(new SecurityManager());
		
		}

		try {
			
			ResourceManager.loadResources();
		
		}catch(Exception exc) {
			Logger.write(LoggingLevel.FATAL, "Failed to load game resources.", exc);
		}

		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				new GameClientApplication();
			
			}
		});
	}
	
	private void swapScreen(DisplayMode mode) {
		
		if(playerName != null) {
			GameClientApplication.this.setTitle(TITLE + " - " + playerName );
		}
		
		cancelTimer();
		
		if(currentMode == DisplayMode.GAME) {
			gamePanelMediator.reset();
		}
		
		currentMode = mode;
		
		if(mode == DisplayMode.LOBBY) {
			updateLobbyData();			
		} 
		
		CardLayout cardsLayout = (CardLayout) screens.getLayout();
		cardsLayout.show(screens,currentMode.name());
	}
	
	private void updateLobbyData() {
	
		if(currentMode == DisplayMode.LOBBY) {
			
			Session session = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());
			
			lobbyPanel.setCurrentBalance(session.getPlayer().getBalance());
			
			lobbyPanel.loadData(false);
			
			activateTimer();
		}
	}
	
	private void activateTimer() {
		
		lobbyUpdateTimer = new Timer();
		
		lobbyUpdateTimer.schedule(new DataUpdateTask(), UPDATE_FREQUENCY, UPDATE_FREQUENCY);
	}
	
	private void cancelTimer() {
		
		if(lobbyUpdateTimer != null) {
			
			lobbyUpdateTimer.cancel();
		
			lobbyUpdateTimer = null;
		
		}
	}
	
	private class DataUpdateTask extends TimerTask {

		@Override
		public void run() {
			
			Session session = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());
			
			try {
			
				session = GameRemoteService.getGameServer().getSession(session.getId());
				
			}catch(RemoteException exc) {
				
				Logger.write(LoggingLevel.ERROR,"Failed to request a session from server", exc);
			
			}

			if(session != null) {
				
				ApplicationContext.set(ApplicationGlobals.SESSION.name(), session);
			
			} else {
				Logger.write(LoggingLevel.WARN, "Session is NULL");
			}

			
			lobbyPanel.loadData(false);
			lobbyPanel.setCurrentBalance(session == null ? BigDecimal.ZERO : session.getPlayer().getBalance());
		}		
	}

	private MouseAdapter mouseClickAdapter = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			if(e.getClickCount() == 2) {
				
				JTable table = (JTable) e.getSource();
				
				LobbyPanel.TablesDataModel model = (LobbyPanel.TablesDataModel) table.getModel(); 
				
				int index = table.convertRowIndexToModel(table.getSelectedRow());
				
				TableInfo row = model.getRow(index);
				
				Session session = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());
				
				pokerGame = null;
				
				try {
					
					pokerGame = GameRemoteService.getGameServer().joinGame(row.getId(), session.getId());
					
					if(pokerGame != null) {

						GameContext gameContext = pokerGame.getGameContext();
						
						ApplicationContext.set(ApplicationGlobals.GAME_CONTEXT.name(), gameContext);
						
						gamePanelMediator.updateSeatsAndPlayers();
						
						gamePanelMediator.setBottomPanelEnabled(false);
						
						gamePanelMediator.updateBalanceRakePot();

						gamePanelMediator.updateBetAmountSlider();
						
						gamePanel.getPlayerNameLabel().setText(session.getPlayer().getName());
						
						swapScreen(DisplayMode.GAME);
						
						gameEventHandler = new GameEventHandler();
						
						gameEventHandler.start();
						
						guiEventHandler = new GuiEventHandler();
						
						guiEventHandler.start();

					}
				}catch(Exception exc) {
					Logger.write(LoggingLevel.ERROR, "Failed to join a game.", exc);
				}
			}else {
				super.mouseClicked(e);
			}
		}
	};
	
	
	class GameEventHandler extends Thread {

		@Override
		public void run() {
			
			while(currentMode == DisplayMode.GAME) {
				
				GameEvent event = null;
				
				Session gameSession = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());

				if(Thread.interrupted()) return;				

				if (pokerGame == null) return;
				
				try {
					
					event = pokerGame.getGameEvent(gameSession.getId());
				
				}catch (Exception exc) {
					Logger.write(LoggingLevel.ERROR, "Error occured while getting a game event", exc);
				}
				
				if(event == null) continue;
				
				Logger.write(LoggingLevel.TRACE, "Client receives event: " + event.getType());

				if(event.getType() == EventType.HAND_FINISHED) {
					
					ApplicationContext.remove(ApplicationGlobals.HOLE_CARDS.name());
					
					ApplicationContext.remove(ApplicationGlobals.HAND.name());
				}

				try {
					
					ApplicationContext.set(ApplicationGlobals.GAME_CONTEXT.name(), pokerGame.getGameContext());
					
					if(event.getType() == EventType.HOLE_CARDS_DEALED) {
						
						Set<Card> cards = pokerGame.getHoleCards(gameSession.getId());
						
						if(cards != null) {
							
							Card [] holeCards = cards.toArray(new Card[] {});
							
							ApplicationContext.set(ApplicationGlobals.HOLE_CARDS.name(), holeCards);
						
						} else {
							
							ApplicationContext.remove(ApplicationGlobals.HOLE_CARDS.name());
						
						}
					}
					
					if(event.getType() == EventType.SHOWDOWN) {

						PlayerHand hand = pokerGame.getHand(gameSession.getId());
						
						if(hand != null) {
							
							ApplicationContext.set(ApplicationGlobals.HAND.name(), hand);
						
						}
					}
					

					if(Thread.interrupted()) return;
					
					gamePanelMediator.update();
					
					try {
						Thread.sleep(200);
					}catch(InterruptedException exc) {}
				}
				catch(Exception exc) {
					
					Logger.write(LoggingLevel.ERROR, "Failed to update game context.", exc);
				
				}
				
				if(Thread.interrupted()) return;
			}
		}
	}
	
	class GuiEventHandler extends Thread {

		@Override
		public void run() {
			
			while(currentMode == DisplayMode.GAME) {
				
				GuiEvent event = null;
				
				try {
					
					if(Thread.interrupted()) return;
					
					event = guiActionsQueue.take();
				
				}catch(InterruptedException exc) {
					return;
				}
				
				if(event == null) continue;

				if(event.getActionType() == ActionType.FOLD) {
					ApplicationContext.remove(ApplicationGlobals.HOLE_CARDS.name());
				}

				Session session = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());
				
				BigDecimal value = event.getValue() == null ? null : (BigDecimal) event.getValue();
				
				try {
					
					pokerGame.action(session.getId(), event.getActionType(), value);
					
				}catch(RemoteException exc) {
					Logger.write(LoggingLevel.ERROR, "Failed to send player action to server.", exc);
				}
				
				if(Thread.interrupted()) return;
			}
		}
	}
	
	private void sendGuiEvent(GuiEvent event) {
		try {
			
			guiActionsQueue.add(event);
		
		}catch(Exception exc) {
		
			Logger.write(LoggingLevel.ERROR, "Error occured while queuing a gui event of action type: " + event.getActionType() , exc);
		
		}
	}
	
	class GamePanelMediator implements ActionListener {
		
		//components
		private GamePanel gamePanel;
		
		private BetAmountSlider betAmountSlider;
		
		private BetAmountLabel betAmountLabel;

		private JLabel playerBalanceLabel;
		
		private JLabel rakeLabel;
		
		private JLabel potLabel;
		
		
		
		
		//variables
		private int playerInTurn = -1;

		private Timer currentTimer;
		
		
		public GamePanelMediator(GamePanel panel) {
			
			gamePanel = panel;

			betAmountLabel = gamePanel.getBetAmountLabel();
			
			playerBalanceLabel = gamePanel.getPlayerBalanceLabel();
			
			//rakeLabel = gamePanel.getCentralPanel().getRakeAmountLabel();
			
			potLabel = gamePanel.getPotAmountLabel();

			
			
			initSeatButtons();
			
			initLobbyButton();

			initBetAmountSlider();
			
			initActionButtons();
			
			setBottomPanelEnabled(false);
			
		}
		
		private void initSeatButtons() {

			for(JButton button : gamePanel.getSeatButtons()) {
				button.addActionListener(seatButtonListener);
			}
		}
		
		private ActionListener seatButtonListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SeatButton button = (SeatButton) e.getSource();
				
				GameContext gameCntx = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
				
				Session session = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());
				
				if(session.getPlayer().getBalance().compareTo(gameCntx.table.getMinBayin()) < 0) {
					
					String [] opts = {"Close"};
					
					JOptionPane.showOptionDialog(
							GameClientApplication.this, 
							"Not enough money. Min buy in: $" + GUIUtils.decimal2text(gameCntx.table.getMinBayin()), 
							"Invalid transaction",
							JOptionPane.OK_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							null, opts, opts[0]);
					
					return;
				}
				
				BuyinDialog dlg = new BuyinDialog(
						GameClientApplication.this, 
						true, 
						gameCntx.table.getMinBayin(),
						gameCntx.table.getMaxBayin() != null ? 
								MiscUtils.min(gameCntx.table.getMaxBayin(), session.getPlayer().getBalance()) : session.getPlayer().getBalance() );

				if(dlg.getDialogStatus() == JOptionPane.CANCEL_OPTION) return;
				
				int seatNumber = button.getSeatNumber();
				
				boolean success = false;
				
				try {
					
					success = pokerGame.takeSeat(session.getId(), button.getSeatNumber(), dlg.getBuyinValue());
					
				}catch(RemoteException exc) {
					Logger.write(LoggingLevel.ERROR, String.format("Failed to sit down at a poker table (seat # %1$d, table # %2$d).", seatNumber, gameCntx.table.getId()), exc);
				}
				
				if(success) {
					
					session.getPlayer().setBalance(session.getPlayer().getBalance().subtract(dlg.getBuyinValue()));
					
					ApplicationContext.set(ApplicationGlobals.SEAT_NUM.name(), seatNumber);
				}
			}
		};

		
		private void initLobbyButton() {
			
			JButton button = gamePanel.getLobbyButton();
			
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					Session session = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());
					
					try {

						pokerGame.goLobby(session.getId());

					}catch(RemoteException exc) {
						Logger.write(LoggingLevel.ERROR, "Failed to leave a table.", exc);
					}

					gamePanelMediator.reset();
					
					ApplicationContext.remove(ApplicationGlobals.GAME_CONTEXT.name());
					
					ApplicationContext.remove(ApplicationGlobals.SEAT_NUM.name());
					
					ApplicationContext.remove(ApplicationGlobals.HOLE_CARDS.name());
					
					ApplicationContext.remove(ApplicationGlobals.HAND.name());
					

					swapScreen(DisplayMode.LOBBY);

					
					gameEventHandler.interrupt();
					
					guiEventHandler.interrupt();
					
					

					pokerGame = null;
				}
			});
		}
		
		
		private void initBetAmountSlider() {
			
			betAmountSlider = gamePanel.getBetAmountSlider();
			
			betAmountSlider.setMinimum(0);
			
			betAmountSlider.setMaximum(0);
			
			betAmountSlider.setValue(0);
			
			
			betAmountSlider.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					updateBetAmount();
				}
			});
			
		}
		
		
		private void initActionButtons() {
			
			for(ActionButton btn : gamePanel.getButtons().values()) {
				btn.addActionListener(this);
			}
		}
		
		public void update() {
			
			updateBalanceRakePot();
			
			updateSeatsAndPlayers();
			
			updateActionButtons();
			
			//updateToggleButtons();

			updateBetAmountSlider();
			
			updateHoleCardsSwitcher();

			gamePanel.repaint();

			updateCurrentPlayer();
			
			//autoAction();
		}
		
		private void updateBetAmount() {
		
			
			GameContext gameCntx = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
			
			Integer seatNumber = (Integer) ApplicationContext.get(ApplicationGlobals.SEAT_NUM.name());
			
			if(seatNumber == null) return;
			
			Player player = gameCntx.handPlayers[seatNumber - 1];
			
			if(player == null) return;
			
			betAmountLabel.setAmount(betAmountSlider.getCurrentAmount());
		}
		
		
		private void updateBetAmountSlider() {
			
			GameContext gameCntx = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
			
			Integer seatNumber = (Integer) ApplicationContext.get(ApplicationGlobals.SEAT_NUM.name());
			
			if(seatNumber == null) {
			
				betAmountSlider.setMinimum(0);
				
				betAmountSlider.setMaximum(0);
				
				betAmountSlider.setValue(0);
				
				return;
			}
			
			Player player = gameCntx.handPlayers[seatNumber - 1];
			
			if(player == null || player.getChipsStack().compareTo(BigDecimal.ZERO) == 0) {
				
				betAmountSlider.setMinimum(0);
				
				betAmountSlider.setMaximum(0);
				
				betAmountSlider.setValue(0);
				
				return;
			}
			
			BigDecimal callBetAmount = gameCntx.lastBetAmount.subtract(player.getCurrentBetAmount());
			
			BigDecimal raiseBetAmount = BigDecimal.ZERO;
			
			switch(gameCntx.table.getGameType()) {
			case FIXED_BETS:

				raiseBetAmount = MiscUtils.min(gameCntx.getMinimumBetAmount(), MiscUtils.max(player.getChipsStack().subtract(callBetAmount), BigDecimal.ZERO));
				
				betAmountLabel.setAmount(raiseBetAmount);
				
				betAmountSlider.setMinimum(0);
				
				betAmountSlider.setValue(0);
				
				betAmountSlider.setMaximum(0);

				betAmountSlider.setVisible(false);
				
				betAmountSlider.setEnabled(false);
				
				break;
				
			case NOLIMIT:

				raiseBetAmount = MiscUtils.min(MiscUtils.max(gameCntx.getMinimumBetAmount(),callBetAmount), MiscUtils.max(player.getChipsStack().subtract(callBetAmount), BigDecimal.ZERO));
				
				betAmountLabel.setAmount(raiseBetAmount);
				
				BigDecimal minValue = raiseBetAmount;
				
				BigDecimal maxValue = player.getChipsStack().subtract(callBetAmount);

				
				//if(maxValue.compareTo(BigDecimal.ONE) > 0) {
				//	maxValue = maxValue.round(new MathContext(0, RoundingMode.UP));
				//}
				
				BigDecimal increment = BigDecimal.ONE;
				
				if(gameCntx.table.getBigBlind().compareTo(BigDecimal.ONE) < 0) {
					increment = new BigDecimal(0.01);
				}
				
				int intMaxValue = maxValue.subtract(minValue).divide(increment, RoundingMode.UP).toBigInteger().intValue();
				
				betAmountSlider.setMinimum(0);
				
				betAmountSlider.setMinAmount(minValue);
				
				betAmountSlider.setValue(0);
				
				betAmountSlider.setMaximum(intMaxValue);
				
				betAmountSlider.setMaxAmount(maxValue);
				
				betAmountSlider.setIncAmount(increment);
				
				betAmountSlider.setVisible(true);
				
				betAmountSlider.setEnabled(gameCntx.currentPlayer == seatNumber - 1);
				
				break;
			} 
			
		}
		
		private void updateActionButtons() {

			Integer seatNumber = (Integer) ApplicationContext.get(ApplicationGlobals.SEAT_NUM.name());
			
			if(seatNumber == null) {

				for(JButton btn : gamePanel.getButtons().values()) {
					btn.setEnabled(false);
				}
				
				return;
			}
			
			GameContext gameCntx = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
			
			Set<ActionType> allowedActions = gameCntx.getAllowedActions(seatNumber.intValue() - 1);
			
			boolean isCurrentPlayer = (seatNumber - 1 == gameCntx.currentPlayer);
			
			Player player = gameCntx.handPlayers[seatNumber - 1];
			
			if(player == null) {

				for(JButton btn : gamePanel.getButtons().values()) {
					btn.setEnabled(false);
				}
				
				return;
			}

			boolean allIn = player.getChipsStack().compareTo(BigDecimal.ZERO) == 0;
			
			gamePanel.getButtons().get(ActionButtonType.FOLD).setEnabled(allowedActions.contains(ActionType.FOLD) && isCurrentPlayer);
			
			gamePanel.getButtons().get(ActionButtonType.CHECK).setEnabled(allowedActions.contains(ActionType.CHECK) && isCurrentPlayer && !allIn);
			
			gamePanel.getButtons().get(ActionButtonType.CHECK).setVisible(allowedActions.contains(ActionType.CHECK) || !allowedActions.contains(ActionType.CALL));

			gamePanel.getButtons().get(ActionButtonType.CALL).setEnabled(allowedActions.contains(ActionType.CALL) && isCurrentPlayer && !allIn);
			
			gamePanel.getButtons().get(ActionButtonType.CALL).setVisible(allowedActions.contains(ActionType.CALL));

			gamePanel.getButtons().get(ActionButtonType.BET).setEnabled(allowedActions.contains(ActionType.BET)  && isCurrentPlayer && !allIn);
			
			gamePanel.getButtons().get(ActionButtonType.BET).setVisible(allowedActions.contains(ActionType.BET) || !allowedActions.contains(ActionType.RAISE));

			BigDecimal betAmount = gameCntx.lastBetAmount.subtract(player.getCurrentBetAmount());
			
			boolean notEnoughFunds = betAmount.compareTo(player.getChipsStack()) >= 0;
			
			gamePanel.getButtons().get(ActionButtonType.RAISE).setEnabled(allowedActions.contains(ActionType.RAISE)  && isCurrentPlayer && !allIn && !notEnoughFunds);
			
			gamePanel.getButtons().get(ActionButtonType.RAISE).setVisible(allowedActions.contains(ActionType.RAISE));

		}
		/*
		private void updateToggleButtons() {
			
			Integer seatNumber = (Integer) ApplicationContext.get(ApplicationGlobals.SEAT_NUM.name());
			
			if(seatNumber == null) {

				for(ToggleButton btn : gamePanel.getBottomPanel().getToggleButtons().values()) {
					
					btn.setEnabled(false);
				
				}
				
				return;
			}

			GameContext gameCntx = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
			
			Player player = gameCntx.handPlayers[seatNumber - 1];
			
			if(player == null) {

				for(ToggleButton btn : gamePanel.getBottomPanel().getToggleButtons().values()) {
					
					btn.setEnabled(false);
				
				}
				
				return;
			}

			Set<ActionType> allowedActions = gameCntx.getAllowedActions(seatNumber.intValue() - 1);
			
			ToggleButtonType type = gamePanel.getBottomPanel().getSelectedToggleButtonType();
			
			boolean allIn = player.getChipsStack().compareTo(BigDecimal.ZERO) == 0;
			
			gamePanel.getBottomPanel().getToggleButtons().get(ToggleButtonType.FOLD_CHECK).setEnabled(allowedActions.contains(ActionType.FOLD) && !allIn);
			
			gamePanel.getBottomPanel().getToggleButtons().get(ToggleButtonType.FOLD).setEnabled(allowedActions.contains(ActionType.FOLD) && !allIn);
			
			
			if(!allowedActions.contains(ActionType.FOLD)) {

				if(type == ToggleButtonType.FOLD || type == ToggleButtonType.FOLD_CHECK) {
					
					gamePanel.getBottomPanel().setSelectedToggleButtonType(null);
					
					gamePanel.getBottomPanel().getToggleButtons().get(type).setSelected(false);
				}
			}
			
			gamePanel.getBottomPanel().getToggleButtons().get(ToggleButtonType.CHECK).setEnabled(allowedActions.contains(ActionType.CHECK) && !allIn);

			if(!allowedActions.contains(ActionType.CHECK)) {
				
				if(type == ToggleButtonType.CHECK) {
					
					gamePanel.getBottomPanel().setSelectedToggleButtonType(null);
					
					gamePanel.getBottomPanel().getToggleButtons().get(type).setSelected(false);
				}
			}
			
			gamePanel.getBottomPanel().getToggleButtons().get(ToggleButtonType.CALL).setEnabled(allowedActions.contains(ActionType.CALL) && !allIn);
			
			if(!allowedActions.contains(ActionType.CALL)) {
				
				if(type == ToggleButtonType.CALL) {
					
					gamePanel.getBottomPanel().setSelectedToggleButtonType(null);
					
					gamePanel.getBottomPanel().getToggleButtons().get(type).setSelected(false);
				}
			}
		}*/
		
		
		private void setBottomPanelEnabled(boolean enabled) {
			
			for(JButton b : gamePanel.getButtons().values()) {
				b.setEnabled(enabled);
			}
			
			gamePanel.getBetAmountSlider().setEnabled(enabled);
		}
		
		/*
		public void autoAction() {

			Integer seatNumber = (Integer) ApplicationContext.get(ApplicationGlobals.SEAT_NUM.name());
			
			if(seatNumber == null) return;
			
			ToggleButtonType type = gamePanel.getBottomPanel().getSelectedToggleButtonType();
			
			if( type == null) return;
			

			GameContext gameCntx = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
			
			Set<ActionType> actions = gameCntx.getAllowedActions( seatNumber.intValue() - 1);
			
			switch(type) {
			case FOLD_CHECK:
				
				if(actions.contains(ActionType.CHECK)) {
					sendGuiEvent(new GuiEvent(ActionType.CHECK));
				} else {
					sendGuiEvent(new GuiEvent(ActionType.FOLD));
				}
				
				break;
			case FOLD:
				sendGuiEvent(new GuiEvent(ActionType.FOLD));				
				break;
			case CHECK:
				if(actions.contains(ActionType.CHECK)) {
					sendGuiEvent(new GuiEvent(ActionType.CHECK));
				}
				break;
			case CALL:
				if(actions.contains(ActionType.CALL)) {
					sendGuiEvent(new GuiEvent(ActionType.CALL));
				}
				break;
			}
			
			unselectToggleButtons();
		} */
		/*
		private void unselectToggleButtons() {
			
			for(ToggleButton btn : gamePanel.getBottomPanel().getToggleButtons().values()) {
				btn.setSelected(false);
			}
			
			gamePanel.getBottomPanel().setSelectedToggleButtonType(null);
		}
		
		*/
		
		private void updateSeatsAndPlayers() {

			GameContext gameContext = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
			
			Integer seatNumber = (Integer) ApplicationContext.get(ApplicationGlobals.SEAT_NUM.name());
			
			PlayerHand myHand = (PlayerHand) ApplicationContext.get(ApplicationGlobals.HAND.name());
			
			PlayerInfoBoxPanel [] panels = gamePanel.getPlayerBoxPanels();
			
			JButton [] buttons = gamePanel.getSeatButtons();
			
			
			for(int i = 0; i < gameContext.activePlayers.length;i++) {
				
				commons.game.poker.Player p = gameContext.activePlayers[i];
				
				if(gameContext.bettingRound == HandRound.SHOWDOWN 
						&& seatNumber != null 
						&& p != null 
						&& p.getSeatNumber() == seatNumber) {
					
					p.setHand(myHand);
				}
				
				final PlayerInfoBoxPanel panel = panels[i];
				
				panel.setPlayerInfo(p);
				
				panel.setVisible(p != null);
				
				buttons[i].setVisible(seatNumber == null && p == null);
			}
		}
		
		
		
		private void updateHoleCardsSwitcher() {
			
			Card [] holeCards = (Card[]) ApplicationContext.get(ApplicationGlobals.HOLE_CARDS.name());
			
			if(gamePanel.getHoleCardsSwitcher().getCards() != holeCards) {
				gamePanel.getHoleCardsSwitcher().setCards(holeCards);
			}
		}
		
		
		private void updateCurrentPlayer() {
			
			GameContext gameContext = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
			
			PlayerInfoBoxPanel [] panels = gamePanel.getPlayerBoxPanels();
			
			if(gameContext.currentPlayer != playerInTurn) {

				if(playerInTurn > -1) {
				
					panels[playerInTurn].stopTimer();
					
					if(currentTimer != null) {
						currentTimer.cancel();
						currentTimer = null;
					}
				}
				
				playerInTurn = gameContext.currentPlayer;
				
				if(playerInTurn > -1) {
					
					final PlayerInfoBoxPanel panel = panels[playerInTurn];
					
					panel.startTimer(30);
					
					TimerTask tt = new TimerTask() {
						
						@Override
						public void run() {
							panel.stopTimer();
						}
					};
					
					currentTimer = new Timer();
					
					currentTimer.schedule(tt, 31000);
				}
			}			
		}
	
		private void updateBalanceRakePot() {
			
			Session s = (Session) ApplicationContext.get(ApplicationGlobals.SESSION.name());
			
			GameContext gc = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
			
			Integer seatNumber = (Integer) ApplicationContext.get(ApplicationGlobals.SEAT_NUM.name());
			
			Player p = null;
			
			if( seatNumber != null ) {
				p = gc.activePlayers[seatNumber - 1];
			}
			
			BigDecimal chips = BigDecimal.ZERO;
			
			if(p != null) {
				chips = p.getChipsStack();
			}
			
			BigDecimal balance = s.getPlayer().getBalance().add(chips);
			
			playerBalanceLabel.setText("Баланс: $" + GUIUtils.decimal2text(balance));
			
			potLabel.setText("Банк: $" + GUIUtils.decimal2text(gc.pot));
			
			//rakeLabel.setText("Рэйк: $" + GUIUtils.decimal2text(gc.rake));
		}
		
		
		public void reset() {
			
			playerInTurn = -1;
			
			if(currentTimer != null) {
				currentTimer.cancel();
				currentTimer = null;
			}
			
			gamePanel.getButtons().get(ActionButtonType.CHECK).setVisible(true);
			gamePanel.getButtons().get(ActionButtonType.CALL).setVisible(false);
			
			gamePanel.getButtons().get(ActionButtonType.BET).setVisible(true);
			gamePanel.getButtons().get(ActionButtonType.RAISE).setVisible(false);
			
			gamePanel.getBetAmountSlider().setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() instanceof ActionButton) {
				
				ActionButton btn = (ActionButton) e.getSource();
				
				switch(btn.getActionType()) {
				case FOLD:
				case CHECK:
				case CALL:
					sendGuiEvent(new GuiEvent(btn.getActionType()));
					break;
				case BET:
				case RAISE:
					sendGuiEvent(new GuiEvent(btn.getActionType(), betAmountLabel.getAmount()));
					break;
				default:
					break;
				}
			}
		}
	}
}
