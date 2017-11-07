package client.game.gui.components;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import client.game.misc.ResourceManager;

public class BetAmountSliderUI extends BasicSliderUI {

	ImageIcon sliderBackgroundImg;
	
	ImageIcon sliderFillerImg;
	
	ImageIcon sliderThumbImg;
	
	
	public BetAmountSliderUI(JSlider slider) {
		
		super(slider);
		
		
		sliderBackgroundImg = ResourceManager.getImage(ResourceManager.SLIDER_BACKGROUND);
		
		sliderFillerImg = ResourceManager.getImage(ResourceManager.SLIDER_FILLER);
		
		sliderThumbImg = ResourceManager.getImage(ResourceManager.SLIDER_THUMB);
		
		slider.setOpaque(false);

	}

	@Override
	protected Dimension getThumbSize() {
		return new Dimension(sliderThumbImg.getIconWidth(), sliderThumbImg.getIconHeight());
	}

	@Override
	public void paintThumb(Graphics g) {
		sliderThumbImg.paintIcon(slider,g, (int) thumbRect.getX(), 0);
	}

	@Override
	public void paintTrack(Graphics g) {
		
		sliderBackgroundImg.paintIcon(slider, g, 3, 8);
		
		g.drawImage(sliderFillerImg.getImage(), 5, 10, 5 + (int)thumbRect.getX(), 10 + sliderFillerImg.getIconHeight(), 0, 0, (int)thumbRect.getX(), sliderFillerImg.getIconHeight(), null);
		
	}
	
}
