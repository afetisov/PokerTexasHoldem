package commons.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeSet;

import commons.game.GameType;
import commons.game.TableOccupancyType;

public class TablesFilter implements Serializable {
	
	private GameType gameType;

	private byte occupancy;

	public TablesFilter() {}
	
	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	public byte getOccupancy() {
		return occupancy;
	}

	public void setOccupancy(byte occupancy) {
		this.occupancy = occupancy;
	} 
	
	public void setOccupancy(TableOccupancyType [] types) {
		
		if(types == null || types.length == 0) {
			occupancy = 0;
		}else {
		
			byte value = 0;
			
			int i = 2;
			
			TreeSet<TableOccupancyType> list = new TreeSet<TableOccupancyType>(Arrays.asList(types));
			
			for(TableOccupancyType type : TableOccupancyType.values()) {
				value |= list.contains(type)?1<<i:0;
				i--;
			}
			
			occupancy = value;
		}
	}
	
	public static boolean isSet(TableOccupancyType type, byte occupancy) {

		switch(type) {
		case EMPTY:
			return (occupancy & 0x04) > 0;
		case FULL:
			return (occupancy & 0x02) > 0;
		default:
			return (occupancy & 0x01) > 0;
		}
	}
}
