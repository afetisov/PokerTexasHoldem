package client.game.gui.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import commons.game.poker.ActionType;

public class ButtonsFactory {
	
	public static ActionButton createButton(ActionType type, ImageIcon defaultIcon, ImageIcon pressedIcon, ImageIcon rolloverIcon, ImageIcon disabledIcon) {

		ActionButton button = new ActionButton(type);
		
		button.setContentAreaFilled(false);
		
		button.setBorderPainted(false);

		button.setIcon(defaultIcon);
		
		button.setPressedIcon(pressedIcon);
		
		button.setDisabledIcon(disabledIcon);
		
		button.setRolloverIcon(rolloverIcon);
		
		return button;
	}

	public static JButton createMenuButton(MenuActionType type, ImageIcon defaultIcon, ImageIcon pressedIcon, ImageIcon rolloverIcon, ImageIcon disabledIcon) {

		JButton button = null;
		
		if (type == MenuActionType.SEAT_DOWN) {
			button = new SeatButton();			
		} else {
			button = new MenuButton(type);
		}
		
		button.setContentAreaFilled(false);
		
		button.setBorderPainted(false);

		button.setIcon(defaultIcon);
		
		button.setPressedIcon(pressedIcon);
		
		button.setDisabledIcon(disabledIcon);
		
		button.setRolloverIcon(rolloverIcon);
		
		return button;
	}
	
	public static ToggleButton createToggleButton(ToggleButtonType type,ImageIcon defaultIcon, ImageIcon selectedIcon, ImageIcon disabledIcon, ImageIcon disabledSelectedIcon, String text) {

		ToggleButton button = new ToggleButton(text, type);
		
		button.setIcon(defaultIcon);
		
		button.setSelectedIcon(selectedIcon);
		
		button.setDisabledIcon(disabledIcon);
		
		button.setDisabledSelectedIcon(disabledSelectedIcon);
		
		return button;
	}
}
