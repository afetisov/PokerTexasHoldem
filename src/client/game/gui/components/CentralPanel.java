package client.game.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import commons.game.GameContext;
import commons.game.TableOccupancyType;
import commons.game.poker.ActionType;
import commons.game.poker.Card;
import commons.game.poker.HandRound;

import client.game.misc.ApplicationContext;
import client.game.misc.ApplicationGlobals;
import client.game.misc.Cards;
import client.game.misc.GameResourcesConstants;
import client.game.misc.ResourceManager;
import client.gui.GUIUtils;

public class CentralPanel extends JPanel {
	
	//constants
	private final Dimension seatButtonSize = new Dimension(100,30);
	
	private final Font betFont = new Font("Arial", Font.BOLD, 12);
	
	
	//images
	private ImageIcon smallCardsImg;
	
	private ImageIcon dealerButtonImg;
	
	private BufferedImage cardsFaceDownImg;
	
	private BufferedImage cardsFaceUpImg;
	
	
	//variables
	private int tableX;
	
	private int tableY;
	
	private Point [] seatButtonXY;
	
	private Point [] dealerButtonXY;
	
	private Point [] playerBoxXY;
	
	private PlayerInfoBoxPanel [] playerBoxPanels;
	
	private Dimension dealerButtonSize;

	private Point [] boardCardXY;
	
	private Point [] holeCardXY;
	
	private Point [] betAmountXY;
		
	
	//components
	private SeatButton [] seatButtons = new SeatButton[TableOccupancyType.MAX_PLAYERS]; 
	
	private JButton lobbyButton;
	
	private JButton seatoutButton;
	
	private JLabel playerBalanceLabel;
	
	private JLabel potAmountLabel;
	
	private JLabel rakeAmountLabel;
	
	private HoleCardsSwitcher holeCardsSwitcher;
	
	
	public CentralPanel() {
		
		setLayout(null);
		
		//tableImg = ResourceManager.getImage(ResourceManager.POKER_TABLE);

		//tableX = (panelWidth - tableImg.getIconWidth()) / 2;
		//tableY = (panelHeight - tableImg.getIconHeight())/ 2;

		smallCardsImg = ResourceManager.getImage(ResourceManager.CARDS_SMALL);
		
		dealerButtonImg = ResourceManager.getImage(ResourceManager.DEALER_BUTTON);
		
		dealerButtonSize = new Dimension((int)(dealerButtonImg.getIconWidth() * 0.5), (int)(dealerButtonImg.getIconHeight() * 0.5));
		
		cardsFaceDownImg = getCardsFaceDownImage();
		
		lobbyButton = new JButton("Lobby");
		
		//lobbyButton.setBounds(backgroundImg.getIconWidth() - 110, 10, 100, 30);

		add(lobbyButton);

		
		seatoutButton = new JButton("Seat out");
		
		//seatoutButton.setBounds(backgroundImg.getIconWidth() - 110, 40, 100, 30);
		
		seatoutButton.setVisible(false);
		
		add(seatoutButton);
		
		
		holeCardsSwitcher = new HoleCardsSwitcher();
		
		//holeCardsSwitcher.setBounds(50, panelHeight - GameResourcesConstants.LARGE_CARDS_HEIGHT / 2, GameResourcesConstants.LARGE_CARDS_WIDTH + 20, GameResourcesConstants.LARGE_CARDS_HEIGHT / 2);
		
		holeCardsSwitcher.setOpaque(false);
		
		holeCardsSwitcher.setVisible(false);
		
		add(holeCardsSwitcher);
		
		
		Font font = new Font("Arial", Font.BOLD, 14);
		
		playerBalanceLabel = new JLabel();
		
		playerBalanceLabel.setFont(font);
		
		playerBalanceLabel.setForeground(Color.WHITE);
		
		playerBalanceLabel.setBounds(10, 10, 250, 20);
		
		add(playerBalanceLabel);
		
		
		
		potAmountLabel = new JLabel();
		
		potAmountLabel.setFont(font);
		
		potAmountLabel.setForeground(Color.WHITE);
		
		potAmountLabel.setBounds(300, 10, 150, 20);
		
		add(potAmountLabel);
		
		
		rakeAmountLabel = new JLabel();
		
		rakeAmountLabel.setFont(font);
		
		rakeAmountLabel.setForeground(Color.WHITE);
		
		rakeAmountLabel.setBounds(490, 10, 150, 20);
		
		add(rakeAmountLabel);
		
		//initSeatButtons();
		
		//initDealerButtonXY();
		
		initPlayerBoxes();
		
		//initBoardCardXY();
		
		//initBetAmountXY();
	}

