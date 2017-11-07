package commons.game.poker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

public class Player implements Serializable {
	
	private static final int MAX_HANDS_INACTIVE = 2;
	
	private String nickname;
	
	private BigDecimal chipsStack = BigDecimal.ZERO;
	
	private int seatNumber = -1;
	
	private BigDecimal totalBetAmount = BigDecimal.ZERO;
	
	private BigDecimal currentBetAmount = BigDecimal.ZERO;
	
	private boolean sittingOut;
	
	private transient Set<Card> holeCards;
	
	private ActionType lastAction;
	
	//it isn't used yet but is intended to be used as a flag 
	//for users that have left a game but the user's bets still 
	//are valuable for current hand and will be included in a pot
	private boolean fantom;
	
	
	private PlayerHand hand;
	
	private BigDecimal wonChipsAmount = BigDecimal.ZERO;
	
	private boolean folded;
	
	private transient BigDecimal balance = BigDecimal.ZERO;
	
	private boolean allIn;
	
	private transient String sessionID;
	
	private transient boolean waitingForBuyIn;
	
	public Player () {}
	
	public Player (String nickname, BigDecimal stack, BigDecimal balance, int seatNum, String sessionID) {
		
		this.nickname = nickname;
		
		this.chipsStack = stack;
		
		this.balance = balance;
		
		this.seatNumber = seatNum;
		
		this.sessionID = sessionID;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public BigDecimal getChipsStack() {
		return chipsStack;
	}

	public void setChipsStack(BigDecimal chipsStack) {
		this.chipsStack = chipsStack;
	}

	public Set<Card> getHoleCards() {
		return holeCards;
	}

	public void setHoleCards(Set<Card> holeCards) {
		this.holeCards = holeCards;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public boolean isSittingOut() {
		return sittingOut;
	}

	public void setSittingOut(boolean sittingOut) {
		this.sittingOut = sittingOut;
	}

	public BigDecimal getTotalBetAmount() {
		return totalBetAmount;
	}

	public void setTotalBetAmount(BigDecimal totalBetAmount) {
		this.totalBetAmount = totalBetAmount;
	}

	public BigDecimal getCurrentBetAmount() {
		return currentBetAmount;
	}

	public void setCurrentBetAmount(BigDecimal currentBetAmount) {
		this.currentBetAmount = currentBetAmount;
	}

	public boolean isFantom() {
		return fantom;
	}

	public void setFantom(boolean fantom) {
		this.fantom = fantom;
	}

	public ActionType getLastAction() {
		return lastAction;
	}

	public void setLastAction(ActionType lastAction) {
		this.lastAction = lastAction;
	}
	
	public PlayerHand getHand() {
		return hand;
	}

	public void setHand(PlayerHand hand) {
		this.hand = hand;
	}

	public BigDecimal getWonChipsAmount() {
		return wonChipsAmount;
	}

	public void setWonChipsAmount(BigDecimal wonChipsAmount) {
		this.wonChipsAmount = wonChipsAmount;
	}

	public boolean isFolded() {
		return folded;
	}

	public void setFolded(boolean folded) {
		this.folded = folded;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public boolean isAllIn() {
		return allIn;
	}

	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}


	public boolean isWaitingForBuyIn() {
		return waitingForBuyIn;
	}

	public void setWaitingForBuyIn(boolean waitingForBuyIn) {
		this.waitingForBuyIn = waitingForBuyIn;
	}

	/**
	 * Resets player's variables
	 * 
	 * @param all - If true then resets total bet amount (collection of bets within a whole hand),
	 *  current bet amount(collection of bets within current betting round only), hole cards,
	 *  last action, hand, won pot and folded 
	 *  otherwise resets only hole cards, last action, hand, won pot and folded. 
	 *  This is needed when player leaves the game before a hand is finished. For calculating a 
	 *  resulting pot the left players' bet collection is still actual.
	 * 
	 */
	public void reset(boolean all) {
		
		if(all) {
			
			totalBetAmount = BigDecimal.ZERO;
			
			currentBetAmount = BigDecimal.ZERO;
		}
		
		holeCards = null;
		
		lastAction = null;
		
		hand = null;
		
		wonChipsAmount = BigDecimal.ZERO;
		
		hand = null;
		
		folded = false;
		
		allIn = false;
	}
}
