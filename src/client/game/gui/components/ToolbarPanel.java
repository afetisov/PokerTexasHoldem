package client.game.gui.components;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import client.game.misc.ResourceManager;

public class ToolbarPanel extends JPanel {

	private ImageIcon panelTile;
	
	public ToolbarPanel() {
		panelTile = ResourceManager.getImage(ResourceManager.MENU_BACKGROUND);
	}
	
	public int getPanelHeight() {
		return panelTile.getIconHeight();
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Dimension dim = getPreferredSize();
		
		int tilesCount = dim.width / panelTile.getIconWidth();
		
		for(int i = 0; i <= tilesCount; i++) {
			g.drawImage(panelTile.getImage(), 0 + i * panelTile.getIconWidth(), 0, null);
		}
	}

}
