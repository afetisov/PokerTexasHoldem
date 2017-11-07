package commons.game.poker.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import commons.game.poker.Card;
import commons.game.poker.HandRank;
import commons.game.poker.PlayerHand;

public class SevenCardHandEvaluator {

	private Map<Card.Rank, Set<Card>> rankMap = new EnumMap<Card.Rank, Set<Card>>(Card.Rank.class);
	
	private Map<Card.Suit, Set<Card>> suitMap = new EnumMap<Card.Suit, Set<Card>>(Card.Suit.class);
	
	private Map<HandRank, Set<Card>> handMap = new EnumMap<HandRank, Set<Card>>(HandRank.class);
	
	public PlayerHand evaluate(Collection<Card> cards){
	
		if(cards == null || cards.isEmpty()) return null;
		
		rankMap.clear();
		suitMap.clear();
		handMap.clear();
			
		classifyCards(cards);
		
		HandRank hand = analyzeClassifiedData();	
		
		if(hand != null){
			return new PlayerHand(hand,new ArrayList<Card>(handMap.get(hand)));
		}
		
		return getPlayerHand(hand);
	}
	
	private void classifyCards(Collection<Card> cards){
		
		for(Card card : cards){
		
			Set<Card> rankList = rankMap.get(card.getRank());
			
			Set<Card> suitList = suitMap.get(card.getSuit());
		
			if(rankList == null){
			
				rankList = new LinkedHashSet<Card>();
				
				rankMap.put(card.getRank(), rankList);
			}
			
			if(suitList == null){
				
				suitList = new LinkedHashSet<Card>();
				
				suitMap.put(card.getSuit(), suitList);
			}
			
			rankList.add(card);
			
			suitList.add(card);
		}
	}

	private HandRank analyzeClassifiedData(){
		
		for(Card.Rank rank : rankMap.keySet()){	
		
			HandRank hand = assembleHand(rank);
			
			if(hand != null){
				return hand;
			}
		}
		return null;
	}

	
	private HandRank assembleHand(Card.Rank rank){
		
		Set<Card> cards = rankMap.get(rank);
		
		if(cards == null || cards.isEmpty()) return null;
		
		if(cards.size() == 4){
			
			return updateHand(HandRank.FourOfKind, cards);
		
		}else {
		
			HandRank hand = updateHand(HandRank.FourOfKind, cards);
			
			if(hand != null) return hand;
			
			hand = updateHand(HandRank.FullHouse, cards);
			
			if(hand != null) return hand;
			
			updateHand(HandRank.Flush, cards);
			
			updateHand(HandRank.Straight, cards);
			
			updateHand(HandRank.ThreeOfKind, cards);
			
			updateHand(HandRank.TwoPairs, cards);
			
			updateHand(HandRank.Pair, cards);
			
			updateHand(HandRank.HighCard, cards);
		}
		
		return null;
	}
	
	private Set<Card> getHandCards(HandRank hand){
		
		if(hand == null){
			return Collections.emptySet();
		}
		
		Set<Card> cards = handMap.get(hand);
		
		if(cards == null){
			cards = new LinkedHashSet<Card>();
			handMap.put(hand, cards);
		}
		
		return cards;
	}
	
	private HandRank updateHand(HandRank hand, Set<Card> cards){
	
		if(hand == null || cards == null || cards.isEmpty()) return null;
		
		Set<Card> handCards = getHandCards(hand);
		
		switch(hand){
		case FourOfKind:
			if(cards.size() == 4 && handCards.isEmpty()){
				handCards.addAll(cards);
				Set<Card> list = getHandCards(HandRank.HighCard);
				list.removeAll(handCards);
				if(!list.isEmpty()){
					handCards.add(list.iterator().next());
				}
			}else if(handCards.size() == 4){
				handCards.add(cards.iterator().next());				
			}
			if(handCards.size() == 5){
				return hand;
			}
			break;
		case FullHouse:
			if(handCards.isEmpty() && (cards.size() == 2 || cards.size() == 3)){
				handCards.addAll(cards);
			}else{
				if(handCards.size() == 2 && cards.size() >= 3){
					int i = 0;
					for(Iterator<Card> it = cards.iterator(); it.hasNext() && i < 3; i++){
						handCards.add(it.next());
					}
				}else if(handCards.size() == 3 && cards.size() >= 2){
					int i = 0;
					for(Iterator<Card> it = cards.iterator(); it.hasNext() && i < 2; i++){
						handCards.add(it.next());
					}
				}
			}
			if(handCards.size() == 5){
				return hand;
			}
			break;
		case Flush:
		case Straight:
				Card card = cards.iterator().next();
				for(Card c : cards){
					Set<Card> suitedCards = suitMap.get(c.getSuit());
					if(suitedCards != null && suitedCards.size() >= 5
							&& suitedCards.contains(c)){
						card = c;
						if(hand == HandRank.Flush){
							handCards.add(card);
						}
						break;
					}
				}
				if(hand == HandRank.Straight){
					if(handCards.isEmpty()){
						handCards.add(card);
					}else{
						Card c = handCards.iterator().next();
						if(c.getRank().ordinal() + handCards.size() == card.getRank().ordinal()){
							handCards.add(card);
						} else if (handCards.size() < 5){
							handCards.clear();
							handCards.add(card);
						}
					}
				}
			break;
		case ThreeOfKind:
			if(cards.size() == 3 && handCards.isEmpty()){
				handCards.addAll(cards);
			}
			break;
		case TwoPairs:
		case Pair:
			if(cards.size() == 2){
				if(handCards.isEmpty()
						|| (hand == HandRank.TwoPairs && handCards.size() == 2)){
					handCards.addAll(cards);
				}				
			}
			break;
		default:
			handCards.addAll(cards);
		}
		return null;
	}
	
