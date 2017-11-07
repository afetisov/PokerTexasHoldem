package server.dao;

public class DatabaseService {
	
	private final static TablesDAO tablesDAO = new TablesDAOImpl();
	
	private final static GameDAO gameDAO = new GameDAOImpl();
	
	private final static PlayersDAO playersDAO = new PlayersDAOImpl();
	
	private DatabaseService() {}
	
	public static TablesDAO getTablesDAO() {
		return tablesDAO;
	}
	
	public static GameDAO getGameDAO() {
		return gameDAO;
	}
	
	public static PlayersDAO getPlayersDAO() {
		return playersDAO;
	}
}
