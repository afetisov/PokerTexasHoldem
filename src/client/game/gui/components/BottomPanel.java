package client.game.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import client.game.misc.ResourceManager;

public class BottomPanel extends JPanel {

	//components
	//private BetAmountSlider betAmountSlider = new BetAmountSlider();
	
	private Map<ActionButtonType,ActionButton> buttons = new HashMap<ActionButtonType,ActionButton>();
		
	private Map<ToggleButtonType, ToggleButton> toggleButtons = new HashMap<ToggleButtonType, ToggleButton>();
	
	private ToggleButtonType selectedToggleButtonType;

	//private BetAmountLabel betAmountLabel = new BetAmountLabel();
	
	
	//constants
	private final Font toggleButtonFont = new Font("Arial", Font.BOLD, 14);
	
	
	public BottomPanel() {
		
		super();
		
		initComponents();
	
		setOpaque(false);
	}
	
	private void initComponents() {
		
		setLayout(null);
		
		initButtons();
		//initToggleButtons();
		//initSlider();
		//initBetAmountLabel();
		/*
		addPropertyChangeListener("preferredSize", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent e) {

				Dimension size = (Dimension) e.getNewValue();
				
				if(buttons.size() > 0) {
					
					JButton button = buttons.get(ButtonType.BET);
					
					button.setBounds(size.width - 149, 1, 144,55);
					
					button = buttons.get(ButtonType.RAISE);
					
					button.setBounds(size.width - 149, 1, 144,55);
					
					betAmountLabel.setBounds(size.width - 279, 1, 130, 55);
				}
			}
		});
		*/
	}
	
	/*
	private void initSlider() {
		
		betAmountSlider.setUI(new BetAmountSliderUI(betAmountSlider));
		
		betAmountSlider.setPreferredSize(new Dimension(310, 19));
		
		betAmountSlider.setFocusable(false);
		
		betAmountSlider.setBounds(295, 30, 310, 19);
		
		add(betAmountSlider);	
	}
	*/

	private void initButtons() {
		
		for(ActionButtonType type : ActionButtonType.values()) {
			
			if (type != ActionButtonType.FOLD) continue;
			
			ActionButton button = ButtonsFactory.createButton(
					type.getActionType(),
					ActionButtonType.FOLD.getDefaultIcon(), 
					ActionButtonType.FOLD.getPressedIcon(),
					ActionButtonType.FOLD.getRolloverIcon(),
					ActionButtonType.FOLD.getDefaultIcon()
			);
			
			buttons.put(type, button);
			
			switch(type) {
			case CHECK:
			case CALL:
				button.setBounds(150, 1, 144, 55);				
				if(type == ActionButtonType.CALL) button.setVisible(false);
				break;
			case FOLD:
				button.setBounds(5, 1, 150, 40);
				break;
			case BET:
			case RAISE:
				if(type == ActionButtonType.RAISE) button.setVisible(false);				
				break;
			}
			add(button);
		}
	}

	
	/*
	private void initToggleButtons() {
		
		int x = 295;
		
		for(ToggleButtonType type : ToggleButtonType.values()) {

			ToggleButton button = ButtonsFactory.createToggleButton(type, ResourceManager.getImage(ResourceManager.CHECKBOX_OFF), 
					ResourceManager.getImage(ResourceManager.CHECKBOX_ON), ResourceManager.getImage(ResourceManager.CHECKBOX_OFF), 
					ResourceManager.getImage(ResourceManager.CHECKBOX_ON), type.getText());
			
			toggleButtons.put(type, button);
			
			add(button);
			
			button.setFont(toggleButtonFont);
			
			button.setForeground(Color.WHITE);
			
			button.addActionListener(toggleButtonListener);
			
			int w = button.getIcon().getIconWidth() + button.getIconTextGap() + button.getFontMetrics(button.getFont()).stringWidth(button.getText()) + 10;			
			
			button.setBounds(x, 5, w, 20);
			
			x += (w + 2);
		}
	}
	*/
	
	private ActionListener toggleButtonListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			ToggleButton button = (ToggleButton) e.getSource();
			
			if(button.isSelected()) {
			
				if(selectedToggleButtonType == null) {
				
					selectedToggleButtonType = button.getType();
				
				}else {
				
					toggleButtons.get(selectedToggleButtonType).setSelected(false);
					
					selectedToggleButtonType = button.getType();
				
				}
			} else {
				selectedToggleButtonType = null;
			}
		}
	};
	
	/*
	private void initBetAmountLabel() {
		
		betAmountLabel.setFont(new Font("Arial",Font.BOLD, 24));
		
		betAmountLabel.setForeground(Color.WHITE);
		
		betAmountLabel.setVerticalAlignment(JLabel.CENTER);
		
		betAmountLabel.setHorizontalAlignment(JLabel.CENTER);
		
		add(betAmountLabel);
	}

	public BetAmountSlider getBetAmountSlider() {
		return betAmountSlider;
	}

	public Map<ButtonType, ActionButton> getButtons() {
		return buttons;
	}

	public ToggleButtonType getSelectedToggleButtonType() {
		return selectedToggleButtonType;
	}

	public BetAmountLabel getBetAmountLabel() {
		return betAmountLabel;
	}

	public Map<ToggleButtonType, ToggleButton> getToggleButtons() {
		return toggleButtons;
	}

	public void setSelectedToggleButtonType(
			ToggleButtonType selectedToggleButtonType) {
		this.selectedToggleButtonType = selectedToggleButtonType;
	}
	*/
}
