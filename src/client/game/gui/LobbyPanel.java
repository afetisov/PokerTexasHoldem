package client.game.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import client.game.remote.GameRemoteService;
import client.gui.BlindAmountItem;
import client.gui.BlindsListModel;
import client.gui.GUIUtils;
import client.gui.GameTypeItem;
import client.gui.TableInfoWrapper;

import commons.game.GameClient;
import commons.game.TableOccupancyType;
import commons.log.Logger;
import commons.log.LoggingLevel;
import commons.model.TableInfo;
import commons.model.TablesFilter;

@SuppressWarnings("serial")
public class LobbyPanel extends JPanel {
	
	private static final String labelBalanceTextFormat = "Current amount: $%1$,.2f";
	
	public LobbyPanel() {
		initComponents();
		initMediator();
	}
	
	private void initComponents() {
		createTopPanel();
		
		tables = new JTable();
		
	    tables.getTableHeader().setReorderingAllowed(false);
	    
	    tables.setRowSelectionAllowed(true);
	    
	    JScrollPane jScrollPane1 = new JScrollPane();
	    jScrollPane1.setViewportView(tables);
	    
	     GroupLayout layout = new GroupLayout(this);
	     
	     this.setLayout(layout);
	     
	     layout.setHorizontalGroup(
	            layout.createParallelGroup(Alignment.LEADING)
	            	.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
	            	.addContainerGap()
	                .addGroup(layout.createParallelGroup(Alignment.LEADING, true)
	                   .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                   .addComponent(subPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                   .addComponent(jScrollPane1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                .addContainerGap())
	        );
	      layout.setVerticalGroup(
	            layout.createParallelGroup(Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	            	.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	            	.addComponent(subPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	                .addGap(10, 10, 10)
	                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
	                .addContainerGap())
	        );	
	}
	
	private void initMediator() {
		
		mediator = new LobbyPanelMediator();
	
		mediator.registerTable(tables);
		mediator.registerGameTypeFilter(gameTypeFilter);
		mediator.registerBlindsFilter(blindsFilter);
		mediator.registerHideEmptyTablesFilter(hideEmptyTables);
		mediator.registerHideFullTablesFilter(hideFullTables);
	}
	
	
	private void createTopPanel() {
		
		mainPanel = new JPanel();
		subPanel = new JPanel();
		
		labelBalance = new JLabel();
		labelBalance.setText(String.format(labelBalanceTextFormat, currentBalance));
		
	    JLabel jLabel1 = new JLabel();
	    
        gameTypeFilter = new JComboBox();
        
        JLabel jLabel2 = new JLabel();
        
        blindsFilter = new JComboBox();
        blindsFilter.setPreferredSize(new Dimension(150, 28));

        mainPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        subPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        jLabel1.setText("Game type:");
        
        mainPanel.add(labelBalance);
        
        subPanel.add(jLabel1);

        subPanel.add(gameTypeFilter);

        jLabel2.setText("Blinds:");
        subPanel.add(jLabel2);

        subPanel.add(blindsFilter);

        hideEmptyTables = new JCheckBox("Hide empty tables");
        subPanel.add(hideEmptyTables);
        
        hideFullTables = new JCheckBox("Hide full tables");
        subPanel.add(hideFullTables);
        
        btnPlayerProfile = new JButton("Profile");
        btnLogout = new JButton("Exit");
        
        btnPlayerProfile.setVisible(false);
        btnLogout.setVisible(false);
        
        mainPanel.add(btnPlayerProfile);
        mainPanel.add(btnLogout);
	}
	
	private class LobbyPanelMediator {

		private JComboBox gameTypeFilter;
		
		private JComboBox blindsFilter;
	    
	    private JTable tables;
	    
	    private TablesDataModel model;
	    
	    LobbyPanelMediator() {}
	    
	    void registerTable(JTable table) {
	    	
	    	tables = table;
	    	
	    	model = new TablesDataModel();
	    	
	    	table.setModel(model);
	    	
	    	tables.setRowSorter(new TableRowSorter<TablesDataModel>(model));	    	
	    } 
	    
	    void registerGameTypeFilter(JComboBox filter) {
	    	gameTypeFilter = filter;
	    	
	    	filter.setModel(GUIUtils.getGameTypeComboBoxModel());
	    	
	    	filter.setEditable(false);
	    	
	    	filter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new TablesDataWorker(false).execute();
				}
			});
	    }
	    
	    void registerBlindsFilter(JComboBox filter) {
	    	
	    	blindsFilter = filter;
	    	
	    	filter.setModel(new BlindsListModel());
	    
	    	filter.setEditable(false);
	    	
	    	filter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setBlindsFilter();
				}
			});
	    }
	    
	    @SuppressWarnings("unchecked")
		void setBlindsFilter() {
	    	
	    	TableRowSorter<TablesDataModel> sorter = (TableRowSorter<TablesDataModel>) tables.getRowSorter();
	    	
	    	BlindAmountItem item = (BlindAmountItem) blindsFilter.getSelectedItem();
	    	
	    	if(item.getBlind() == null) {
	    		sorter.setRowFilter(null);
	    	}else {
	    		
	    		sorter.setRowFilter(new RowFilter<TablesDataModel, Integer>(){

					@Override
					public boolean include(
							RowFilter.Entry<? extends TablesDataModel, ? extends Integer> entry) {
						
						BlindAmountItem item = (BlindAmountItem) blindsFilter.getSelectedItem();
						
						TableInfo table = model.getRow(entry.getIdentifier());

						if(table.getBigBlind().equals(item.getBlind())) return true;
						
						return false;
					}
	    		});
	    	}
	    }
	    
	    void registerHideEmptyTablesFilter(JCheckBox filter) {
	    	
	    	hideEmptyTables = filter;
	    	
	    	filter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new TablesDataWorker(false).execute();					
				}
			});
	    }
	    
	    void registerHideFullTablesFilter(JCheckBox filter) {
	    	
	    	hideFullTables = filter;
	    
	    	filter.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new TablesDataWorker(false).execute();					
				}
			});
	    }
	    
	    TablesFilter getTablesFilter() {
	    	
	    	TablesFilter filter = new TablesFilter();
	    	
	    	GameTypeItem gameType = (GameTypeItem) gameTypeFilter.getSelectedItem();
	    	
	    	filter.setGameType(gameType.getGameType());
	    	
	    	filter.setOccupancy(getOccupancyList());
	    	
	    	return filter;
	    }
	    
	    TableOccupancyType[] getOccupancyList() {
	    	List<TableOccupancyType> list = new ArrayList<TableOccupancyType>();
	    	
	    	list.add(TableOccupancyType.NOT_FULL);
	    	
	    	if(!hideEmptyTables.isSelected()) {
	    		list.add(TableOccupancyType.EMPTY);
	    	}
	    	
	    	if(!hideFullTables.isSelected()) {
	    		list.add(TableOccupancyType.FULL);
	    	}
	    	
	    	return list.toArray(new TableOccupancyType[] {});
	    }
	}
	
	public class TablesDataModel extends AbstractTableModel { 

		private String [] headers = {"Table #", "Seats", "Game type", "Blinds", "Avg. pot","Hands/hr"};
		
		private Vector<TableInfoWrapper> data = new Vector<TableInfoWrapper>();
		
		@Override
		public int getColumnCount() {
			return headers.length;
		}
		
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}

		@Override
		public String getColumnName(int column) {
			return headers[column];
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			
			TableInfoWrapper rowItem = null;
				
			synchronized (data) {

				if(data.size() == 0) return "";
				
				rowItem = data.get(row);
			}

			if(rowItem == null) return "";
			
			switch(column) {
			case 0:
				return rowItem.getTableInfo().getId();
			case 1:
				return rowItem.getNumberOfPlayers();
			case 2:
				return rowItem.getTableInfo().getGameType().getName();
			case 3:
				return rowItem.getBlinds();
			case 4:
				return rowItem.getAveragePot();
			case 5:
				return rowItem.getTableInfo().getStatistics().getHandsPerHour();
			}
			return null;
		}

		@Override
		public Class<?> getColumnClass(int column) {			
			return getValueAt(0, column).getClass();
		}
		
		public synchronized void loadData(TablesFilter filter, boolean update) {
			synchronized (data) {
				try {
					
					List<TableInfo> list = GameRemoteService.getGameServer().getTables(filter);
					
					data.clear();
					
					if(list != null) {
						for(TableInfo t : list) {
							data.add(new TableInfoWrapper(t));
						}
					}
					
					if(update) {
						if(data.size() > 0 ) {
							fireTableRowsUpdated(0, data.size() - 1);
						}
					}else {
						fireTableDataChanged();
					}
				}catch(Exception exc) {
					Logger.write(LoggingLevel.ERROR, "Failed to reload lobby data", exc);
				}
			}
		}
		
		public TableInfo getRow(int rowIndex) {
			synchronized (data) {

				if(data.size() == 0 || data.size() <= rowIndex) return null;
				
				return data.get(rowIndex).getTableInfo();
			}
		}
	}

    private class TablesDataWorker extends SwingWorker<Void,Void> {
    	
    	private boolean update;
    	
    	public TablesDataWorker(boolean update) {
    		this.update = update;
    	}

		@Override
		protected Void doInBackground() throws Exception {
			
			TablesDataModel model1 = (TablesDataModel) tables.getModel();
			model1.loadData(mediator.getTablesFilter(), update);
			
			if(!update) {
				BlindsListModel model2 = (BlindsListModel) blindsFilter.getModel();
				model2.loadData(mediator.getTablesFilter());
			}
			
			return null;
		}
    }

	public void loadData(boolean update) {
		new TablesDataWorker(update).execute();
	}

	public void setCurrentBalance(BigDecimal balance) {
		
		currentBalance = balance;
		
		labelBalance.setText("Current amount: " + (balance == null ? "" : String.format("$%1$,.2f",balance)));
	}

	public GameClient getGameClient() {
		return gameClient;
	}

	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	public void addTablesMouseListener(MouseListener listener) {
		tables.addMouseListener(listener);
	}

	private GameClient gameClient;
	
    private BigDecimal currentBalance = BigDecimal.ZERO;
    
    private LobbyPanelMediator mediator;

	private JLabel labelBalance;
	
	private JPanel mainPanel;
	
	private JPanel subPanel;
	
	private JTable tables;

    private JComboBox gameTypeFilter;
    
    private JComboBox blindsFilter;
    
    private JCheckBox hideEmptyTables;
    
    private JCheckBox hideFullTables;
    
    private JButton btnPlayerProfile;
    
    private JButton btnLogout;
}
