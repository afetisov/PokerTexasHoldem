package client.gui;

import commons.game.GameType;

public class GameTypeItem {

	private String name;
	
	private GameType type;
	
	public GameTypeItem(String name, GameType type) {
		this.name = name;
		this.type = type;
	}
	
	public String toString() {
		return name;
	}
	
	public GameType getGameType() {
		return type;
	}
}
