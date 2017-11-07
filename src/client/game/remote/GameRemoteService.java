package client.game.remote;

import commons.game.GameServer;
import commons.log.Logger;
import commons.log.LoggingLevel;

import client.service.ServiceManager;

public class GameRemoteService {
	
	private static final String gameServer = "GameServer";
	
	public static GameServer getGameServer() {
		
		try {
			
			return (GameServer) ServiceManager.getRemoteService(gameServer);
		
		} catch (Exception exc) {
			Logger.write(LoggingLevel.ERROR, "Failed to locate game server", exc);
		}
		return null;
	}

}
