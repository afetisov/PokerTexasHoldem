package server.dao;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import commons.model.Hand;
import commons.model.TableInfo;
import commons.model.TablesFilter;

public interface TablesDAO {

	List<TableInfo> getDeletedOrNewTables() throws Exception;
	
	List<TableInfo> getActiveTables() throws Exception;
	
	void resetTablesInfo() throws Exception;
	
	void updateTableOccupancy(Long tableID, short occupancy) throws Exception;
	
	void updateTableGameID(Long tableID, Long gameID) throws Exception;
	
	void removeTables(List<TableInfo> tables) throws Exception;
	
	List<TableInfo> getTables(TablesFilter filter, boolean game) throws Exception;
	
	List<BigDecimal> getBlinds(TablesFilter filter, boolean game) throws Exception;
	
}
