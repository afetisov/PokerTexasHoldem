package commons.game.poker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Card implements Comparable<Card>, Serializable {	
	
	private Rank rank;
	
	private Suit suit;
	
	public Card(){}
	
	public Card(Rank rank, Suit suit){
		this.rank = rank;
		this.suit = suit;
	}
	
	public static Card parse(String cardCode) throws Exception{
		if(cardCode == null || cardCode.length() != 2){
			throw new Exception("Incorrect input parameter");
		}
		
		return new Card(Rank.get(cardCode.substring(0, 1)),Suit.get(cardCode.substring(1, 2)));
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	
	
	@Override
	public String toString() {
		if(rank != null && suit != null){
			return rank.toString() + suit.toString();
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Card){
			Card card = (Card) obj;
			if(this.rank == card.rank 
					&& this.suit == card.suit){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 11;
		
		hash ^= 31 * (null == this.rank ? 0 : this.rank.hashCode());
		hash ^= 21 * (null == this.suit ? 0 : this.suit.hashCode());
		
		return hash;
	}

	public enum Rank {
		ACE {
			@Override
			public String toString() {
				return "A";
			}		
		}, 
		KING {
			@Override
			public String toString() {
				return "K";
			}		
		}, 
		QUEEN {
			@Override
			public String toString() {
				return "Q";
			}		
		}, 
		JACK {
			@Override
			public String toString() {
				return "J";
			}		
		}, 
		TEN {
			@Override
			public String toString() {
				return "T";
			}		
		}, 
		NINE {
			@Override
			public String toString() {
				return "9";
			}		
		}, 
		EIGHT {
			@Override
			public String toString() {
				return "8";
			}		
		}, 
		SEVEN {
			@Override
			public String toString() {
				return "7";
			}		
		}, 
		SIX {
			@Override
			public String toString() {
				return "6";
			}		
		}, 
		FIFE {
			@Override
			public String toString() {
				return "5";
			}		
		}, 
		FOUR {
			@Override
			public String toString() {
				return "4";
			}		
		}, 
		THREE {
			@Override
			public String toString() {
				return "3";
			}		
		}, 
		DEUCE {
			@Override
			public String toString() {
				return "2";
			}		
		};
		
		private static Map<String,Rank> mapping = new HashMap<String, Rank>();
		
		static{
			for(Rank rank : Rank.values()){
				mapping.put( rank.toString() , rank);
			}
		}
		
		public static Rank get(String rankSymbol) throws Exception{
			
			Rank rank = null;		
			
			if(rankSymbol != null && rankSymbol.length() == 1){
				rank = mapping.get(rankSymbol.toUpperCase());
				if(rank != null) return rank;
			}
			
			throw new Exception("Wrong rank symbol " + rankSymbol);
		}
	}
	
	public enum Suit {
		HEARTS {
			public String toString(){
				return "h";
			}
		}, 
		SPADES{
			public String toString(){
				return "s";
			}		
		}, 
		CLUBS{
			public String toString(){
				return "c";
			}	
		}
		,
		DIAMONDS{
			public String toString(){
				return "d";
			}
		};
		
		private static Map<String,Suit> mapping = new HashMap<String, Suit>();
		
		static{
			for(Suit suit : Suit.values()){
				mapping.put(suit.toString(),suit);
			}
		}
		
		public static Suit get(String suitSymbol) throws Exception{
			Suit suit = null;
			
			if(suitSymbol != null && suitSymbol.length() > 0){
				suit = mapping.get(suitSymbol.toLowerCase());
				if(suit != null) return suit;
			}
			
			throw new Exception("Wrong suit symbol " + suitSymbol);
		}
	}

	@Override
	public int compareTo(Card o) {
		return rank.compareTo(o.rank);
	}
}
