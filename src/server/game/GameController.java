package server.game;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.rowset.spi.SyncResolver;

import commons.game.Game;
import commons.game.GameClient;
import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.TableInfo;

public class GameController extends Thread {
	
	private GameImpl game;
	
	private Set<String> clients = new HashSet<String>();
	
	private TableInfo tableInfo;
	
	public GameController(TableInfo table) {
		tableInfo = table;
	}

	@Override
	public void run() {
		try {
			
			game = new GameImpl(tableInfo);
			
			game.start();
			
		} catch (Exception e) {
			Logger.write(LoggingLevel.FATAL, String.format("Fatat error occured in the game controller for table # %1$d. The game has been stopped.", tableInfo.getId()), e);
		}
	}

	public void stopGame(boolean interrupt) {
		
		game.setStopGameRequested(true);
		
		if(interrupt) {
			interrupt();
		}
	}
	
	public void joinGame(String sessionID) throws Exception {
		game.addClient(sessionID);
	}
	
	public Game getGame() {
		return game;
	}
}
