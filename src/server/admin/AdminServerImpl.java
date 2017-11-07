package server.admin;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import md5.MD5Crypt;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import server.dao.DatabaseService;
import server.persistence.HibernateUtil;

import commons.admin.AdminServer;
import commons.game.TableOccupancyType;
import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.BalanceRecord;
import commons.model.Player;
import commons.model.PlayersFilter;
import commons.model.TableInfo;
import commons.model.TablesFilter;
import commons.util.MiscUtils;

public class AdminServerImpl extends UnicastRemoteObject implements
		AdminServer {

	public AdminServerImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean loginAdmin(String password) throws RemoteException {
		
		if(password == null || password.isEmpty()) return false;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
		
			List result = session.createSQLQuery("select value from PROPERTIES where id = 1").list();
		
			if(result.size() == 1) {
		
				String adminPassword = (String) result.get(0);
			
				if(MD5Crypt.isEquivalent(password, adminPassword)) return true;
			}
			
		}catch(Exception exc) {
			
			Logger.write(LoggingLevel.ERROR, "Failed to login as admin", exc);
		
		}finally {
			session.close();
		}
		
		return false;
	}

	@Override
	public boolean changeAdminPassword(String newPassword, String oldPassword) throws RemoteException{

		if(newPassword == null || newPassword.isEmpty() || oldPassword == null || oldPassword.isEmpty()) return false;
		
		boolean accepted = loginAdmin(oldPassword);
		
		if(accepted) {
		
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			Transaction tx = session.beginTransaction();
			
			try {
			
				Query query = session.createSQLQuery("update PROPERTIES set value = :newPswd where id = 1").setString("newPswd", MD5Crypt.apacheCrypt(newPassword));
				
				query.executeUpdate();
				
				tx.commit();
				
				return true;
			
			}catch(Exception exc) {
			
				tx.rollback();
				
				Logger.write(LoggingLevel.ERROR, "Failed to change an admin password", exc);
				
				throw new RemoteException();
			
			}finally {
				session.close();
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> getPlayers(PlayersFilter filter) throws RemoteException {
		
		List<Player> list = null;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Player.class);
		
		criteria = applyRestrictioins(criteria, filter);

		try {
		
			list = criteria.list();
		
		}finally {
			
			session.close();
		
		}
		
		return list;
	}

	@Override
	public Long savePlayer(Player player) throws RemoteException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
		
			session.saveOrUpdate(player);
			
			tx.commit();
			
			return player.getId();
		
		}finally {
			
			session.close();
		
		}
	}

	
	private Criteria applyRestrictioins(Criteria criteria, PlayersFilter filter) {
		
		if(!filter.isBlockedUsers()) {
		
			criteria = criteria.add(Property.forName("blocked").eq(false));
		
		}
		
		return criteria;
	}

	@Override
	public void deletePlayers(List<Player> list) throws RemoteException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
		
			for(Player p : list) {
				session.delete(p);
			}
			
			tx.commit();
		
		}finally {
			session.close();
		}
	}

	@Override
	public void updateBalance(Long playerID, BigDecimal amount)
			throws RemoteException {
		
		if(playerID == null || amount == null || amount == BigDecimal.ZERO) return;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Player p = (Player) session.load(Player.class, playerID);
		
		if(p != null) {
		
			Transaction tx = session.beginTransaction();
			
			try {
			
				p.setBalance(p.getBalance().add(amount));
				
				BalanceRecord rec = new BalanceRecord();
				
				rec.setAmount(amount);
				
				rec.setPlayer(p);
				
				session.save(rec);
				
				tx.commit();
			
			}catch(Exception e) {
				
				if(tx != null) tx.rollback();
				
				e.printStackTrace();
			}
			finally {
				session.close();
			}
		}
	}

	@Override
	public List<TableInfo> getTables(TablesFilter filter)
			throws RemoteException {
		
		try {
			
			return DatabaseService.getTablesDAO().getTables(filter, false);
		
		} catch (Exception exc) {
	
			Logger.write(LoggingLevel.ERROR, "Failed to retrieve a list of tables", exc);
		
		}
		
		return Collections.emptyList();

	}

	@Override
	public Long createTable(TableInfo tableInfo) throws RemoteException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
		
			Long id = (Long) session.save(tableInfo);
			
			tx.commit();
			
			return id;
		
		}finally {
			session.close();
		}
	}

	@Override
	public void deleteTables(List<TableInfo> list) throws RemoteException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
		
			for(TableInfo obj : list) {
			
				obj.setDeleted(true);
				
				obj.setLastModified(new Date());
				
				session.update(obj);
			}
			
			tx.commit();
		
		}finally {
			session.close();
		}
	}

	@Override
	public List<BigDecimal> getBlinds(TablesFilter filter)
			throws RemoteException {
		
		try {
			
			return DatabaseService.getTablesDAO().getBlinds(filter, false);
		
		} catch (Exception exc) {
			
			Logger.write(LoggingLevel.ERROR, "Failed to retrieve a list of blinds", exc);
		
		}
		
		return Collections.emptyList();

	}
}
