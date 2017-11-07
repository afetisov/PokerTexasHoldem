package server.game;

import java.util.LinkedList;
import java.util.List;

import server.dao.DatabaseService;

import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.TableInfo;

public class TablesMaintenanceTask implements Runnable {

	@Override
	public void run() {
		try {
			
			List<TableInfo> tables = DatabaseService.getTablesDAO().getDeletedOrNewTables();
			
			List<TableInfo> tablesToRemove = new LinkedList<TableInfo>();
			
			for(TableInfo table : tables) {
				
				if(table.isDeleted()) {
					
					GameController gc = GameServerEnvironment.games.get(table.getId());
					
					if(gc == null) {
						
						tablesToRemove.add(table);
						
						continue;
					}
					
					if (!gc.isAlive()) {
						
						tablesToRemove.add(table);
						
						GameServerEnvironment.games.remove(table.getId());
						
					} else {
						gc.stopGame(false);
					}
					
				} else if(table.getGameId() == null) {

					GameController game = new GameController(table);
					
					DatabaseService.getTablesDAO().updateTableGameID(table.getId(), game.getId());
					
					GameServerEnvironment.games.put(table.getId(), game);
					
					game.start();
				}
			}
			
			if(tablesToRemove.size() > 0) {
				DatabaseService.getTablesDAO().removeTables(tablesToRemove);
			}
			
		}catch(Exception exc) {
			Logger.write(LoggingLevel.ERROR, "Maintenance task has failed.", exc);
		}
	}
}
