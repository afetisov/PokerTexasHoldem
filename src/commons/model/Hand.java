package commons.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import commons.game.poker.HandRound;

@Entity
@Table(name = "HANDS_HISTORY")
@org.hibernate.annotations.Table(appliesTo = "HANDS_HISTORY", indexes = { @Index(name = "idx_hands_history_finish_date_time", columnNames = {"finishDateTime"})})
public final class Hand extends BaseEntity {

	@Column(length = 20)
	private String board;
	
	@ManyToOne()
	@JoinTable(
			name = "HAND_TABLE", 
			joinColumns = @JoinColumn(name = "hand_id"), 
			inverseJoinColumns = @JoinColumn(name = "table_id")
	)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ForeignKey(name = "fk_hand_table_id")
	private TableInfo table;
	
	
	private Date startDateTime = new Date();
	
	private Date finishDateTime = new Date();
	
	private BigDecimal totalPot = BigDecimal.ZERO;
	
	private BigDecimal rake = BigDecimal.ZERO;
	
	private HandRound lastRound = HandRound.PREFLOP;
	
	@Column(length = 2000)
	private String log;
	
	@ElementCollection
	@JoinTable(
	name = "HAND_PLAYER",
	joinColumns = @JoinColumn(name = "hand_id")
	)
	private Set<HandPlayer> handPlayers = new HashSet<HandPlayer>();
	
	public Hand() {}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public TableInfo getTable() {
		return table;
	}

	public void setTable(TableInfo table) {
		this.table = table;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getFinishDateTime() {
		return finishDateTime;
	}

	public void setFinishDateTime(Date finishDateTime) {
		this.finishDateTime = finishDateTime;
	}

	public BigDecimal getTotalPot() {
		return totalPot;
	}

	public void setTotalPot(BigDecimal totalPot) {
		this.totalPot = totalPot;
	}

	public BigDecimal getRake() {
		return rake;
	}

	public void setRake(BigDecimal rake) {
		this.rake = rake;
	}

	public HandRound getLastRound() {
		return lastRound;
	}

	public void setLastRound(HandRound lastRound) {
		this.lastRound = lastRound;
	}

	public Set<HandPlayer> getHandPlayers() {
		return handPlayers;
	}

	
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
}
