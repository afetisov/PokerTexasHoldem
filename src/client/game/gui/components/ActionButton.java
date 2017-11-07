package client.game.gui.components;

import javax.swing.JButton;

import commons.game.poker.ActionType;

@SuppressWarnings("serial")
public class ActionButton extends JButton {
	
	private ActionType actionType;
	
	public ActionButton(ActionType type) {
		super();
		actionType = type;
	}
	
	public ActionButton(String text, ActionType type) {
		super(text);
		actionType = type;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
}
