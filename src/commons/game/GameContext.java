package commons.game;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import commons.game.poker.ActionType;
import commons.game.poker.Card;
import commons.game.poker.HandRound;
import commons.game.poker.Player;
import commons.game.poker.PlayerHand;
import commons.model.TableInfo;

public class GameContext implements Serializable {
	
	//general data
	public TableInfo table;
	
	public Player [] activePlayers = new Player[TableOccupancyType.MAX_PLAYERS];
	
	
	//hand data
	public Player [] handPlayers = new Player[TableOccupancyType.MAX_PLAYERS];

	public transient PlayerHand [] handValues = new PlayerHand[TableOccupancyType.MAX_PLAYERS];
	
	public transient Stack<Card> cardPack;

	public List<Card> board = new ArrayList<Card>(5);
	
	public int dealer = -1;
	
	public HandRound bettingRound;
	
	public BigDecimal pot = BigDecimal.ZERO;
	
	public BigDecimal rake = BigDecimal.ZERO;
	
	public boolean headsUp;
	
	public boolean onePlayerLeft;

	
	
	
	//round data
	public int raisesCounter;
	
	public int smallBlindPlayer = -1;
	
	public int bigBlindPlayer = -1;
	
	public int firstBetPlayer = -1;
	
	public int lastBetPlayer = -1;
	
	public int currentPlayer = -1;
	
	public BigDecimal lastBetAmount = BigDecimal.ZERO;


	public void resetRound() {
		
		//reset variables before next betting round begins
		for(Player p : handPlayers) {
			if (p != null ) {
				p.setCurrentBetAmount(BigDecimal.ZERO);
				p.setLastAction(null);
			}
		}
		
		currentPlayer = -1;
		lastBetAmount = BigDecimal.ZERO;
		lastBetPlayer = -1;
		raisesCounter = 0;
	}
	
	public void resetHand() {

		for(int i = 0; i < handPlayers.length;i++) {
			
			if(handPlayers[i] !=null ) {
				handPlayers[i].reset(true);
			}
			
			handPlayers[i] = null;
			handValues[i] = null;
		}
		
		cardPack = null;
		
		board.clear();
		
		bettingRound = null;
		
		pot = BigDecimal.ZERO;
		
		raisesCounter = 0;
		
		smallBlindPlayer = -1;
		
		bigBlindPlayer = -1;
		
		lastBetPlayer = -1;
		
		firstBetPlayer = -1;
		
		currentPlayer = -1;
		
		lastBetAmount = BigDecimal.ZERO;
		
		rake = BigDecimal.ZERO;
		
		headsUp = false;
		
		onePlayerLeft = false;
	}
	
	public Set<ActionType> getAllowedActions(int playerNumber) {
		
		Set<ActionType> actions = new HashSet<ActionType>(ActionType.values().length);

		if ( playerNumber < 0 || playerNumber > handPlayers.length - 1 || handPlayers[playerNumber] == null) return actions;
		
		Player player = handPlayers[playerNumber];
		
		BigDecimal minBetAmount = lastBetAmount.subtract(player.getCurrentBetAmount());
		
		boolean isEnoughFunds = minBetAmount.compareTo(player.getChipsStack()) < 0;
		
		actions.add(ActionType.FOLD);
		
		if( player.getCurrentBetAmount().compareTo(lastBetAmount) == 0) {
			
			actions.add(ActionType.CHECK);
		
			if(!player.isAllIn() && 
					(table.getGameType() == GameType.NOLIMIT 
					|| headsUp 
					|| table.getCapSize() > raisesCounter)) {
				
				if(lastBetPlayer == -1) {
				
					actions.add(ActionType.BET);	
				
				} else if (isEnoughFunds ){
					
					actions.add(ActionType.RAISE);
				
				}
			}
		} else {
			
			actions.add(ActionType.CALL);
			
			if(!player.isAllIn() && isEnoughFunds &&
					(table.getGameType() == GameType.NOLIMIT 
					|| headsUp 
					|| table.getCapSize() > raisesCounter)) {
				
				actions.add(ActionType.RAISE);
			
			}
		}
		
		return actions;
	}
	
	
	public int getActivePlayersCount() {

		int count = 0;
		
		for(int i = 0; i < handPlayers.length; i++) {
		
			Player p = handPlayers[i];
			
			if( p == null 
					|| activePlayers[i] != p // if player is not sitting at the poker table
					|| (p.getChipsStack().equals(BigDecimal.ZERO) && p.getHoleCards() == null) //if player has no money and there are no hole cards in his hands
					|| p.isFolded() //if player has folded
					|| p.isAllIn() //if player is all-in
					|| (p.getHoleCards() == null && bettingRound != null) ) { //if player has no hole cards and betting round is pre-flop
				
				continue;
			}
			
			count++;
		}
		
		return count;
	}
	
	
	public boolean skipBettingRound() {

		int count = 0;
		
		for(int i = 0; i < handPlayers.length; i++) {
			
			Player p = handPlayers[i];
			
			if( p == null 
					|| activePlayers[i] != p 
					|| (p.getChipsStack().equals(BigDecimal.ZERO) && p.getHoleCards() == null) 
					|| p.isFolded() 
					|| p.isAllIn()
					|| (p.getHoleCards() == null && bettingRound != null) ) {
				
				continue;
			}
			
			if(p.getChipsStack().compareTo(BigDecimal.ZERO) > 0) {
				count++;
			}
		}

		return count < 2; 
	}
	
	
	public BigDecimal getMinimumBetAmount() {
		
		if(bettingRound != null && bettingRound.ordinal() > HandRound.FLOP.ordinal()) {
		
			return table.getBigBlind().multiply(new BigDecimal(2.0));
		}
		
		return table.getBigBlind();
	}
	
	
	public int getLastActivePlayer() {
		
		for(int i = 0; i < handPlayers.length; i++) {
			
			Player p = handPlayers[i];

			if( p == null 
					|| activePlayers[i] != p 
					|| (p.getChipsStack().equals(BigDecimal.ZERO) && p.getHoleCards() == null) 
					|| p.isFolded() 
					|| (p.getHoleCards() == null && bettingRound != null) ) {
				
				continue;
			}
			
			return i;
		}
		
		return -1;
	}
	
	public boolean isHeadsUp() {
		
		int count = 0;
		
		for(int i = 0; i < handPlayers.length; i++) {
			
			Player p = handPlayers[i];
			
			if( p == null 
					|| activePlayers[i] != p 
					|| (p.getChipsStack().equals(BigDecimal.ZERO) && p.getHoleCards() == null) 
					|| (p.getHoleCards() == null && bettingRound != null) ) {
				
				continue;
			}
			
			count++;
		}

		return count == 2; 		
	}
}
