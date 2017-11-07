package server.game;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import md5.MD5Crypt;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import server.dao.DatabaseService;
import server.persistence.HibernateUtil;

import commons.game.Game;
import commons.game.GameClient;
import commons.game.GameServer;
import commons.game.TableOccupancyType;
import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.Player;
import commons.model.Session;
import commons.model.TableInfo;
import commons.model.TablesFilter;
import commons.util.MiscUtils;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {

	public GameServerImpl() throws RemoteException {
		super();
	}

	@Override
	public Session loginPlayer(String userID, String password)
			throws RemoteException {
		
		return DatabaseService.getPlayersDAO().loginPlayer(userID, password);
	}

	@Override
	public void logoutPlayer(String sessionID) throws RemoteException {
		
		DatabaseService.getPlayersDAO().logoutPlayer(sessionID);
	}

	@Override
	public boolean changePassword(String sessionID, String oldPassword,
			String newPassword) throws RemoteException {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Game joinGame(Long tableID, String sessionID)
			throws RemoteException {

		GameController gc = GameServerEnvironment.games.get(tableID);
		
		try {
			
			if(gc != null) {
				
				gc.joinGame(sessionID);
				
				return gc.getGame();
			}
		}catch(Exception e) {
			Logger.write(LoggingLevel.ERROR, "Failed to join a game.", e);
		}
		
		return null;
	}

	@Override
	public List<TableInfo> getTables(TablesFilter filter)
			throws RemoteException {
				
		try {
			
			return DatabaseService.getTablesDAO().getTables(filter, true);
		
		} catch (Exception exc) {
			Logger.write(LoggingLevel.ERROR, "Failed to retrieve a list of tables", exc);
		}
		
		return Collections.emptyList();
	}

	@Override
	public List<BigDecimal> getBlinds(TablesFilter filter)
			throws RemoteException {

		try {
			
			return DatabaseService.getTablesDAO().getBlinds(filter, true);
		
		} catch (Exception exc) {
			
			Logger.write(LoggingLevel.ERROR, "Failed to retrieve a list of blinds", exc);
		
		}
		
		return Collections.emptyList();
	}


	@Override
	public Session getSession(String sessionID) throws RemoteException {

		return DatabaseService.getPlayersDAO().findSession(sessionID);
	}
}