	@Override
	protected void paintComponent(Graphics g) {

		try {
			
			//draw dealer button
			//drawDealerButton(g);
			
			//draw board cards
			//drawBoardCards(g);
			
			//draw back up hole cards
			//drawHoleCards(g);
			
			//draw bets
			//drawBets(g);
			
			//draw won pots
			//drawWonChips(g);
		
		}catch(Exception exc) {
			//TODO proper loggin
		}
	}
	
	private void initSeatButtons() {
		
		seatButtonXY = new Point[TableOccupancyType.MAX_PLAYERS];
		
		seatButtonXY[0] = new Point(tableX, tableY + 200);
		seatButtonXY[1] = new Point(seatButtonXY[0].x + (seatButtonSize.width + 100), seatButtonXY[0].y - (seatButtonSize.height + 80));
		seatButtonXY[2] = new Point(seatButtonXY[1].x + (seatButtonSize.width + 50), seatButtonXY[1].y);
		seatButtonXY[3] = new Point(seatButtonXY[2].x + (seatButtonSize.width + 50), seatButtonXY[2].y);
		seatButtonXY[4] = new Point(seatButtonXY[3].x + (seatButtonSize.width + 100), seatButtonXY[3].y + (seatButtonSize.height + 80));
		seatButtonXY[5] = new Point(seatButtonXY[4].x, seatButtonXY[4].y + (seatButtonSize.height +  120));
		seatButtonXY[6] = new Point(seatButtonXY[5].x - (seatButtonSize.width + 100), seatButtonXY[5].y + (seatButtonSize.height + 80));
		seatButtonXY[7] = new Point(seatButtonXY[6].x - (seatButtonSize.width + 50), seatButtonXY[6].y);
		seatButtonXY[8] = new Point(seatButtonXY[7].x - (seatButtonSize.width + 50), seatButtonXY[7].y);
		seatButtonXY[9] = new Point(seatButtonXY[8].x - (seatButtonSize.width + 100), seatButtonXY[8].y - (seatButtonSize.height + 80));
		
		for(int i = 0; i < 10; i++) {
			seatButtons[i] = new SeatButton("Сесть");
			seatButtons[i].setSeatNumber((short) (i + 1));
			seatButtons[i].setBounds(seatButtonXY[i].x, seatButtonXY[i].y, seatButtonSize.width, seatButtonSize.height);
			add(seatButtons[i]);
		}
	}

	private void initDealerButtonXY() {

		dealerButtonXY = new Point[TableOccupancyType.MAX_PLAYERS];
		
		dealerButtonXY[0] = new Point(tableX + 200, tableY + 240);
		dealerButtonXY[1] = new Point(dealerButtonXY[0].x + (dealerButtonSize.width + 46), dealerButtonXY[0].y - (dealerButtonSize.height + 4));
		dealerButtonXY[2] = new Point(dealerButtonXY[1].x + (dealerButtonSize.width + 80), dealerButtonXY[1].y);
		dealerButtonXY[3] = new Point(dealerButtonXY[2].x + (dealerButtonSize.width + 90), dealerButtonXY[2].y);
		dealerButtonXY[4] = new Point(dealerButtonXY[3].x + (dealerButtonSize.width + 50), dealerButtonXY[3].y + (dealerButtonSize.height + 4));
		dealerButtonXY[5] = new Point(dealerButtonXY[4].x, dealerButtonXY[4].y + (dealerButtonSize.height + 40));
		dealerButtonXY[6] = new Point(dealerButtonXY[5].x - (dealerButtonSize.width + 50), dealerButtonXY[5].y + (dealerButtonSize.height + 4));
		dealerButtonXY[7] = new Point(dealerButtonXY[6].x - (dealerButtonSize.width + 90), dealerButtonXY[6].y);
		dealerButtonXY[8] = new Point(dealerButtonXY[7].x - (dealerButtonSize.width + 80), dealerButtonXY[7].y);
		dealerButtonXY[9] = new Point(dealerButtonXY[8].x - (dealerButtonSize.width + 46), dealerButtonXY[8].y - (dealerButtonSize.height + 4));
	}
	
