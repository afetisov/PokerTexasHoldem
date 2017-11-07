package server.game;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import commons.game.GameContext;
import commons.game.poker.Card;
import commons.game.poker.Deck;
import commons.game.poker.Player;

public class TexasHoldemGameHelper {
	
	public static void initCardPack(GameContext gameContext) {
		
		gameContext.cardPack = new Stack<Card>();
		
		gameContext.cardPack.addAll(Deck.getCards());
		
		Collections.shuffle(gameContext.cardPack);
	}
	
	public static void dealHoleCards(GameContext gameContext) {
		
		//burn top card
		gameContext.cardPack.pop();
		
		List<Player> players = new LinkedList<Player>();
		
		int firstPlayer;
		
		int currentPlayer = firstPlayer = gameContext.headsUp ? gameContext.bigBlindPlayer : gameContext.smallBlindPlayer; 
		
		boolean firstTime = true;
		
		while(true) {
			
			if(currentPlayer == firstPlayer && !firstTime) break;
			
			firstTime = false;
			
			Player p = gameContext.handPlayers[currentPlayer];
			
			if(p == null || gameContext.activePlayers[currentPlayer] == null || p != gameContext.activePlayers[currentPlayer] ) {
				
				currentPlayer = (currentPlayer + 1) % gameContext.handPlayers.length;
				
				continue;
			}
			
			Set<Card> cards = new HashSet<Card>(2);
			
			cards.add(gameContext.cardPack.pop());

			p.setHoleCards(cards);
			
			players.add(p);
			
			currentPlayer = (currentPlayer + 1) % gameContext.handPlayers.length;
		}
		
		for(Player p : players) {
			p.getHoleCards().add(gameContext.cardPack.pop());
		}
	}
}