	private PlayerHand getPlayerHand(HandRank hand){		

		Set<Card> rankedCards = getHandCards(HandRank.Straight);
		
		Set<Card> suitedCards = getHandCards(HandRank.Flush);
		
		Set<Card> highCards = getHandCards(HandRank.HighCard);
		
		if(rankedCards.size() == 4 
				&& rankedCards.iterator().next().getRank() == Card.Rank.FIFE){

			Card ace = null;
			
			for(Card card : highCards){
				if(card.getRank() != Card.Rank.ACE){
					break;
				}
				ace = card;
				if(suitedCards.contains(ace)){
					break;
				}
			}
			if(ace != null){
				rankedCards.add(ace);
			}
		}
		
		if(rankedCards.size() >= 5 && suitedCards.size() >= 5){
	
			Set<Card> list = new LinkedHashSet<Card>(rankedCards);
			
			list.retainAll(suitedCards);
			
			if(list.size() >= 5){
			
				if(list.iterator().next().getRank() == Card.Rank.ACE){
				
					return new PlayerHand(HandRank.RoyalFlush, new ArrayList<Card>(list));
				
				}else{
				
					return new PlayerHand(HandRank.StraightFlush, new ArrayList<Card>(list));
				
				}
			}
		}
		
		rankedCards = getHandCards(HandRank.FourOfKind);
		
		if(!rankedCards.isEmpty()){
			if(rankedCards.size() == 4){
				Set<Card> list = getHandCards(HandRank.HighCard);
				list.removeAll(rankedCards);
				if(!list.isEmpty()){
					rankedCards.add(list.iterator().next());
				}
			}
			if(rankedCards.size() == 5){
				return new PlayerHand(HandRank.FourOfKind, new ArrayList<Card>(rankedCards));
			}
		}
		
		rankedCards = getHandCards(HandRank.FullHouse);
		
		if(rankedCards.size() == 5){
			return new PlayerHand(HandRank.FullHouse, new ArrayList<Card>(rankedCards));
		}
		
		suitedCards = getHandCards(HandRank.Flush);
		
		if(suitedCards.size() >= 5){
			return new PlayerHand(HandRank.Flush, new ArrayList<Card>(rankedCards));
		}
		
		rankedCards = getHandCards(HandRank.Straight);
		
		if(rankedCards.size() >= 5){
			return new PlayerHand(HandRank.Straight, new ArrayList<Card>(rankedCards));
		}
		
		rankedCards = getHandCards(HandRank.ThreeOfKind);
		
		if(rankedCards.size() == 3){
			
			Set<Card> list = getHandCards(HandRank.HighCard);
			list.removeAll(rankedCards);
			Iterator<Card> it = list.iterator();
			rankedCards.add(it.next());
			rankedCards.add(it.next());
			
			return new PlayerHand(HandRank.ThreeOfKind, new ArrayList<Card>(rankedCards));
		}
		
		rankedCards = getHandCards(HandRank.TwoPairs);
		
		if(rankedCards.size() == 4){			
			
			Set<Card> list = getHandCards(HandRank.HighCard);
			list.removeAll(rankedCards);
			rankedCards.add(list.iterator().next());
			
			return new PlayerHand(HandRank.TwoPairs, new ArrayList<Card>(rankedCards));
		}
		
		rankedCards = getHandCards(HandRank.Pair);
		
		if(rankedCards.size() == 2){			
			Set<Card> list = getHandCards(HandRank.HighCard);
			list.removeAll(rankedCards);
			Iterator<Card> it = list.iterator();
			rankedCards.add(it.next());
			rankedCards.add(it.next());
			rankedCards.add(it.next());
		
			return new PlayerHand(HandRank.Pair, new ArrayList<Card>(rankedCards));
		}
		
		rankedCards = getHandCards(HandRank.HighCard);
		
		if(rankedCards.size() >= 5){
			return new PlayerHand(HandRank.HighCard, new ArrayList<Card>(rankedCards));
		}
		
		return null;
	}
}
