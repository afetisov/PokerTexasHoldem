package client.game.misc;

import java.io.Serializable;

import commons.game.poker.ActionType;

public class GuiEvent implements Serializable {
	
	private ActionType actionType;
	
	private Object value;
	
	public GuiEvent() {}
	
	public GuiEvent(ActionType type, Object value) {
		this.actionType = type;
		this.value = value;
	}
	
	public GuiEvent(ActionType type) {
		this(type,null);
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
