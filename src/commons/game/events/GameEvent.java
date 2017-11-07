package commons.game.events;

import java.io.Serializable;

public class GameEvent implements Serializable {

	private EventType type;
	
	private Object value;

	public GameEvent() {}
	
	public GameEvent(EventType type) {
		this(type, null);
	}
	
	public GameEvent(EventType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
