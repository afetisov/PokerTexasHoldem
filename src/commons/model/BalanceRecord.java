package commons.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "BALANCE_HISTORY")
public class BalanceRecord extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@ForeignKey(name="fk_balance_history_player_id")
	private Player player;
	
	private BigDecimal amount;

	@Column(nullable = false, updatable = false)
	private Date dateCreated = new Date();

	public BalanceRecord() {}
	
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}
