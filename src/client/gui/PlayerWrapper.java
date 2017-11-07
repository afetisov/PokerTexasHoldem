package client.gui;

import commons.model.Player;

public class PlayerWrapper {

	private Player player;
	
	private BigDecimalWrapper balance;
	
	private DateTimeWrapper loginDate;
	
	private DateTimeWrapper createdDate;
	
	public PlayerWrapper(Player player) {
		this.player = player;
		balance = new BigDecimalWrapper(player.getBalance());
		loginDate = new DateTimeWrapper(player.getLastLogin());
		createdDate = new DateTimeWrapper(player.getCreatedDate());
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public BigDecimalWrapper getBalance() {
		return balance;
	}

	public void setBalance(BigDecimalWrapper balance) {
		this.balance = balance;
	}

	public DateTimeWrapper getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(DateTimeWrapper loginDate) {
		this.loginDate = loginDate;
	}

	public DateTimeWrapper getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTimeWrapper createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if(player == null) 
			return false;
		if(obj instanceof Player) {
			Player other = (Player) obj;
			return player.equals(other);
		}else if(obj instanceof PlayerWrapper) {
			PlayerWrapper other = (PlayerWrapper) obj;
			return player.equals(other.player);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (player == null)? super.hashCode(): player.hashCode();
	}
}
