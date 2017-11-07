package client.game.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingQueue;

import client.game.GameClientApplication;

import commons.game.Game;
import commons.game.GameClient;
import commons.game.events.GameEvent;

public class GameClientImpl extends UnicastRemoteObject implements GameClient {

	private Game game;
	
	private String sessionID;
	
	private LinkedBlockingQueue<GameEvent> incomingEventQueue;
	
	public GameClientImpl() throws RemoteException {
		super();
	}
	
	public void setIncomingQueue(LinkedBlockingQueue<GameEvent> queue) {
		this.incomingEventQueue = queue;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public Game getGame() {
		return game;
	}
	
	@Override
	public void setGame(Game game) throws RemoteException {
		this.game = game;
	}

	@Override
	public String getSessionID() throws RemoteException {
		return sessionID;
	}
	
	@Override
	public void postEvent(GameEvent event) throws RemoteException {
		try {
			incomingEventQueue.add(event);
		}catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}
