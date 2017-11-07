package client.game.gui.components;

import javax.swing.JButton;

public class MenuButton extends JButton {

	private MenuActionType menuActionType;
	
	public MenuButton(MenuActionType type) {
		super();
		menuActionType = type;
	}
	
	public MenuButton(String text, MenuActionType type) {
		super(text);
		menuActionType = type;
	}

	public MenuActionType getActionType() {
		return menuActionType;
	}

	public void setMenuActionType(MenuActionType actionType) {
		this.menuActionType = actionType;
	}
}
