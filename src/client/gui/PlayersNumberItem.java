package client.gui;

import commons.game.TableOccupancyType;

public class PlayersNumberItem implements Comparable<PlayersNumberItem>{

	private String name;
	
	private Short numberOfPlayers;
	
	public PlayersNumberItem(Short value) {
		numberOfPlayers = value;
		name = "" + value + "/" + TableOccupancyType.MAX_PLAYERS;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(Short numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	@Override
	public int compareTo(PlayersNumberItem o) {
		return this.numberOfPlayers.compareTo(o.numberOfPlayers);
	}

	@Override
	public String toString() {
		return name;
	}
}
