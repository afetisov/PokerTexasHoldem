package commons.game;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import commons.game.events.GameEvent;
import commons.game.poker.ActionType;
import commons.game.poker.Card;
import commons.game.poker.HandRound;
import commons.game.poker.PlayerHand;

public interface Game extends Remote, Serializable {
		
	boolean takeSeat(String sessionID, int seatNumber, BigDecimal buyIn) throws RemoteException;
	
	void sitOut(String sessionID) throws RemoteException;
	
	GameContext getGameContext() throws RemoteException;
	
	void goLobby(String sessionID) throws RemoteException;
	
	Set<Card> getHoleCards(String sessionID) throws RemoteException;
	
	void action(String sessionID, ActionType actionType, BigDecimal amount) throws RemoteException;

	PlayerHand getHand(String sessionID) throws RemoteException;
	
	GameEvent getGameEvent(String sessionID) throws RemoteException;
}
