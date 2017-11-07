package server.dao;

import java.rmi.RemoteException;

import commons.model.Session;

public interface PlayersDAO {

	Session loginPlayer(String userID, String password) throws RemoteException;
	
	void logoutPlayer(String sessionID) throws RemoteException;
	
	Session findSession(String sessionID) throws RemoteException;
	
	
	
	
}
