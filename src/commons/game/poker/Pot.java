package commons.game.poker;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Pot {
	
	private BigDecimal amount = BigDecimal.ZERO;
	
	private Set<Player> players = new HashSet<Player>();

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
}
