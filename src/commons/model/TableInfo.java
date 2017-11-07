package commons.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import commons.game.GameType;

@Entity
@Table(name = "TABLES")
@org.hibernate.annotations.Table(appliesTo="TABLES", indexes = {@Index(name = "idx_tables_game_type", columnNames = {"gameType"})})
public class TableInfo extends BaseEntity {
	
	@Column(nullable = false)
	private GameType gameType;
	
	@Column(nullable = false)
	private BigDecimal bigBlind = BigDecimal.ZERO;
	
	@Column(nullable = false)
	private Integer capSize = 3;
	
	@Column(nullable = false)
	private BigDecimal minBayin;
	
	private BigDecimal maxBayin;
	
	@Column(length = 50)
	private String password;
	
	@Column(nullable = false, updatable = false)
	private Date createdDate = new Date();
	
	private Date lastModified;
	
	private Long gameId;
	
	@Column(nullable = false)
	private boolean deleted;
	
	@Embedded
	private TableStat statistics;
	
	
	public TableInfo() {}
	
	
	public TableInfo(GameType type, BigDecimal blind, Integer capSize, BigDecimal minBuyin, BigDecimal maxBuyin, String pswd) {
		
		this.gameType = type;
		
		this.bigBlind = blind;
		
		this.capSize = capSize;
		
		this.password = pswd;
		
		this.minBayin = minBuyin;
		
		this.maxBayin = maxBuyin;
		
		this.statistics = new TableStat();
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public TableStat getStatistics() {
		return statistics;
	}

	public void setStatistics(TableStat statistics) {
		this.statistics = statistics;
	}
	
	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public BigDecimal getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(BigDecimal bigBlind) {
		this.bigBlind = bigBlind;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Integer getCapSize() {
		return capSize;
	}

	public void setCapSize(Integer capSize) {
		this.capSize = capSize;
	}

	public BigDecimal getMinBayin() {
		return minBayin;
	}

	public void setMinBayin(BigDecimal minBayin) {
		this.minBayin = minBayin;
	}

	public BigDecimal getMaxBayin() {
		return maxBayin;
	}

	public void setMaxBayin(BigDecimal maxBayin) {
		this.maxBayin = maxBayin;
	}

	@Override
	public String toString() {
		return gameType.toString() + ";" + bigBlind.toString() + ";" + password;
	}
}
