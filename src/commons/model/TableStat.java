package commons.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Immutable;

@Embeddable
@Immutable
public final class TableStat implements Serializable {
	
	private BigDecimal averagePot = BigDecimal.ZERO;

	private BigDecimal averageRake = BigDecimal.ZERO;
	
	private BigDecimal totalRake = BigDecimal.ZERO;
	
	private Short handsPerHour = 0;
	
	private Short numberOfPlayers = 0;

	public TableStat() {}
	
	public BigDecimal getAveragePot() {
		return averagePot;
	}

	public void setAveragePot(BigDecimal averagePot) {
		this.averagePot = averagePot;
	}

	public BigDecimal getAverageRake() {
		return averageRake;
	}

	public void setAverageRake(BigDecimal averageRake) {
		this.averageRake = averageRake;
	}
	
	public BigDecimal getTotalRake() {
		return totalRake;
	}

	public void setTotalRake(BigDecimal totalRake) {
		this.totalRake = totalRake;
	}

	public Short getHandsPerHour() {
		return handsPerHour;
	}

	public void setHandsPerHour(Short handsPerHour) {
		this.handsPerHour = handsPerHour;
	}

	public Short getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(Short numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
}
