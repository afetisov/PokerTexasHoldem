package server.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import server.persistence.HibernateUtil;

import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.Hand;

public class GameDAOImpl implements GameDAO {

	@Override
	public void saveHandHistory(Hand hand) throws Exception {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = session.beginTransaction();
		
		try {
			
			session.saveOrUpdate(hand);
			
			tx.commit();
		
		}catch(Exception exc) {
			
			tx.rollback();
			
			Logger.write(LoggingLevel.ERROR, "Failed to save hand history.", exc);
		}
	
		session.close();
	}
}
