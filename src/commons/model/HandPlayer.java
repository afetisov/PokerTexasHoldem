package commons.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

@Embeddable
public class HandPlayer implements Serializable {

	@org.hibernate.annotations.Parent
	@ForeignKey(name = "fk_hand_player_hand_id")
	private Hand hand;
	
	@ManyToOne
	@JoinColumn(
			name = "player_id",
			nullable = false,
			updatable = false
	)

	@ForeignKey(name = "fk_hand_player_player_id")	
	private Player player;
	
	public HandPlayer() {}
	
	public HandPlayer(Hand hand, Player player, String holeCards, BigDecimal totalBet, BigDecimal wonPot, String handCards) {
		
		this.hand = hand;
		
		this.player = player;
		
		this.holeCards = holeCards;
		
		this.totalBet = totalBet;
		
		this.wonPot = wonPot;
		
		this.handCards = handCards;
	}
	
	@Column(length = 5)
	private String holeCards;
	
	@Column(length = 20)
	private String handCards;

	private BigDecimal totalBet = BigDecimal.ZERO;
	
	private BigDecimal wonPot = BigDecimal.ZERO;

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getHoleCards() {
		return holeCards;
	}

	public void setHoleCards(String holeCards) {
		this.holeCards = holeCards;
	}

	public BigDecimal getTotalBet() {
		return totalBet;
	}

	public void setTotalBet(BigDecimal totalBet) {
		this.totalBet = totalBet;
	}

	public BigDecimal getWonPot() {
		return wonPot;
	}

	public void setWonPot(BigDecimal wonPot) {
		this.wonPot = wonPot;
	}
	
	public String getHandCards() {
		return handCards;
	}

	public void setHandCards(String handCards) {
		this.handCards = handCards;
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result
				+ ((hand == null || hand.getId() == null) ? 0 : hand.getId().hashCode());
		
		result = prime * result
				+ ((player == null || player.getId() == null) ? 0 : player.getId().hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if(this.hand == null || this.hand.getId() == null || this.player == null || this.player.getId() == null) return false;
		
		if (obj instanceof HandPlayer) {
			
			HandPlayer other = (HandPlayer) obj;
			
			if(other.hand == null || other.player == null ) return false;
			
			return hand.getId().equals(other.hand.getId()) && player.getId().equals(other.player.getId());
		}
		
		return false;
	}
}