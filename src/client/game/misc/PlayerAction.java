package client.game.misc;

import java.io.Serializable;
import java.math.BigDecimal;

import commons.game.poker.ActionType;

public class PlayerAction implements Serializable {
		
	private ActionType type;
	
	private BigDecimal value;

	public PlayerAction() {}
	
	public PlayerAction(ActionType type, BigDecimal value) {
		this.type = type;
		this.value = value;
	}
	
	public PlayerAction(ActionType type) {
		this(type, null);
	}
	
	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
