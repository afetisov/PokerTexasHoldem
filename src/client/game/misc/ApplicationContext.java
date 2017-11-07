package client.game.misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

	private static Map<String, Object> params = new ConcurrentHashMap<String, Object>();
	
	public static void set(String param, Object value) {
		params.put(param, value);
	}

	public static Object get(String param) {
		return params.get(param);
	}
	
	public static void remove(String param) {
		params.remove(param);
	}
	
	public static void clear() {
		params.clear();
	}
}
