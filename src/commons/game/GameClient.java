package commons.game;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import commons.game.events.GameEvent;

public interface GameClient extends Remote, Serializable {
	
	void setGame(Game game) throws RemoteException;
	
	String getSessionID() throws RemoteException;

	void postEvent(GameEvent event) throws RemoteException; 
}
