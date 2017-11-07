package commons.game.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	private final static List<Card> cards = new ArrayList<Card>(52);
	
	static {
		for(Card.Rank rank : Card.Rank.values()){
			for(Card.Suit suit : Card.Suit.values()){
				cards.add(new Card(rank, suit));
			}
		}		
	}

	public static List<Card> getCards(){
		return Collections.unmodifiableList(cards);
	}	
}
