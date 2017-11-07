package client.game.gui.components;

import java.math.BigDecimal;

import javax.swing.JSlider;

@SuppressWarnings("serial")
public class BetAmountSlider extends JSlider {
	
	private BigDecimal minAmount;
	
	private BigDecimal maxAmount;
	
	private BigDecimal incAmount;
	

	public BetAmountSlider() {
		super();
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	public BigDecimal getIncAmount() {
		return incAmount;
	}

	public void setIncAmount(BigDecimal incAmount) {
		this.incAmount = incAmount;
	}

	public BigDecimal getCurrentAmount() {
		
		if(getValue() == getMaximum()) return maxAmount;

		if(getValue() ==  getMinimum()) return minAmount;
		
		return minAmount.add(incAmount.multiply(new BigDecimal(getValue())));
	}
}
