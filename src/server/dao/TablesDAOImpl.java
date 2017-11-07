package server.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import server.persistence.HibernateUtil;

import commons.game.TableOccupancyType;
import commons.model.TableInfo;
import commons.model.TablesFilter;
import commons.util.MiscUtils;

public class TablesDAOImpl implements TablesDAO {
	
	private final static String resetTablesQuery = "update TABLES set gameId = null, averagePot = 0, averageRake = 0, handsPerHour = 0, numberOfPlayers = 0";

	private final static String updateTableOccupancyQuery = "update TABLES set numberOfPlayers = :numberOfPlayers where id = :tableID";

	private final static String updateTableGameIDQuery = "update TABLES set gameId = :gameID where id = :tableID";
	
	@Override
	public List<TableInfo> getDeletedOrNewTables() throws Exception {

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			
			Criteria criteria = session.createCriteria(TableInfo.class);
			
			criteria = criteria.add(Restrictions.disjunction().add(Restrictions.eq("deleted", true)).add(Restrictions.isNull("gameId"))).addOrder(Order.asc("id"));
			
			return criteria.list();
			
		}finally {
			session.close();
		}
	}
	

	@Override
	public List<TableInfo> getActiveTables() throws Exception {

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			
			Criteria criteria = session.createCriteria(TableInfo.class);
			
			criteria = criteria.add(Restrictions.eq("deleted", false)).addOrder(Order.asc("id"));
			
			return criteria.list();
			
		}finally {
			session.close();
		}
	}



	@Override
	public void updateTableOccupancy(Long tableID, short occupancy) throws Exception {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
	
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			session.createSQLQuery(updateTableOccupancyQuery).setShort("numberOfPlayers", occupancy).setLong("tableID", tableID).executeUpdate();
			
			tx.commit();
			
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}

	@Override
	public void updateTableGameID(Long tableID, Long gameID) throws Exception {

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			session.createSQLQuery(updateTableGameIDQuery).setLong("gameID", gameID).setLong("tableID",  tableID).executeUpdate();
			
			tx.commit();
			
		} catch (Exception e) {
			
			if(tx != null) tx.rollback();
			
			throw e;
			
		}finally {
			session.close();
		}		
	}
	
	@Override
	public void removeTables(List<TableInfo> tables) throws Exception {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
	
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			for(TableInfo t : tables) {
				session.delete(t);
			}
			
			tx.commit();
		
		} catch (Exception e) {
		
			tx.rollback();
			throw e;
		
		}finally {
			session.close();
		}
	}
	
	@Override
	public void resetTablesInfo() throws Exception {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		
		try {
		
			tx = session.beginTransaction();
			
			session.createSQLQuery(resetTablesQuery).executeUpdate();
			
			tx.commit();
			
		} catch (Exception e) {
			
			if(tx != null) tx.rollback();
			
			throw e;
		
		}finally {
			session.close();
		}
	}


	@Override
	public List<TableInfo> getTables(TablesFilter filter, boolean game) throws Exception {

		List<TableInfo> tables = null;
		
		org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(TableInfo.class);
		
		criteria = applyRestrictioins(criteria, filter, game);
		
		try {
			
			tables = criteria.list();
		
		}
		finally {
			
			session.close();
		}
		
		return tables;

	}

	
	@Override
	public List<BigDecimal> getBlinds(TablesFilter filter, boolean game) throws Exception {

		List<BigDecimal> blinds = null;

		org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria query = session.createCriteria(TableInfo.class);
		
		query = applyRestrictioins(query, filter, game);
		
		query.setProjection(Projections.distinct(Property.forName("bigBlind")));

		try {
			
			blinds = query.list();
		
		}finally {
		
			session.close();
		
		}
		
		return blinds;

	}


	private Criteria applyRestrictioins(Criteria criteria, TablesFilter filter, boolean game) {
		
		criteria = criteria.add(Restrictions.eq("deleted", false));
		
		if(game) {
			criteria = criteria.add(Restrictions.isNotNull("gameId"));
		}
		
		if(filter == null) return criteria;
		
		Property gameType = Property.forName("gameType");
		
		if(filter.getGameType() != null) {
			
			criteria = criteria.add(gameType.eq(filter.getGameType()));
		
		}
		if(filter.getOccupancy() > 0) {
			
			Disjunction occupancyTypes = Restrictions.disjunction();
			
			for(TableOccupancyType type : TableOccupancyType.values()) {
			
				if(TablesFilter.isSet(type, filter.getOccupancy())) {
				
					switch(type) {
					case EMPTY:
						occupancyTypes.add(Restrictions.eq("statistics.numberOfPlayers", MiscUtils.toShort(0)));
						break;
					case FULL:
						occupancyTypes.add(Restrictions.eq("statistics.numberOfPlayers", MiscUtils.toShort(TableOccupancyType.MAX_PLAYERS)));
						break;
					default:
						occupancyTypes.add(Restrictions.between("statistics.numberOfPlayers", MiscUtils.toShort(1), MiscUtils.toShort(TableOccupancyType.MAX_PLAYERS - 1)));
						break;
					}
				}
			}
			
			criteria = criteria.add(occupancyTypes);
		}
		return criteria;
	}
}
