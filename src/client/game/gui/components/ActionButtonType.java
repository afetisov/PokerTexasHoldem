package client.game.gui.components;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import commons.game.poker.ActionType;
import client.game.misc.Matrix;
import client.game.misc.ResourceManager;

public enum ActionButtonType {
	
	FOLD {
		
		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.FOLD_DEFAULT);
		}

		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.FOLD_PRESSED);
		}
		
		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.FOLD_ROLLOVER);
		}

		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.FOLD_DEFAULT);
		}

		@Override
		public ActionType getActionType() {
			return ActionType.FOLD;
		}
	},
	CHECK {
		
		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.CHECK_DEFAULT);
		}

		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.CHECK_PRESSED);
		}
		
		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.FOLD_DEFAULT);
		}
		
		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.CHECK_ROLLOVER);
		}

		@Override
		public ActionType getActionType() {
			return ActionType.CHECK;
		}
	},
	CALL {
		
		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.CALL_DEFAULT);
		}

		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.CALL_PRESSED);
		}

		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.CALL_ROLLOVER);
		}

		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.CALL_DEFAULT);
		}

		@Override
		public ActionType getActionType() {
			return ActionType.CALL;
		}
	},
	BET {
		
		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.BET_DEFAULT);
		}

		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.BET_PRESSED);
		}
		
		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.BET_ROLLOVER);
		}

		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.BET_DEFAULT);
		}

		@Override
		public ActionType getActionType() {
			return ActionType.BET;
		}
	},
	RAISE {

		@Override
		public ImageIcon getDefaultIcon() {
			return getButtonImageIcon(ButtonIconType.RAISE_DEFAULT);
		}

		
		@Override
		public ImageIcon getPressedIcon() {
			return getButtonImageIcon(ButtonIconType.RAISE_PRESSED);
		}

		@Override
		public ImageIcon getRolloverIcon() {
			return getButtonImageIcon(ButtonIconType.RAISE_ROLLOVER);
		}

		@Override
		public ImageIcon getDisabledIcon() {
			return getButtonImageIcon(ButtonIconType.RAISE_DEFAULT);
		}

		@Override
		public ActionType getActionType() {
			return ActionType.RAISE;
		}
	};
	
	private static BufferedImage actionButtonsImage;
	
	private static BufferedImage getActionButtonsImage() {
		
		if(actionButtonsImage == null) {
			ImageIcon img = ResourceManager.getImage(ResourceManager.ACTION_BUTTONS);
			actionButtonsImage = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics g = actionButtonsImage.getGraphics();
			g.drawImage(img.getImage(), 0, 0, null);
			g.dispose();
		}
		
		return actionButtonsImage;
	}
	
	public abstract ImageIcon getDefaultIcon();
	public abstract ImageIcon getPressedIcon();
	public abstract ImageIcon getDisabledIcon();
	public abstract ImageIcon getRolloverIcon();
	public abstract ActionType getActionType();
	
	enum ButtonIconType {
		
		FOLD_DEFAULT,
		FOLD_PRESSED,
		FOLD_ROLLOVER,
		
		CHECK_DEFAULT,
		CHECK_PRESSED,
		CHECK_ROLLOVER,
		
		CALL_DEFAULT,
		CALL_PRESSED,
		CALL_ROLLOVER,
		
		BET_DEFAULT,
		BET_PRESSED,
		BET_ROLLOVER,
		
		RAISE_DEFAULT,
		RAISE_PRESSED,
		RAISE_ROLLOVER
	}
	
	private static Map<ButtonIconType, Matrix> buttonMatrix = new HashMap<ButtonIconType, Matrix>();
	
	static {
		
		buttonMatrix.put(ButtonIconType.FOLD_DEFAULT, new Matrix(0,0));
		buttonMatrix.put(ButtonIconType.FOLD_ROLLOVER, new Matrix(1,0));
		buttonMatrix.put(ButtonIconType.FOLD_PRESSED, new Matrix(2,0));
		
		buttonMatrix.put(ButtonIconType.CALL_DEFAULT, new Matrix(0,1));
		buttonMatrix.put(ButtonIconType.CALL_ROLLOVER, new Matrix(1,1));
		buttonMatrix.put(ButtonIconType.CALL_PRESSED, new Matrix(2,1));
		
		buttonMatrix.put(ButtonIconType.BET_DEFAULT, new Matrix(0,2));
		buttonMatrix.put(ButtonIconType.BET_ROLLOVER, new Matrix(1,2));
		buttonMatrix.put(ButtonIconType.BET_PRESSED, new Matrix(2,2));

		buttonMatrix.put(ButtonIconType.CHECK_DEFAULT, new Matrix(0,3));
		buttonMatrix.put(ButtonIconType.CHECK_ROLLOVER, new Matrix(1,3));
		buttonMatrix.put(ButtonIconType.CHECK_PRESSED, new Matrix(2,3));

		buttonMatrix.put(ButtonIconType.RAISE_DEFAULT, new Matrix(0,4));
		buttonMatrix.put(ButtonIconType.RAISE_ROLLOVER, new Matrix(1,4));
		buttonMatrix.put(ButtonIconType.RAISE_PRESSED, new Matrix(2,4));

	}
	
	private static Point getButtonIconTypeOffset(ButtonIconType type) {
		
		if(type == null) return null;
		
		Matrix m = buttonMatrix.get(type);
		
		if(m == null) return null;
		
		return new Point(m.x * ResourceManager.ACTION_BUTTON_WIDTH, m.y * ResourceManager.ACTION_BUTTON_HEIGHT);
	
	}
	
	private static ImageIcon getButtonImageIcon(ButtonIconType type) {
		
		if (type == null) return null;
		
		Point offset = getButtonIconTypeOffset(type);
		
		return new ImageIcon(getActionButtonsImage().getSubimage(offset.x, offset.y, ResourceManager.ACTION_BUTTON_WIDTH, ResourceManager.ACTION_BUTTON_HEIGHT));
	}
}
