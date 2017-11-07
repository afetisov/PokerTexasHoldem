package client.game.gui.components;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import client.game.misc.Matrix;
import client.game.misc.ResourceManager;


public enum MenuButtonType {
	
	BUY_MORE_CHIPS {

		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.BUY_MORE_CHIPS_DEFAULT);
		}

		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.BUY_MORE_CHIPS_PRESSED);
		}

		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.BUY_MORE_CHIPS_DEFAULT);
		}

		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.BUY_MORE_CHIPS_ROLLOVER);
		}

		@Override
		public MenuActionType getActionType() {
			return MenuActionType.BUY_MORE_CHIPS;
		}
		
	},
	STAND_UP {

		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.STAND_UP_DEFAULT);
		}

		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.STAND_UP_PRESSED);
		}

		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.STAND_UP_DEFAULT);
		}

		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.STAND_UP_ROLLOVER);
		}

		@Override
		public MenuActionType getActionType() {
			return MenuActionType.STAND_UP;
		}
		
	},
	LOBBY {

		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.LOBBY_DEFAULT);
		}

		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.LOBBY_PRESSED);
		}

		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.LOBBY_DEFAULT);
		}

		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.LOBBY_ROLLOVER);
		}

		@Override
		public MenuActionType getActionType() {
			return MenuActionType.LOBBY;
		}
		
	},
	SEAT_DOWN {

		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.SEAT_DOWN_DEFAULT);
		}

		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.SEAT_DOWN_PRESSED);
		}

		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.SEAT_DOWN_DEFAULT);
		}

		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.SEAT_DOWN_ROLLOVER);
		}

		@Override
		public MenuActionType getActionType() {
			return MenuActionType.SEAT_DOWN;
		}
		
	};
	
	private static BufferedImage menuButtonsImage;
	
	private static BufferedImage getMenuButtonsImage() {
		
		if(menuButtonsImage == null) {
			
			ImageIcon img = ResourceManager.getImage(ResourceManager.MENU_BUTTONS);
			menuButtonsImage = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics g = menuButtonsImage.getGraphics();
			g.drawImage(img.getImage(), 0, 0, null);
			g.dispose();
		
		}
		
		return menuButtonsImage;
	}
	
	public abstract ImageIcon getDefaultIcon();
	public abstract ImageIcon getPressedIcon();
	public abstract ImageIcon getDisabledIcon();
	public abstract ImageIcon getRolloverIcon();
	public abstract MenuActionType getActionType();
	
	enum ButtonIconType {
		
		BUY_MORE_CHIPS_DEFAULT,
		BUY_MORE_CHIPS_PRESSED,
		BUY_MORE_CHIPS_ROLLOVER,
		
		STAND_UP_DEFAULT,
		STAND_UP_PRESSED,
		STAND_UP_ROLLOVER,
		
		LOBBY_DEFAULT,
		LOBBY_PRESSED,
		LOBBY_ROLLOVER,
		
		SEAT_DOWN_DEFAULT,
		SEAT_DOWN_PRESSED,
		SEAT_DOWN_ROLLOVER
		
	}
	
	private static Map<ButtonIconType, Matrix> buttonMatrix = new HashMap<ButtonIconType, Matrix>();
	
	static {
		
		buttonMatrix.put(ButtonIconType.BUY_MORE_CHIPS_DEFAULT, new Matrix(0,0));
		buttonMatrix.put(ButtonIconType.BUY_MORE_CHIPS_ROLLOVER, new Matrix(1,0));
		buttonMatrix.put(ButtonIconType.BUY_MORE_CHIPS_PRESSED, new Matrix(2,0));
		
		buttonMatrix.put(ButtonIconType.STAND_UP_DEFAULT, new Matrix(0,1));
		buttonMatrix.put(ButtonIconType.STAND_UP_ROLLOVER, new Matrix(1,1));
		buttonMatrix.put(ButtonIconType.STAND_UP_PRESSED, new Matrix(2,1));
		
		buttonMatrix.put(ButtonIconType.LOBBY_DEFAULT, new Matrix(0,2));
		buttonMatrix.put(ButtonIconType.LOBBY_ROLLOVER, new Matrix(1,2));
		buttonMatrix.put(ButtonIconType.LOBBY_PRESSED, new Matrix(2,2));

		buttonMatrix.put(ButtonIconType.SEAT_DOWN_DEFAULT, new Matrix(0,3));
		buttonMatrix.put(ButtonIconType.SEAT_DOWN_ROLLOVER, new Matrix(1,3));
		buttonMatrix.put(ButtonIconType.SEAT_DOWN_PRESSED, new Matrix(2,3));

	}
	
	private static Point getButtonIconTypeOffset(ButtonIconType type) {
		
		if(type == null) return null;
		
		Matrix m = buttonMatrix.get(type);
		
		if(m == null) return null;
		
		return new Point(m.x * ResourceManager.MENU_BUTTON_WIDTH, m.y * ResourceManager.MENU_BUTTON_HEIGHT);
	
	}
	
	private static ImageIcon getButtonImageIcon(ButtonIconType type) {
		
		if (type == null) return null;
		
		Point offset = getButtonIconTypeOffset(type);
		
		return new ImageIcon(getMenuButtonsImage().getSubimage(offset.x, offset.y, ResourceManager.MENU_BUTTON_WIDTH, ResourceManager.MENU_BUTTON_HEIGHT));
	}

}




