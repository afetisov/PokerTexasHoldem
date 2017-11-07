package client.gui;

import commons.model.TableInfo;

public class TableInfoWrapper {
	
	private TableInfo data;
	
	private BlindAmountItem blinds;
	
	private PlayersNumberItem numberOfPlayers;
	
	private BigDecimalWrapper averagePot;
	
	private BigDecimalWrapper averageRake;
	
	private BigDecimalWrapper totalRake;
	
	public TableInfoWrapper(TableInfo value) {
		data = value;
		
		blinds = new BlindAmountItem(data.getBigBlind());
		
		numberOfPlayers = new PlayersNumberItem(data.getStatistics().getNumberOfPlayers());
		
		averagePot = new BigDecimalWrapper(data.getStatistics().getAveragePot());
		
		averageRake = new BigDecimalWrapper(data.getStatistics().getAverageRake());
		
		totalRake = new BigDecimalWrapper(data.getStatistics().getTotalRake());
	}
	
	public TableInfo getTableInfo() {
		return data;
	}

	public BlindAmountItem getBlinds() {
		return blinds;
	}

	public void setBlinds(BlindAmountItem blinds) {
		this.blinds = blinds;
	}

	public PlayersNumberItem getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(PlayersNumberItem numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public BigDecimalWrapper getAveragePot() {
		return averagePot;
	}

	public void setAveragePot(BigDecimalWrapper averagePot) {
		this.averagePot = averagePot;
	}

	public BigDecimalWrapper getAverageRake() {
		return averageRake;
	}

	public void setAverageRake(BigDecimalWrapper averageRake) {
		this.averageRake = averageRake;
	}

	public BigDecimalWrapper getTotalRake() {
		return totalRake;
	}

	public void setTotalRake(BigDecimalWrapper totalRake) {
		this.totalRake = totalRake;
	}

	@Override
	public int hashCode() {
		return (data == null) ? super.hashCode() : data.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if(data == null)
			return false;
		if (obj == null)
			return false;
		if(obj instanceof TableInfo) {
			TableInfo other = (TableInfo) obj;
			return data.equals(other);
		}else if(obj instanceof TableInfoWrapper){
			TableInfoWrapper other = (TableInfoWrapper) obj;
			return data.equals(other.data);	
		}
		return false;
	}
	
	
}
