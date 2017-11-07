package client.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BlindAmountItem implements Comparable<BlindAmountItem>{
	private BigDecimal blind;
	private String name;
	
	public BlindAmountItem(BigDecimal blind) {
		this.blind = blind;
		name = getName();
	}

	public BlindAmountItem(BigDecimal blind, String name) {
		this.blind = blind;
		this.name = name;
	}

	private String getName() {
		return blind == null?null: blind.toPlainString() + "-" + blind.multiply(BigDecimal.valueOf(2.0)).setScale(2, RoundingMode.HALF_UP).toPlainString();
	}

	@Override
	public String toString() {
		return name;
	}

	public BigDecimal getBlind() {
		return blind;
	}

	@Override
	public int compareTo(BlindAmountItem o) {
		return blind.compareTo(o.blind);
	}

}
