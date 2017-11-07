package client.game.gui.components;

import java.math.BigDecimal;
import javax.swing.JLabel;
import client.gui.GUIUtils;

@SuppressWarnings("serial")
public class BetAmountLabel extends JLabel {
	
	private BigDecimal amount = BigDecimal.ZERO;
		
	public BetAmountLabel() {
		updateUI();	
	}
	
	public void setAmount(BigDecimal value) {
		amount = value;
		repaint();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public String getText() {
		
		if(amount == null) return null;
		
		return GUIUtils.chips2text(amount);
	}

	/*
	@Override
	public void updateUI() {
		setUI(new BetAmounLabelUI());
	}
	*/
}
