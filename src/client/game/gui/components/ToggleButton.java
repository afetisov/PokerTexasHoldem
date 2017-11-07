package client.game.gui.components;

import javax.swing.JCheckBox;

public class ToggleButton extends JCheckBox {
	
	private ToggleButtonType type;
	
	public ToggleButton(String label, ToggleButtonType type) {
		super(label);
		this.type = type;
		setOpaque(false);
		setFocusable(false);
	}

	public ToggleButtonType getType() {
		return type;
	}

	public void setType(ToggleButtonType type) {
		this.type = type;
	}
}
