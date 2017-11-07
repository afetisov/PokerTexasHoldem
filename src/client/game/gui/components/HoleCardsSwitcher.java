package client.game.gui.components;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import client.game.misc.Cards;
import client.game.misc.GameResourcesConstants;
import client.game.misc.ResourceManager;

import commons.game.poker.Card;

public class HoleCardsSwitcher extends JToggleButton {

	private static final int distanceX = 30;
	
	private static final int distanceY = 8;

	private static final int cutoffSize = 60;
	
	private Card [] cards;
	
	private BufferedImage faceUpImage;
	
	private BufferedImage faceDownImage;
	
	private ImageIcon cardsImage;
	

	
	public HoleCardsSwitcher() {
		
		cardsImage = ResourceManager.getImage(ResourceManager.CARDS_BIG);
		
		initCardsFaceDownImage();
	
		setContentAreaFilled(false);
		
		setIcon(new ImageIcon(faceDownImage));
		
		setBorderPainted(false);
		
		setFocusable(false);
		
	}

	
	public Card[] getCards() {
		return cards;
	}

	
	public void setCards(Card[] cards) {
		
		this.cards = cards;
		
		if(cards == null) {

			setVisible(false);
			
			faceUpImage = null;
		
		} else {
		
			initCardsFaceUpImage();
			
			setSelectedIcon(new ImageIcon(faceUpImage));
			
			setVisible(true);
		}
	}

	
	private void initCardsFaceDownImage() {

		int dX = Cards.getOffsetX(Cards.BACK, GameResourcesConstants.LARGE_CARDS_WIDTH);
		
		int dY = Cards.getOffsetY(Cards.BACK, GameResourcesConstants.LARGE_CARDS_HEIGHT);
		
		faceDownImage = new BufferedImage(GameResourcesConstants.LARGE_CARDS_WIDTH + 50, GameResourcesConstants.LARGE_CARDS_HEIGHT - cutoffSize, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = (Graphics2D) faceDownImage.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		g.translate(GameResourcesConstants.LARGE_CARDS_WIDTH / 2, GameResourcesConstants.LARGE_CARDS_HEIGHT / 2);
		
		g.rotate(0.1);
		
		g.translate(-GameResourcesConstants.LARGE_CARDS_WIDTH / 2, -GameResourcesConstants.LARGE_CARDS_HEIGHT / 2);
		
		g.drawImage(cardsImage.getImage(), 0, distanceY, GameResourcesConstants.LARGE_CARDS_WIDTH, GameResourcesConstants.LARGE_CARDS_HEIGHT, dX, dY, dX + GameResourcesConstants.LARGE_CARDS_WIDTH, dY + GameResourcesConstants.LARGE_CARDS_HEIGHT, null);
		
		g.translate(GameResourcesConstants.LARGE_CARDS_WIDTH / 2, GameResourcesConstants.LARGE_CARDS_HEIGHT / 2);
		
		g.rotate(0.2);
		
		g.translate(-GameResourcesConstants.LARGE_CARDS_WIDTH / 2, -GameResourcesConstants.LARGE_CARDS_HEIGHT / 2);
		
		g.drawImage(cardsImage.getImage(), distanceX, 0, distanceX + GameResourcesConstants.LARGE_CARDS_WIDTH, GameResourcesConstants.LARGE_CARDS_HEIGHT, dX, dY, dX + GameResourcesConstants.LARGE_CARDS_WIDTH, dY + (GameResourcesConstants.LARGE_CARDS_HEIGHT), null);
		
		faceDownImage.flush();
	}
	
	
	
	private void initCardsFaceUpImage() {

		if(cards != null) {
		
			faceUpImage = new BufferedImage(GameResourcesConstants.LARGE_CARDS_WIDTH + 50, GameResourcesConstants.LARGE_CARDS_HEIGHT - cutoffSize, BufferedImage.TYPE_INT_ARGB);
	
			Graphics2D g = (Graphics2D)faceUpImage.getGraphics();
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			
			int shiftX = 0;
			
			int shiftY = distanceY;
			
			double rad = 0.1;
				
			for(Card card : cards) {
				
				int dX = Cards.getOffsetX(card.toString(), GameResourcesConstants.LARGE_CARDS_WIDTH);
			
				int dY = Cards.getOffsetY(card.toString(), GameResourcesConstants.LARGE_CARDS_HEIGHT);
				
				g.translate(GameResourcesConstants.LARGE_CARDS_WIDTH / 2, GameResourcesConstants.LARGE_CARDS_HEIGHT / 2);
				
				g.rotate(rad);
				
				g.translate(-GameResourcesConstants.LARGE_CARDS_WIDTH / 2, -GameResourcesConstants.LARGE_CARDS_HEIGHT / 2);

				
				g.drawImage(cardsImage.getImage(), 0 + shiftX, shiftY, GameResourcesConstants.LARGE_CARDS_WIDTH + shiftX, GameResourcesConstants.LARGE_CARDS_HEIGHT, dX, dY, dX + GameResourcesConstants.LARGE_CARDS_WIDTH, dY + GameResourcesConstants.LARGE_CARDS_HEIGHT, null);
			
				shiftX += distanceX;
				shiftY = 0;
				rad += 0.1;
			}
			
		} else {
			faceUpImage = null;
		}
	}
}
