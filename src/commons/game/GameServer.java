package commons.game;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import commons.model.Session;
import commons.model.TableInfo;
import commons.model.TablesFilter;

public interface GameServer extends Remote {
	
	Session loginPlayer(String userID, String password) throws RemoteException;

	void logoutPlayer(String sessionID) throws RemoteException;
	
	boolean changePassword(String sessionID, String oldPassword, String newPassword) throws RemoteException;
	
	Game joinGame(Long tableID, String sessionID) throws RemoteException;
	
	List<TableInfo> getTables(TablesFilter filter) throws RemoteException;
	
	List<BigDecimal> getBlinds(TablesFilter filter) throws RemoteException;
	
	Session getSession(String session) throws RemoteException;
}