	private void initPlayerBoxes() {
		
		playerBoxXY = new Point[TableOccupancyType.MAX_PLAYERS];
		playerBoxPanels = new PlayerInfoBoxPanel[TableOccupancyType.MAX_PLAYERS]; 
		holeCardXY = new Point[TableOccupancyType.MAX_PLAYERS];
		
		playerBoxPanels[0] = new PlayerInfoBoxPanel();
		playerBoxXY[0] = new Point(0, tableY + (playerBoxPanels[0].getSize().height + 80));
		playerBoxPanels[0].setLocation(playerBoxXY[0]);
		playerBoxPanels[0].setBounds(playerBoxPanels[0].getBoundsRectangle());
		add(playerBoxPanels[0]);
		
		playerBoxPanels[1] = new PlayerInfoBoxPanel();
		playerBoxXY[1] = new Point(playerBoxXY[0].x + (playerBoxPanels[1].getSize().width + 15), playerBoxXY[0].y - (playerBoxPanels[1].getSize().height + 20));
		playerBoxPanels[1].setLocation(playerBoxXY[1]);
		playerBoxPanels[1].setBounds(playerBoxPanels[1].getBoundsRectangle());
		add(playerBoxPanels[1]);
		
		playerBoxPanels[2] = new PlayerInfoBoxPanel();
		playerBoxXY[2] = new Point(playerBoxXY[1].x + (playerBoxPanels[2].getSize().width + 25), playerBoxXY[1].y);
		playerBoxPanels[2].setLocation(playerBoxXY[2]);
		playerBoxPanels[2].setBounds(playerBoxPanels[2].getBoundsRectangle());
		add(playerBoxPanels[2]);

		playerBoxPanels[3] = new PlayerInfoBoxPanel();
		playerBoxXY[3] = new Point(playerBoxXY[2].x + (playerBoxPanels[3].getSize().width + 25), playerBoxXY[2].y);
		playerBoxPanels[3].setLocation(playerBoxXY[3]);
		playerBoxPanels[3].setBounds(playerBoxPanels[3].getBoundsRectangle());
		add(playerBoxPanels[3]);

		playerBoxPanels[4] = new PlayerInfoBoxPanel();
		playerBoxXY[4] = new Point(playerBoxXY[3].x + (playerBoxPanels[4].getSize().width + 20), playerBoxXY[3].y + (playerBoxPanels[4].getSize().height + 20));
		playerBoxPanels[4].setLocation(playerBoxXY[4]);
		playerBoxPanels[4].setBounds(playerBoxPanels[4].getBoundsRectangle());
		add(playerBoxPanels[4]);

		playerBoxPanels[5] = new PlayerInfoBoxPanel();
		playerBoxXY[5] = new Point(playerBoxXY[4].x, playerBoxXY[4].y + (playerBoxPanels[5].getSize().height + 70));
		playerBoxPanels[5].setLocation(playerBoxXY[5]);
		playerBoxPanels[5].setBounds(playerBoxPanels[5].getBoundsRectangle());
		add(playerBoxPanels[5]);

		playerBoxPanels[6] = new PlayerInfoBoxPanel();
		playerBoxXY[6] = new Point(playerBoxXY[5].x - (playerBoxPanels[6].getSize().width + 20), playerBoxXY[5].y + (playerBoxPanels[5].getSize().height + 20));
		playerBoxPanels[6].setLocation(playerBoxXY[6]);
		playerBoxPanels[6].setBounds(playerBoxPanels[6].getBoundsRectangle());
		add(playerBoxPanels[6]);

		playerBoxPanels[7] = new PlayerInfoBoxPanel();
		playerBoxXY[7] = new Point(playerBoxXY[6].x - (playerBoxPanels[7].getSize().width + 25), playerBoxXY[6].y);
		playerBoxPanels[7].setLocation(playerBoxXY[7]);
		playerBoxPanels[7].setBounds(playerBoxPanels[7].getBoundsRectangle());
		add(playerBoxPanels[7]);

		playerBoxPanels[8] = new PlayerInfoBoxPanel();
		playerBoxXY[8] = new Point(playerBoxXY[7].x - (playerBoxPanels[8].getSize().width + 25), playerBoxXY[7].y);
		playerBoxPanels[8].setLocation(playerBoxXY[8]);
		playerBoxPanels[8].setBounds(playerBoxPanels[8].getBoundsRectangle());
		add(playerBoxPanels[8]);

		playerBoxPanels[9] = new PlayerInfoBoxPanel();
		playerBoxXY[9] = new Point(playerBoxXY[8].x - (playerBoxPanels[9].getSize().width + 15), playerBoxXY[8].y - (playerBoxPanels[0].getSize().height + 20));
		playerBoxPanels[9].setLocation(playerBoxXY[9]);
		playerBoxPanels[9].setBounds(playerBoxPanels[9].getBoundsRectangle());
		add(playerBoxPanels[9]);
		
		for(int i = 0; i < playerBoxXY.length; i++) {
			holeCardXY[i] = new Point(playerBoxXY[i].x + 50, playerBoxXY[i].y - 30);
		}
	}
	
/*
	private void initBoardCardXY() {
		
		int tableCenterX = tableX + tableImg.getIconWidth() / 2;
		int tableCenterY = tableY + tableImg.getIconHeight() / 2;
		
		int cardStartX = tableCenterX - (GameResourcesConstants.SMALL_CARDS_WIDTH * 5 + 4 * 10) / 2;
		int cardStartY = tableCenterY - GameResourcesConstants.SMALL_CARDS_HEIGHT / 2 + 15;
		
		boardCardXY = new Point[5];
		
		for(int i = 0; i < 5; i++) {
			boardCardXY[i] = new Point(cardStartX + i * GameResourcesConstants.SMALL_CARDS_WIDTH + i * 10, cardStartY);
		}
	}
*/	
	private void initBetAmountXY() {
		
		betAmountXY = new Point[TableOccupancyType.MAX_PLAYERS];
		
		betAmountXY[0] = new Point(tableX + 150, tableY + 240);
		betAmountXY[1] = new Point(betAmountXY[0].x + 100, betAmountXY[0].y - 50);
		betAmountXY[2] = new Point(betAmountXY[1].x + 140, betAmountXY[1].y);
		betAmountXY[3] = new Point(betAmountXY[2].x + 140, betAmountXY[2].y);
		betAmountXY[4] = new Point(betAmountXY[3].x + 100, betAmountXY[3].y + 50);
		betAmountXY[5] = new Point(betAmountXY[4].x, betAmountXY[4].y + 100);
		betAmountXY[6] = new Point(betAmountXY[5].x - 100, betAmountXY[5].y + 50);
		betAmountXY[7] = new Point(betAmountXY[6].x - 140, betAmountXY[6].y);
		betAmountXY[8] = new Point(betAmountXY[7].x - 140, betAmountXY[7].y);
		betAmountXY[9] = new Point(betAmountXY[8].x - 100, betAmountXY[8].y - 50);
	}
		
