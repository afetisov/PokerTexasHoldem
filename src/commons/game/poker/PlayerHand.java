package commons.game.poker;

import java.io.Serializable;
import java.util.List;

public class PlayerHand implements Serializable, Comparable<PlayerHand> {

	private HandRank handRank;
	
	private List<Card> handCards;
	
	private Card[] holeCards;
	
	public PlayerHand() {}
	
	public PlayerHand(HandRank rank, List<Card> cards) {
		this.handRank = rank;
		this.handCards = cards;
	}

	public HandRank getHandRank() {
		return handRank;
	}

	public void setHandRank(HandRank handRank) {
		this.handRank = handRank;
	}

	public List<Card> getHandCards() {
		return handCards;
	}

	public void setHandCards(List<Card> handCards) {
		this.handCards = handCards;
	}
	
	public Card[] getHoleCards() {
		return holeCards;
	}

	public void setHoleCards(Card[] holeCards) {
		this.holeCards = holeCards;
	}

	@Override
	public String toString() {
		return (handRank == null || handCards == null)? null: handRank.toString() + ":" + handCards.toString();
	}
	
	
	
	@Override
	public int compareTo(PlayerHand other) {
		
		int comp = handRank.compareTo(other.getHandRank());
		
		if(comp != 0) return comp;
		
		if(handRank == HandRank.RoyalFlush) return 0;
		
		List<Card> otherCards = other.getHandCards();
		
		for(int i = 0; i < 5; i++){
			
			comp = handCards.get(i).compareTo(otherCards.get(i));
			
			if( comp != 0) return comp;
		}
		
		return 0;
	}

}
