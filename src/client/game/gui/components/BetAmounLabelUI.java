package client.game.gui.components;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

import client.game.misc.ResourceManager;

public class BetAmounLabelUI extends BasicLabelUI {

	private ImageIcon beginLabelImg;
	
	private ImageIcon tileImg;

	@Override
	protected void installComponents(JLabel c) {
		
		//beginLabelImg = ResourceManager.getImage(ResourceManager.BET_AMOUNT_PLATE_LEFT);
		
		//tileImg = ResourceManager.getImage(ResourceManager.BET_AMOUNT_PLATE_TILE);

		super.installComponents(c);
	}

	@Override
	protected void paintDisabledText(JLabel label, Graphics g, String text,
			int x, int y) {

		beginLabelImg.paintIcon(label, g, 0, 0);
		
		int tiles = (label.getWidth() - beginLabelImg.getIconWidth()) / tileImg.getIconWidth();
		for(int i = 0; i <= tiles; i++) {
			tileImg.paintIcon(label, g, beginLabelImg.getIconWidth() + i * tileImg.getIconWidth(), 0);
		}
		
		super.paintEnabledText(label, g, text, x, y);		
	}

	@Override
	protected void paintEnabledText(JLabel arg0, Graphics arg1, String arg2,
			int arg3, int arg4) {
		this.paintDisabledText(arg0, arg1, arg2, arg3, arg4);
	}
}
