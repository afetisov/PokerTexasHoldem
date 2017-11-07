package client.gui;

import java.math.BigDecimal;

public class BigDecimalWrapper implements Comparable<BigDecimalWrapper>{

	private BigDecimal value;
	
	public BigDecimalWrapper(BigDecimal value) {
		this.value = value;
	}
	
	@Override
	public int compareTo(BigDecimalWrapper o) {
		if(value == null) return -1;
		return value.compareTo(o.value);
	}

	@Override
	public String toString() {
		if(value == null) return "";
		return value.toPlainString();
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
