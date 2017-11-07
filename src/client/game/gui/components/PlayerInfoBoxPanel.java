package client.game.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;

import javax.security.auth.login.AppConfigurationEntry;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import commons.game.GameContext;
import commons.game.poker.Card;
import commons.game.poker.HandRound;
import commons.game.poker.Player;
import commons.game.poker.PlayerHand;

import client.game.GameClientApplication;
import client.game.misc.ApplicationContext;
import client.game.misc.ApplicationGlobals;
import client.game.misc.Cards;
import client.game.misc.GameResourcesConstants;
import client.game.misc.ResourceManager;

public class PlayerInfoBoxPanel extends JPanel {
	
	//images
	private ImageIcon playerInfoBoxImg;

	private ImageIcon timerTileImg;
	
	private BufferedImage timerImg;
	
	private ImageIcon smallCardsImg;
	
	private BufferedImage cardsFaceDownImg;
	
	private BufferedImage cardsFaceUpImg;
	
	
	//variables
	private boolean showTimer;
	
	private int timerPortion;
	
	private boolean isShowdown;
	
	private PlayerHand playerHand;
	
	
	//components
	private JLabel nameLabel;
	
	private JLabel chipsLabel;
	
	private JLabel miscInfoLabel;
	
	private TimerCounter timerCounter;
	
	
	public PlayerInfoBoxPanel() {

		setLayout(null);
		
		setOpaque(false);
		
		playerInfoBoxImg = ResourceManager.getImage(ResourceManager.PLAYER_INFO_BOX);
		
		setSize(new Dimension(playerInfoBoxImg.getIconWidth(), playerInfoBoxImg.getIconHeight()));
		
		smallCardsImg = ResourceManager.getImage(ResourceManager.CARDS_SMALL);
		
		cardsFaceDownImg = getCardsFaceDownImage();
		
		timerCounter = new TimerCounter();
		timerCounter.setBounds(10, 28, timerCounter.getPreferredSize().width, timerCounter.getPreferredSize().height);
		timerCounter.setVisible(false);
		
		Font font = new Font("Arial", Font.BOLD, 14);
		
		nameLabel = new JLabel();
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(font);
		
		nameLabel.setBounds(5, 2, 100, 25);
		
		chipsLabel = new JLabel();
		chipsLabel.setForeground(Color.WHITE);
		chipsLabel.setFont(font);
		
		chipsLabel.setBounds(getSize().width - 70, 2, 70, 25);
		
		chipsLabel.setHorizontalAlignment(JLabel.TRAILING);
		
		miscInfoLabel = new JLabel();
		miscInfoLabel.setForeground(Color.WHITE);
		miscInfoLabel.setFont(font);
		
		miscInfoLabel.setBounds(10, 30, getSize().width, 30);

		add(nameLabel);
		add(chipsLabel);
		add(miscInfoLabel);
		add(timerCounter);

	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(playerInfoBoxImg.getImage(), 0, 0,playerInfoBoxImg.getIconWidth(), playerInfoBoxImg.getIconHeight(), null);
		drawHoleCards(g);
	}

	private void drawHoleCards(Graphics g) {
		
		if( isShowdown && playerHand != null ) {
			g.drawImage(getCardsImage(playerHand.getHoleCards()), 100, 30, null);
		} else {
			g.drawImage(cardsFaceDownImg, 100, 30, null);
		}
	}
	
	private BufferedImage getCardsImage(Card [] cards) {
		
		if(cards != null && cards.length == 2) {
			
			BufferedImage image = new BufferedImage(GameResourcesConstants.SMALL_CARDS_WIDTH + 20, GameResourcesConstants.SMALL_CARDS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = image.getGraphics();
			
			for(int i = 0; i < cards.length; i++) {

				int dX = Cards.getOffsetX(cards[i].toString(), GameResourcesConstants.SMALL_CARDS_WIDTH);
				
				int dY = Cards.getOffsetY(cards[i].toString(), GameResourcesConstants.SMALL_CARDS_HEIGHT);
		
				g.drawImage(smallCardsImg.getImage(), i * 20, 0, 
						(i == 0 ? 25 : GameResourcesConstants.SMALL_CARDS_WIDTH + 20), GameResourcesConstants.SMALL_CARDS_HEIGHT, 
						dX, dY, 
						dX + (i == 0 ? 25 : GameResourcesConstants.SMALL_CARDS_WIDTH), dY + GameResourcesConstants.SMALL_CARDS_HEIGHT, null);
			
			}
			
			image.flush();
			
			return image;
		}
		
		return null;
	} 
	
	private BufferedImage getCardsFaceDownImage() {

		int dX = Cards.getOffsetX(Cards.BACK, GameResourcesConstants.SMALL_CARDS_WIDTH);
		
		int dY = Cards.getOffsetY(Cards.BACK, GameResourcesConstants.SMALL_CARDS_HEIGHT);
		
		BufferedImage holeCardsBack = new BufferedImage(GameResourcesConstants.SMALL_CARDS_WIDTH + 20, GameResourcesConstants.SMALL_CARDS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = holeCardsBack.getGraphics();
		
		g.drawImage(smallCardsImg.getImage(), 0, 0, 25,GameResourcesConstants.SMALL_CARDS_HEIGHT, 
				dX, dY, dX + 25, dY + GameResourcesConstants.SMALL_CARDS_HEIGHT, null);

		g.drawImage(smallCardsImg.getImage(), 20, 0, 
				GameResourcesConstants.SMALL_CARDS_WIDTH + 20, GameResourcesConstants.SMALL_CARDS_HEIGHT,
				dX, dY, dX + GameResourcesConstants.SMALL_CARDS_WIDTH, dY + GameResourcesConstants.SMALL_CARDS_HEIGHT, null);

		holeCardsBack.flush();

		return holeCardsBack;
	}
	
	public Rectangle getBoundsRectangle() {
		return new Rectangle(getLocation(), getSize());
	}

	public void setPlayerInfo(Player player) {
		
		GameContext gameCntx = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
		
		if(gameCntx.bettingRound == null) {
			cardsFaceUpImg = null;
		}
		
		nameLabel.setText(player == null ? "" : player.getNickname());
		
		chipsLabel.setText(player == null ? "" : player.getChipsStack().toPlainString());
		
		if(gameCntx.bettingRound == HandRound.SHOWDOWN) {

			isShowdown = true;
			
			miscInfoLabel.setText( player == null || player.getHand() == null ? "" : player.getHand().getHandRank().toString() );
	
			playerHand = player == null ? null : player.getHand();
			
		} else {
			
			isShowdown = false;
			
			miscInfoLabel.setText( player == null || player.getLastAction() == null ? "" : player.getLastAction().toString() );
		
			playerHand = null;
			
		}
	}
	
	public void startTimer(int seconds) {
		timerCounter.setSecondsNumber(seconds);
		timerCounter.setVisible(true);
		timerCounter.start();
	}
	
	public void stopTimer() {
		timerCounter.stop();
		timerCounter.setVisible(false);
	}
}
