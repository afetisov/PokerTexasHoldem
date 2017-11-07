package client.game.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import commons.game.GameContext;
import commons.game.TableOccupancyType;
import commons.game.poker.HandRound;

import client.game.gui.components.ActionButton;
import client.game.gui.components.ActionButtonType;
import client.game.gui.components.BetAmountLabel;
import client.game.gui.components.BetAmountSlider;
import client.game.gui.components.BetAmountSliderUI;
import client.game.gui.components.ButtonsFactory;
import client.game.gui.components.HoleCardsSwitcher;
import client.game.gui.components.MenuActionType;
import client.game.gui.components.MenuButtonType;
import client.game.gui.components.PlayerInfoBoxPanel;
import client.game.gui.components.SeatButton;
import client.game.gui.components.ToolbarPanel;
import client.game.misc.ApplicationContext;
import client.game.misc.ApplicationGlobals;
import client.game.misc.Cards;
import client.game.misc.GameResourcesConstants;
import client.game.misc.ResourceManager;
import client.gui.GUIUtils;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	//constants
	private static final int ACTION_BUTTON_OFFSET = 10;
	
	private final Font betFont = new Font("Arial", Font.BOLD, 12);

	
	
	//images
	private ImageIcon backgroundImg;
	
	private ImageIcon betAmountFieldImg;
	
	private ImageIcon dealerButtonImg;
	
	private ImageIcon smallCardsImg;
	
	
	//components
	private BetAmountSlider betAmountSlider = new BetAmountSlider();
	
	private JButton [] seatButtons = new JButton[TableOccupancyType.MAX_PLAYERS]; 
	
	private Map<ActionButtonType, ActionButton> actionButtons = new HashMap<ActionButtonType, ActionButton>();

	private PlayerInfoBoxPanel [] playerBoxPanels;
	
	private JButton lobbyButton;
	
	private ToolbarPanel topPanel;
	
	private BetAmountLabel betAmountLabel = new BetAmountLabel();
	
	private JLabel playerBalanceLabel;
	
	private JLabel potAmountLabel;
	
	private JLabel playerNameLabel;
	
	
	
	//variables
	private Point betAmountFieldPoint;
	
	private Point [] seatButtonXY;

	private Point [] playerBoxXY;
	
	private Point [] dealerButtonXY;
	
	private Dimension dealerButtonSize;

	private Point [] boardCardXY;
	
	private Point [] betAmountXY;
	
	private Point tableCenterXY;

	private int backgroundImageWidth;
	
	private int backgroundImageHeight;
	
	private int betAmountFieldImageWidth;
	
	private int betAmountFieldImageHeight;
	
	private HoleCardsSwitcher holeCardsSwitcher;
	
	
	
	
	public GamePanel() {
		
		initComponents();
	}

	private void initComponents() {
	
		setLayout(null);
		
		backgroundImg = ResourceManager.getImage(ResourceManager.BACKGROUND);
		
		betAmountFieldImg = ResourceManager.getImage(ResourceManager.BET_AMOUNT_FIELD);
		
		dealerButtonImg = ResourceManager.getImage(ResourceManager.DEALER_BUTTON);

		smallCardsImg = ResourceManager.getImage(ResourceManager.CARDS_SMALL);
		
		dealerButtonSize = new Dimension((int)(dealerButtonImg.getIconWidth() * 0.5), (int)(dealerButtonImg.getIconHeight() * 0.5));
		
		backgroundImageWidth = backgroundImg.getIconWidth();
		
		backgroundImageHeight = backgroundImg.getIconHeight();

		setPreferredSize(new Dimension(backgroundImageWidth, backgroundImageHeight));		

		tableCenterXY = new Point(backgroundImageWidth / 2 - 20, backgroundImageHeight / 2 - 50);
		
		betAmountFieldImageWidth = betAmountFieldImg.getIconWidth();
		
		betAmountFieldImageHeight = betAmountFieldImg.getIconHeight();
		
		betAmountFieldPoint = new Point(backgroundImageWidth - betAmountFieldImageWidth, backgroundImageHeight - betAmountFieldImageHeight);
		
		topPanel = new ToolbarPanel();
		
		topPanel.setPreferredSize(new Dimension(backgroundImageWidth, topPanel.getPanelHeight()));
		
		topPanel.setBounds(0,0, backgroundImageWidth, topPanel.getPanelHeight());
		
		lobbyButton = ButtonsFactory.createMenuButton(MenuActionType.LOBBY, MenuButtonType.LOBBY.getDefaultIcon(), MenuButtonType.LOBBY.getPressedIcon(), MenuButtonType.LOBBY.getRolloverIcon(), MenuButtonType.LOBBY.getDefaultIcon());
		
		lobbyButton.setBounds(backgroundImg.getIconWidth() - (ResourceManager.MENU_BUTTON_WIDTH + 50), 5, ResourceManager.MENU_BUTTON_WIDTH, ResourceManager.MENU_BUTTON_HEIGHT);

		//add(lobbyButton);
		
		add(topPanel);
		
		topPanel.setLayout(null);
		
		topPanel.add(lobbyButton);

		betAmountLabel.setOpaque(false);
		
		betAmountLabel.setBounds(backgroundImageWidth - 105, backgroundImageHeight - 50, 100, 50);
		
		betAmountLabel.setFont(new Font("Arial",Font.BOLD, 24));
		
		betAmountLabel.setForeground(Color.WHITE);
		
		betAmountLabel.setVerticalAlignment(JLabel.CENTER);
		
		betAmountLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//betAmountLabel.setAmount(new BigDecimal(100));
		
		add(betAmountLabel);
		
		holeCardsSwitcher = new HoleCardsSwitcher();
		
		/*
		Card [] cards = new Card[2];
		
		try {
			cards[0] = Card.parse("As");
			cards[1] = Card.parse("Kh");
		}catch(Exception e) {}
		
		holeCardsSwitcher.setCards(cards);
		*/

		holeCardsSwitcher.setBounds(10, backgroundImageHeight - (ResourceManager.LARGE_CARDS_HEIGHT - 50), ResourceManager.LARGE_CARDS_WIDTH + 50, ResourceManager.LARGE_CARDS_HEIGHT - 50);
		
		//holeCardsSwitcher.setOpaque(false);
		
		holeCardsSwitcher.setVisible(false);
		
		add(holeCardsSwitcher);
		
		Font font = new Font("Arial", Font.BOLD, 14);
		
		
		playerBalanceLabel = new JLabel();
		
		playerBalanceLabel.setFont(font);
		
		playerBalanceLabel.setForeground(Color.WHITE);
		
		playerBalanceLabel.setBounds(10, 40, 150, 30);
		
		topPanel.add(playerBalanceLabel);
		
		//playerBalanceLabel.setText("Amount : $ " + GUIUtils.decimal2text(new BigDecimal(100)));
		
		
		
		potAmountLabel = new JLabel();
		
		potAmountLabel.setFont(font);
		
		potAmountLabel.setForeground(Color.WHITE);
		
		potAmountLabel.setBounds(tableCenterXY.x - 90, tableCenterXY.y - 20, 150, 40);
		
		add(potAmountLabel);


		font = new Font("Arial", Font.BOLD | Font.ITALIC, 24);
		
		playerNameLabel = new JLabel();
		
		playerNameLabel.setFont(font);
		
		playerNameLabel.setForeground(Color.WHITE);
		
		playerNameLabel.setBounds(10, 2, 150, 30);
		
		topPanel.add(playerNameLabel);

		
		initButtons();
		
		initBetAmountSlider();

		initSeatButtons();
		
		initPlayerBoxes() ;
		
		initDealerButtonXY();
		
		initBoardCardXY();
		
		initBetAmountXY();
	}

	@Override
	protected void paintComponent(Graphics g) {

		try {
			
			//draw background and table
			drawBackground(g);
			
			//draw dealer button
			drawDealerButton(g);
			
			//draw board cards
			drawBoardCards(g);
			
			//draw back up hole cards
			//drawHoleCards(g);
			
			//draw bets
			drawBets(g);
			
			//draw won pots
			drawWonChips(g);
		
		}catch(Exception exc) {
			//TODO proper logging
			exc.printStackTrace();
		}
	}
	
	private void drawBackground(Graphics g) {
		g.drawImage(backgroundImg.getImage(), 0, 0, null);
		g.drawImage(betAmountFieldImg.getImage(), betAmountFieldPoint.x, betAmountFieldPoint.y, null);
	}
	
	private void drawDealerButton(Graphics g) {
		
		//below is the commented code. it's used for adjusting dealer's button image only
		/* 
		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[0].x, dealerButtonXY[0].y, 
				dealerButtonXY[0].x + dealerButtonSize.width, dealerButtonXY[0].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);

		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[1].x, dealerButtonXY[1].y, 
				dealerButtonXY[1].x + dealerButtonSize.width, dealerButtonXY[1].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		
		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[2].x, dealerButtonXY[2].y, 
				dealerButtonXY[2].x + dealerButtonSize.width, dealerButtonXY[2].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		
		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[3].x, dealerButtonXY[3].y, 
				dealerButtonXY[3].x + dealerButtonSize.width, dealerButtonXY[3].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		
		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[4].x, dealerButtonXY[4].y, 
				dealerButtonXY[4].x + dealerButtonSize.width, dealerButtonXY[4].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		
		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[5].x, dealerButtonXY[5].y, 
				dealerButtonXY[5].x + dealerButtonSize.width, dealerButtonXY[5].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		
		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[6].x, dealerButtonXY[6].y, 
				dealerButtonXY[6].x + dealerButtonSize.width, dealerButtonXY[6].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		
		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[7].x, dealerButtonXY[7].y, 
				dealerButtonXY[7].x + dealerButtonSize.width, dealerButtonXY[7].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);

		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[8].x, dealerButtonXY[8].y, 
				dealerButtonXY[8].x + dealerButtonSize.width, dealerButtonXY[8].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		
		g.drawImage(dealerButtonImg.getImage(), 
				dealerButtonXY[9].x, dealerButtonXY[9].y, 
				dealerButtonXY[9].x + dealerButtonSize.width, dealerButtonXY[9].y + dealerButtonSize.height, 
				0, 0, dealerButtonImg.getIconWidth(), dealerButtonImg.getIconHeight(), null);
		*/
		
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
		
		int cardsNum = gameContext.board.size();
		
		for(int i = 0; i < cardsNum; i++) {
			
			
			int dX = Cards.getOffsetX(gameContext.board.get(i).toString(), GameResourcesConstants.SMALL_CARDS_WIDTH); 
			
			int dY = Cards.getOffsetY(gameContext.board.get(i).toString(), GameResourcesConstants.SMALL_CARDS_HEIGHT);
			
			g.drawImage(smallCardsImg.getImage(),
					boardCardXY[i].x, boardCardXY[i].y, 
					boardCardXY[i].x + ResourceManager.SMALL_CARDS_WIDTH, boardCardXY[i].y + ResourceManager.SMALL_CARDS_HEIGHT, 
					dX, dY,
					dX + ResourceManager.SMALL_CARDS_WIDTH, dY + ResourceManager.SMALL_CARDS_HEIGHT, null);
		}
	}
	
	
	private void drawBets(Graphics g) {

		GameContext gameContext = (GameContext) ApplicationContext.get(ApplicationGlobals.GAME_CONTEXT.name());
		
		if(gameContext.bettingRound == HandRound.SHOWDOWN || gameContext.onePlayerLeft) return;
		
		g.setFont(betFont);
		
		g.setColor(Color.WHITE);
		
		FontMetrics fm = this.getFontMetrics(betFont);
		
		int playersNum = gameContext.handPlayers.length;
		
		for(int i = 0; i < playersNum; i++) {
			
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

	
	private void initBetAmountXY() {
		
		betAmountXY = new Point[TableOccupancyType.MAX_PLAYERS];
		
		betAmountXY[0] = new Point(tableCenterXY.x - 200, tableCenterXY.y - 30);
		betAmountXY[1] = new Point(betAmountXY[0].x + 100, betAmountXY[0].y - 50);
		betAmountXY[2] = new Point(betAmountXY[1].x + 140, betAmountXY[1].y);
		betAmountXY[3] = new Point(betAmountXY[2].x + 140, betAmountXY[2].y);
		betAmountXY[4] = new Point(betAmountXY[3].x + 50, betAmountXY[3].y + 50);
		betAmountXY[5] = new Point(betAmountXY[4].x, betAmountXY[4].y + 100);
		betAmountXY[6] = new Point(betAmountXY[5].x - 50, betAmountXY[5].y + 50);
		betAmountXY[7] = new Point(betAmountXY[6].x - 140, betAmountXY[6].y);
		betAmountXY[8] = new Point(betAmountXY[7].x - 140, betAmountXY[7].y);
		betAmountXY[9] = new Point(betAmountXY[8].x - 100, betAmountXY[8].y - 50);
	}
	
	
	private void initDealerButtonXY() {

		dealerButtonXY = new Point[TableOccupancyType.MAX_PLAYERS];
		
		dealerButtonXY[0] = new Point(tableCenterXY.x - 150, tableCenterXY.y - 15);
		dealerButtonXY[1] = new Point(dealerButtonXY[0].x + (dealerButtonSize.width + 26), dealerButtonXY[0].y - (dealerButtonSize.height + 8));
		dealerButtonXY[2] = new Point(dealerButtonXY[1].x + (dealerButtonSize.width + 80), dealerButtonXY[1].y);
		dealerButtonXY[3] = new Point(dealerButtonXY[2].x + (dealerButtonSize.width + 90), dealerButtonXY[2].y);
		dealerButtonXY[4] = new Point(dealerButtonXY[3].x + (dealerButtonSize.width + 15), dealerButtonXY[3].y + (dealerButtonSize.height + 8));
		dealerButtonXY[5] = new Point(dealerButtonXY[4].x, dealerButtonXY[4].y + (dealerButtonSize.height + 40));
		dealerButtonXY[6] = new Point(dealerButtonXY[5].x - (dealerButtonSize.width + 15), dealerButtonXY[5].y + (dealerButtonSize.height + 2));
		dealerButtonXY[7] = new Point(dealerButtonXY[6].x - (dealerButtonSize.width + 90), dealerButtonXY[6].y);
		dealerButtonXY[8] = new Point(dealerButtonXY[7].x - (dealerButtonSize.width + 80), dealerButtonXY[7].y);
		dealerButtonXY[9] = new Point(dealerButtonXY[8].x - (dealerButtonSize.width + 26), dealerButtonXY[8].y - (dealerButtonSize.height + 2));
	}
	

	private void initBoardCardXY() {
		
		int dX = 5;
		
		int cardStartX = tableCenterXY.x - (ResourceManager.SMALL_CARDS_WIDTH * 5 + 4 * dX) / 2 + 20;
		int cardStartY = tableCenterXY.y - ResourceManager.SMALL_CARDS_HEIGHT / 2 + 38;
		
		boardCardXY = new Point[5];
		
		for(int i = 0; i < 5; i++) {
			boardCardXY[i] = new Point(cardStartX + i * ResourceManager.SMALL_CARDS_WIDTH + i * dX, cardStartY);
		}
	}
	
	private void initButtons() {
		
		
		Rectangle [] actionButtonBounds = new Rectangle[3];
		
		int y = backgroundImageHeight - ResourceManager.ACTION_BUTTON_HEIGHT - ACTION_BUTTON_OFFSET;
		int x = 170;
		int dX = 50;
		
		
		for(int i = 0; i < 3; i++) {
			actionButtonBounds[i] = new Rectangle(x + i * (ResourceManager.ACTION_BUTTON_WIDTH + dX), y, ResourceManager.ACTION_BUTTON_WIDTH, ResourceManager.ACTION_BUTTON_HEIGHT);
		}
		
		for(ActionButtonType type : ActionButtonType.values()) {
			
			ActionButton button = ButtonsFactory.createButton(
					type.getActionType(),
					type.getDefaultIcon(), 
					type.getPressedIcon(),
					type.getRolloverIcon(),
					type.getDefaultIcon()
			);
			
			actionButtons.put(type, button);
			
			switch(type) {
			case FOLD:
				button.setBounds(actionButtonBounds[0]);
				break;
			case CHECK:
			case CALL:
				button.setBounds(actionButtonBounds[1]);				
				if(type == ActionButtonType.CALL) button.setVisible(false);
				break;
			case BET:
			case RAISE:
				button.setBounds(actionButtonBounds[2]);
				if(type == ActionButtonType.RAISE) button.setVisible(false);				
				break;
			default:
				break;
			}
			
			add(button);
		}
	}
	
	private void initBetAmountSlider() {
		
		betAmountSlider.setUI(new BetAmountSliderUI(betAmountSlider));
		
		betAmountSlider.setPreferredSize(new Dimension(157, 26));
		
		betAmountSlider.setFocusable(false);

		betAmountSlider.setBounds(570, backgroundImageHeight - ResourceManager.ACTION_BUTTON_HEIGHT - ACTION_BUTTON_OFFSET - 33, 157, 26);
		
		add(betAmountSlider);		
	}
	
	
	
	private void initSeatButtons() {
		
		seatButtonXY = new Point[TableOccupancyType.MAX_PLAYERS];
		
		seatButtonXY[0] = new Point(tableCenterXY.x - (ResourceManager.MENU_BUTTON_WIDTH + 270), tableCenterXY.y - (ResourceManager.MENU_BUTTON_HEIGHT + 70));
		
		seatButtonXY[1] = new Point(seatButtonXY[0].x + (ResourceManager.MENU_BUTTON_WIDTH + 70), seatButtonXY[0].y - (ResourceManager.MENU_BUTTON_HEIGHT + 80));
		seatButtonXY[2] = new Point(seatButtonXY[1].x + (ResourceManager.MENU_BUTTON_WIDTH + 50), seatButtonXY[1].y);
		seatButtonXY[3] = new Point(seatButtonXY[2].x + (ResourceManager.MENU_BUTTON_WIDTH + 50), seatButtonXY[2].y);
		seatButtonXY[4] = new Point(seatButtonXY[3].x + (ResourceManager.MENU_BUTTON_WIDTH + 60), seatButtonXY[3].y + (ResourceManager.MENU_BUTTON_HEIGHT + 80));
		seatButtonXY[5] = new Point(seatButtonXY[4].x, seatButtonXY[4].y + (ResourceManager.MENU_BUTTON_HEIGHT +  190));
		seatButtonXY[6] = new Point(seatButtonXY[5].x - (ResourceManager.MENU_BUTTON_WIDTH + 60), seatButtonXY[5].y + (ResourceManager.MENU_BUTTON_HEIGHT + 100));
		seatButtonXY[7] = new Point(seatButtonXY[6].x - (ResourceManager.MENU_BUTTON_WIDTH + 50), seatButtonXY[6].y);
		seatButtonXY[8] = new Point(seatButtonXY[7].x - (ResourceManager.MENU_BUTTON_WIDTH + 50), seatButtonXY[7].y);
		seatButtonXY[9] = new Point(seatButtonXY[8].x - (ResourceManager.MENU_BUTTON_WIDTH + 70), seatButtonXY[8].y - (ResourceManager.MENU_BUTTON_HEIGHT + 100));
		
		for(int i = 0; i < TableOccupancyType.MAX_PLAYERS; i++) {
			seatButtons[i] = ButtonsFactory.createMenuButton(MenuActionType.SEAT_DOWN, MenuButtonType.SEAT_DOWN.getDefaultIcon(), MenuButtonType.SEAT_DOWN.getPressedIcon(), MenuButtonType.SEAT_DOWN.getRolloverIcon(), MenuButtonType.SEAT_DOWN.getDefaultIcon());
			SeatButton button = (SeatButton) seatButtons[i];
			button.setSeatNumber((short) (i + 1));
			button.setBounds(seatButtonXY[i].x, seatButtonXY[i].y, ResourceManager.MENU_BUTTON_WIDTH, ResourceManager.MENU_BUTTON_HEIGHT);
			add(button);
		}
	}
	
	private void initPlayerBoxes() {
		
		playerBoxXY = new Point[TableOccupancyType.MAX_PLAYERS];
		playerBoxPanels = new PlayerInfoBoxPanel[TableOccupancyType.MAX_PLAYERS]; 
		
		playerBoxPanels[0] = new PlayerInfoBoxPanel();
		playerBoxXY[0] = new Point(10, tableCenterXY.y - (playerBoxPanels[0].getSize().height + 30));
		playerBoxPanels[0].setLocation(playerBoxXY[0]);
		playerBoxPanels[0].setBounds(playerBoxPanels[0].getBoundsRectangle());
		add(playerBoxPanels[0]);
		
		
		playerBoxPanels[1] = new PlayerInfoBoxPanel();
		playerBoxXY[1] = new Point(playerBoxXY[0].x + (playerBoxPanels[1].getSize().width - 25), playerBoxXY[0].y - (playerBoxPanels[1].getSize().height + 35));
		playerBoxPanels[1].setLocation(playerBoxXY[1]);
		playerBoxPanels[1].setBounds(playerBoxPanels[1].getBoundsRectangle());
		add(playerBoxPanels[1]);
		
		
		playerBoxPanels[2] = new PlayerInfoBoxPanel();
		playerBoxXY[2] = new Point(playerBoxXY[1].x + (playerBoxPanels[2].getSize().width + 30), playerBoxXY[1].y);
		playerBoxPanels[2].setLocation(playerBoxXY[2]);
		playerBoxPanels[2].setBounds(playerBoxPanels[2].getBoundsRectangle());
		add(playerBoxPanels[2]);

		
		playerBoxPanels[3] = new PlayerInfoBoxPanel();
		playerBoxXY[3] = new Point(playerBoxXY[2].x + (playerBoxPanels[3].getSize().width + 30), playerBoxXY[2].y);
		playerBoxPanels[3].setLocation(playerBoxXY[3]);
		playerBoxPanels[3].setBounds(playerBoxPanels[3].getBoundsRectangle());
		add(playerBoxPanels[3]);

		
		playerBoxPanels[4] = new PlayerInfoBoxPanel();
		playerBoxXY[4] = new Point(playerBoxXY[3].x + (playerBoxPanels[4].getSize().width - 25), playerBoxXY[3].y + (playerBoxPanels[4].getSize().height + 35));
		playerBoxPanels[4].setLocation(playerBoxXY[4]);
		playerBoxPanels[4].setBounds(playerBoxPanels[4].getBoundsRectangle());
		add(playerBoxPanels[4]);

		
		playerBoxPanels[5] = new PlayerInfoBoxPanel();
		playerBoxXY[5] = new Point(playerBoxXY[4].x, playerBoxXY[4].y + (playerBoxPanels[5].getSize().height + 120));
		playerBoxPanels[5].setLocation(playerBoxXY[5]);
		playerBoxPanels[5].setBounds(playerBoxPanels[5].getBoundsRectangle());
		add(playerBoxPanels[5]);

		
		playerBoxPanels[6] = new PlayerInfoBoxPanel();
		playerBoxXY[6] = new Point(playerBoxXY[5].x - (playerBoxPanels[6].getSize().width - 25), playerBoxXY[5].y + (playerBoxPanels[5].getSize().height + 50));
		playerBoxPanels[6].setLocation(playerBoxXY[6]);
		playerBoxPanels[6].setBounds(playerBoxPanels[6].getBoundsRectangle());
		add(playerBoxPanels[6]);

		
		playerBoxPanels[7] = new PlayerInfoBoxPanel();
		playerBoxXY[7] = new Point(playerBoxXY[6].x - (playerBoxPanels[7].getSize().width + 30), playerBoxXY[6].y);
		playerBoxPanels[7].setLocation(playerBoxXY[7]);
		playerBoxPanels[7].setBounds(playerBoxPanels[7].getBoundsRectangle());
		add(playerBoxPanels[7]);

		 
		playerBoxPanels[8] = new PlayerInfoBoxPanel();
		playerBoxXY[8] = new Point(playerBoxXY[7].x - (playerBoxPanels[8].getSize().width + 30), playerBoxXY[7].y);
		playerBoxPanels[8].setLocation(playerBoxXY[8]);
		playerBoxPanels[8].setBounds(playerBoxPanels[8].getBoundsRectangle());
		add(playerBoxPanels[8]);

		
		
		playerBoxPanels[9] = new PlayerInfoBoxPanel();
		playerBoxXY[9] = new Point(playerBoxXY[8].x - (playerBoxPanels[9].getSize().width - 25), playerBoxXY[8].y - (playerBoxPanels[0].getSize().height + 45));
		playerBoxPanels[9].setLocation(playerBoxXY[9]);
		playerBoxPanels[9].setBounds(playerBoxPanels[9].getBoundsRectangle());
		add(playerBoxPanels[9]);
	}
	
	public JButton[] getSeatButtons() {
		return seatButtons;
	}

	public PlayerInfoBoxPanel[] getPlayerBoxPanels() {
		return playerBoxPanels;
	}
	
	public BetAmountSlider getBetAmountSlider() {
		return betAmountSlider;
	}

	public Map<ActionButtonType, ActionButton> getButtons() {
		return actionButtons;
	}

	public BetAmountLabel getBetAmountLabel() {
		return betAmountLabel;
	}
	
	public JLabel getPlayerBalanceLabel() {
		return playerBalanceLabel;
	}

	public JLabel getPotAmountLabel() {
		return potAmountLabel;
	}
	
	public JButton getLobbyButton() {

		return lobbyButton;
	}
	
	public  HoleCardsSwitcher getHoleCardsSwitcher() {
		return holeCardsSwitcher;
	}
	
	public JLabel getPlayerNameLabel() {
		return playerNameLabel;
	}
}
