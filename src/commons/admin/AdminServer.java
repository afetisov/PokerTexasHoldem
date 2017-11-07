package commons.admin;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import commons.model.Player;
import commons.model.PlayersFilter;
import commons.model.TableInfo;
import commons.model.TablesFilter;

public interface AdminServer extends Remote {
	
	List<TableInfo> getTables(TablesFilter filter) throws RemoteException;
	
	List<BigDecimal> getBlinds(TablesFilter filter) throws RemoteException;
	
	Long createTable(TableInfo tableInfo) throws RemoteException;
	
	void deleteTables(List<TableInfo> list) throws RemoteException;
	
	List<Player> getPlayers(PlayersFilter filter) throws RemoteException;
	
	Long savePlayer(Player player) throws RemoteException;
	
	void deletePlayers(List<Player> list) throws RemoteException;
	
	void updateBalance(Long playerID, BigDecimal amount) throws RemoteException;

	boolean loginAdmin(String password) throws RemoteException;
	
	boolean changeAdminPassword(String newPassword, String oldPassword) throws RemoteException;

}
