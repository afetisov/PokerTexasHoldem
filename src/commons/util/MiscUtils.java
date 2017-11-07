package commons.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.util.Collection;

import commons.game.poker.Card;

public class MiscUtils {

	public static Short toShort(int value) {
		if(value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
			throw new InvalidParameterException("Integer value is out of bounds of Short data type");
		}
		return new Short((short)value);
	}
	
	public static String wordCase(int number, String nominative, String genitiveSingular, String genitivePlural) {
		int mod = number;
		while (mod  >= 20 ) {
			mod = mod % 20;
		}
		if(mod == 1) {
			return nominative;
		}else if(mod > 1 && mod < 5) {
			return genitiveSingular;
		}else {
			return genitivePlural;
		}
	}

	public static BigDecimal max(BigDecimal first, BigDecimal second) {
		
		if(first == null || second == null) return null;
		
		if(first.compareTo(second) < 0) return second;
		
		return first;
	}
	
	public static BigDecimal min(BigDecimal first, BigDecimal second) {
		
		if(first == null || second == null) return null;
		
		if(first.compareTo(second) > 0) return second;
		
		return first;
	}
	
	public static BigDecimal getRake(BigDecimal pot) {
				
		if(pot == null) return null;
		
		if(pot.equals(BigDecimal.ZERO)) return BigDecimal.ZERO;
		
		BigDecimal rake = pot.multiply(new BigDecimal(0.05));
		
		if(rake.compareTo(new BigDecimal(3)) >= 0) {
			rake = new BigDecimal(3);
		} else {
			if(rake.compareTo(BigDecimal.ONE) < 0) {
				rake = rake.round(new MathContext(2, RoundingMode.HALF_EVEN));
			} else {
				rake = rake.round(new MathContext(0, RoundingMode.HALF_EVEN));
			}
		}
		
		return rake;
	}
	
	public static BigDecimal getSmallBlind(BigDecimal bigBlind) {
		
		if(bigBlind == null) return null;
		
		if(bigBlind.equals(BigDecimal.ZERO)) return bigBlind;
		
		BigDecimal smallBlind = bigBlind.divide(new BigDecimal(2));
		
		int [] chips = {1, 5, 10, 25, 100};
		
		if(smallBlind.compareTo(BigDecimal.ONE) < 0) {
			
			smallBlind = smallBlind.round(new MathContext(2, RoundingMode.DOWN));
		
		} else {
			
			int maxChip = chips[0];
			
			int value = smallBlind.intValue();
			
			for(int chip : chips) {
				if(chip <= value) {
					maxChip = chip;
					continue;
				}
				break;
			}
			
			int mult = value / maxChip;
			
			smallBlind = new BigDecimal(maxChip * mult);
		}
		
		return smallBlind;
	}
	
	public static String cards2str(Collection<Card> cards) {
		
		if(cards == null || cards.isEmpty()) return null;
		
		StringBuilder sb = new StringBuilder();
	
		for(Card c : cards) {
			sb.append(c.toString()).append(" ");
		}
		
		return sb.toString().trim();
	}
	
}