	private void drawDealerButton(Graphics g) {
		
		GameContext gameContext = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
		
		if(gameContext.dealer > -1) {
		
			g.drawImage(dealerButtonImg.getImage(), 
					dealerButtonXY[gameContext.dealer].x, dealerButtonXY[gameContext.dealer].y, 
					dealerButtonXY[gameContext.dealer].x + dealerButtonSize.width, dealerButtonXY[gameContext.dealer].y + dealerButtonSize.height, 
					0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		}		
	}
	
	private void drawBoardCards(Graphics g) {
		
		GameContext gameContext = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
		
		for(int i = 0; i < gameContext.board.size(); i++) {
			
			int dX = Cards.getOffsetX(gameContext.board.get(i).toString(), GameResourcesConstants.SMALL_CARDS_WIDTH); 
			
			int dY = Cards.getOffsetY(gameContext.board.get(i).toString(), GameResourcesConstants.SMALL_CARDS_HEIGHT);
			
			g.drawImage(smallCardsImg.getImage(),
					boardCardXY[i].x, boardCardXY[i].y, 
					boardCardXY[i].x + GameResourcesConstants.SMALL_CARDS_WIDTH, boardCardXY[i].y + GameResourcesConstants.SMALL_CARDS_HEIGHT, 
					dX, dY,
					dX + GameResourcesConstants.SMALL_CARDS_WIDTH, dY + GameResourcesConstants.SMALL_CARDS_HEIGHT, null);
		}
	}
	
	private void drawHoleCards(Graphics g) {

		GameContext gameContext = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
		
		//Card[] holeCards = (Card[]) ApplicationContext.get(ApplicationGlobals.HOLE_CARDS.name());
		
		Integer seatNumber = (Integer) ApplicationContext.get(ApplicationGlobals.SEAT_NUM.name());
		
		if(gameContext == null) return;
		
		if(gameContext.bettingRound == null) {
			cardsFaceUpImg = null;
		}
		
		for(int i = 0; i < gameContext.handPlayers.length; i++) {
			
			if(gameContext.handPlayers[i] != null 
					&& gameContext.activePlayers[i] != null 
					&& gameContext.handPlayers[i] == gameContext.activePlayers[i] 
					&& !gameContext.handPlayers[i].isFolded()
					&& gameContext.bettingRound != null) {

				if(seatNumber != null && i == (seatNumber - 1) ) {

					continue;
					/*
					if(holeCards != null && holeCards.length == 2) {
						g.drawImage(getHoleCardsImage(), holeCardXY[i].x, holeCardXY[i].y, null);
					}
					*/
				} else {
					
					if(gameContext.handPlayers[i].getHand() != null && gameContext.bettingRound == HandRound.SHOWDOWN) {
						g.drawImage(getCardsImage(gameContext.handPlayers[i].getHand().getHoleCards()), holeCardXY[i].x, holeCardXY[i].y,null);
					} else {
						g.drawImage(cardsFaceDownImg, holeCardXY[i].x, holeCardXY[i].y, null);
					}
				}
			}
		}
	}
	
	private void drawBets(Graphics g) {

		GameContext gameContext = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
		
		if(gameContext.bettingRound == HandRound.SHOWDOWN || gameContext.onePlayerLeft) return;
		
		g.setFont(betFont);
		
		g.setColor(Color.WHITE);
		
		FontMetrics fm = this.getFontMetrics(betFont);
		
		for(int i = 0; i < gameContext.handPlayers.length; i++) {
			
			commons.game.poker.Player p = gameContext.handPlayers[i];
			
			if(p != null && p.getCurrentBetAmount() != null && p.getCurrentBetAmount().compareTo(BigDecimal.ZERO) > 0) {
				
				String amount = "$" + GUIUtils.chips2text(p.getCurrentBetAmount());
				
				g.drawString(amount, betAmountXY[i].x - fm.stringWidth(amount) / 2, betAmountXY[i].y);
			
			}
		}
	}
	
	private void drawWonChips(Graphics g) {
		
		GameContext gameContext = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());

		if(gameContext.bettingRound != HandRound.SHOWDOWN && !gameContext.onePlayerLeft) return;
		
		g.setFont(betFont);
		
		g.setColor(Color.WHITE);

		FontMetrics fm = this.getFontMetrics(betFont);
		
		for(int i = 0; i < gameContext.handPlayers.length; i++) {
			
			commons.game.poker.Player p = gameContext.handPlayers[i];
			
			if(p != null && p.getWonChipsAmount().compareTo(BigDecimal.ZERO) > 0) {
				
				String amount = "$" + GUIUtils.chips2text(p.getWonChipsAmount());
				
				g.drawString(amount, betAmountXY[i].x - fm.stringWidth(amount) / 2, betAmountXY[i].y);
			
			}
		}
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
	
	private BufferedImage getCardsFaceUpImage() {
		
		Card[] holeCards = (Card[]) ApplicationContext.get(ApplicationGlobals.HOLE_CARDS.name());
		
		if(cardsFaceUpImg != null) return cardsFaceUpImg;
		
		cardsFaceUpImg = getCardsImage(holeCards);
			
		return cardsFaceUpImg;
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
	
	/*
	public int getPanelWidth() {
		return panelWidth;
	}*/

	/*
	public void setPanelWidth(int panelWidth) {
		tableX = (panelWidth - tableImg.getIconWidth()) / 2;
		this.panelWidth = panelWidth;
	}*/

	/*
	public int getPanelHeight() {
		return panelHeight;
	}*/

	/*
	public void setPanelHeight(int panelHeight) {
		tableY = (panelHeight - tableImg.getIconHeight())/ 2;
		this.panelHeight = panelHeight;
	}*/
	public JButton getLobbyButton() {

		return lobbyButton;
	}

	public JButton getSeatoutButton() {
		return seatoutButton;
	}

	public PlayerInfoBoxPanel[] getPlayerBoxPanels() {
		return playerBoxPanels;
	}

	public SeatButton[] getSeatButtons() {
		return seatButtons;
	}

	public JLabel getPlayerBalanceLabel() {
		return playerBalanceLabel;
	}

	public JLabel getPotAmountLabel() {
		return potAmountLabel;
	}

	public JLabel getRakeAmountLabel() {
		return rakeAmountLabel;
	}
	
	public  HoleCardsSwitcher getHoleCardsSwitcher() {
		return holeCardsSwitcher;
	}
}
