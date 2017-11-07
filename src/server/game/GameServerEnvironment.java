package server.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameServerEnvironment {
	
	public final static Map<Long, GameController> games = new ConcurrentHashMap<Long, GameController>();
	
}
