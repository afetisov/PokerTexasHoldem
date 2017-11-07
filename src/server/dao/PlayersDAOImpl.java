package server.dao;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import md5.MD5Crypt;

import org.hibernate.Transaction;

import server.persistence.HibernateUtil;

import commons.model.Player;
import commons.model.Session;

public class PlayersDAOImpl implements PlayersDAO {

	@Override
	public Session loginPlayer(String userID, String password)
			throws RemoteException {
		
		if(userID == null || userID.isEmpty()
				|| password == null || password.isEmpty())
			return null;
		
		org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Player player = (Player) session.get(Player.class, Long.parseLong(userID));
			
			if(player == null) return null;

			if(!MD5Crypt.isEquivalent(password, player.getPassword())) {
				return null;
			}
			
			player.setLastLogin(new Date());
			
			session.flush();
			
			List list = session.createQuery("from commons.model.Session as s where s.player = :playerID").setLong("playerID", player.getId()).list();
			
			Session result = null;
			
			if(list.size() > 0) {
				result = (Session) list.get(0); 
			}else {
				result = new Session();
				result.setPlayer(player);
				session.save(result);
			}
			
			tx.commit();
			
			return result;

		}catch(Exception exc) {
			tx.rollback();
			exc.printStackTrace();
		}finally {
			session.close();
		}
		
		return null;
	}


	@Override
	public void logoutPlayer(String sessionID) throws RemoteException {
		
		org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
		
			Session sess = (Session) session.get(Session.class, sessionID);
			
			if(sess != null) {
				session.delete(sess);
				tx.commit();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}finally {
			session.close();
		}
	}

	@Override
	public Session findSession(String sessionID) throws RemoteException {

		org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
		
		Session s = null;
		
		try {
			s = (Session) session.get(Session.class, sessionID);
		}finally {
			session.close();
		}
		
		return s;
	}
	
	

}
