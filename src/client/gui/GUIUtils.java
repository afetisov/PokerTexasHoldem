package client.gui;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.swing.DefaultComboBoxModel;

import commons.game.GameType;

public class GUIUtils {
	
	public static DefaultComboBoxModel getGameTypeComboBoxModel() {
		
		return new DefaultComboBoxModel(new Object [] {
				new GameTypeItem("All",null), 
				new GameTypeItem(GameType.FIXED_BETS.getName(), GameType.FIXED_BETS), 
				new GameTypeItem(GameType.NOLIMIT.getName(),GameType.NOLIMIT)});
	}
	
	public static String chips2text(BigDecimal value) {
		
		long [] nums = {1000000000L, 1000000, 1000, 1};
		
		String [] str = {"B", "M", "K", ""};
		
		if(value.compareTo(BigDecimal.ONE) < 0) {
			return decimal2text(value);
		}
		
		int a = 3; //index from nums (default = last value = 1)
		
		long b = value.longValue();
		
		//seek for proper divisor in order (1B or 1M or 1K or 1)
		for(int i = 0; i < nums.length; i++) {
			if(b > nums[i]) {
				a = i;
				break;
			}
		}
		
		BigDecimal div = null;
		
		if(nums[a] > 1) {
			div = value.divide(new BigDecimal(nums[a]), 0, RoundingMode.HALF_EVEN);
		} else {
			div = value.round(new MathContext(2, RoundingMode.HALF_EVEN));
		}
		
		String result = decimal2text(div) + str[a];
		
		return result;
	}
	
	public static String decimal2text(BigDecimal value) {
		
		if(value == null) return "";
		
		if(value.compareTo(BigDecimal.ONE) < 0) {
			return String.format("%1$,.2f", value);
		}else {
			return String.format("%1$,.0f", value);
		}
	}
}
