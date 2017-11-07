package client.admin.gui.tables;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import client.admin.gui.CardPanel;
import client.admin.remote.AdminRemoteService;
import client.gui.BlindAmountItem;
import client.gui.BlindsListModel;
import client.gui.GUIUtils;
import client.gui.GameTypeItem;
import client.gui.TableInfoWrapper;

import commons.game.TableOccupancyType;
import commons.model.TableInfo;
import commons.model.TablesFilter;
import commons.util.MiscUtils;

@SuppressWarnings("serial")
public class TablesPanel extends JPanel implements CardPanel{
		
	public TablesPanel(JFrame frame) {
		mainWindow = frame;		
        initComponents();  
        initMediator();
	}
	
	private void initComponents() {

		createTopPanel();

		createRightPanel();
		
	    tables = new JTable();
	    
	    tables.getTableHeader().setReorderingAllowed(false);
	    tables.setRowSelectionAllowed(true);
	    
	    jScrollPane1 = new JScrollPane();
	    jScrollPane1.setViewportView(tables);	     
	    
	     GroupLayout layout = new GroupLayout(this);
	     this.setLayout(layout);
	     
	     layout.setHorizontalGroup(
	            layout.createParallelGroup(Alignment.LEADING)
	            	.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
	            	.addContainerGap()
	                .addGroup(layout.createParallelGroup(Alignment.LEADING, true)
	                   .addComponent(topPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
	                    	.addContainerGap()
	                        .addComponent(jScrollPane1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(rightPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
	                .addContainerGap())
	        );
	      layout.setVerticalGroup(
	            layout.createParallelGroup(Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	            	.addComponent(topPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	                .addGap(10, 10, 10)
	                .addGroup(layout.createParallelGroup(Alignment.LEADING)
	                	.addGroup(layout.createSequentialGroup()
	                			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
	                			.addContainerGap())
	                	.addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
	        );
	}

	private void createTopPanel() {
		
	    topPanel = new JPanel();
	    
	    JLabel jLabel1 = new JLabel();
	    
        gameTypeFilter = new JComboBox();
        
        JLabel jLabel2 = new JLabel();
        
        blindsFilter = new JComboBox();
        blindsFilter.setPreferredSize(new Dimension(150, 28));
        
        JLabel jLabel3 = new JLabel();
        
        searchField = new JTextField();
        
        btnSearch = new JButton();

        topPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        jLabel1.setText("Game type:");
        topPanel.add(jLabel1);

        topPanel.add(gameTypeFilter);

        jLabel2.setText("Blinds:");
        topPanel.add(jLabel2);

        topPanel.add(blindsFilter);

        jLabel3.setText("Search table:");
        topPanel.add(jLabel3);

        searchField.setPreferredSize(new Dimension(70, 28));
        topPanel.add(searchField);

        btnSearch.setText("Search");
        btnSearch.setPreferredSize(new Dimension(70, 28));
        topPanel.add(btnSearch);

	}
	
	private void createRightPanel() {
			rightPanel = new JPanel();
			
	       GridBagConstraints gridBagConstraints;

	        JLabel jLabel3 = new JLabel();
	        JScrollPane jScrollPane2 = new JScrollPane();
	        
	        occupancyFilter = new JList();
	        create = new JButton();
	        delete = new JButton();

	        rightPanel.setLayout(new GridBagLayout());

	        jLabel3.setText("Show tables:");
	        gridBagConstraints = new GridBagConstraints();
	        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
	        rightPanel.add(jLabel3, gridBagConstraints);
	        
	        occupancyFilter.setVisibleRowCount(3);
	        jScrollPane2.setViewportView(occupancyFilter);

	        create.setText("Create");

	        delete.setText("Delete");		
	        
	        GroupLayout layout = new GroupLayout(rightPanel);
	        rightPanel.setLayout(layout);
	        
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(Alignment.LEADING)
	            .addGroup(Alignment.LEADING,layout.createSequentialGroup()
	                .addComponent(jLabel3))
	            .addGroup(layout.createSequentialGroup()
	                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
	            .addGroup(layout.createSequentialGroup()
	                .addComponent(create))
	            .addGroup(layout.createSequentialGroup()
	                .addComponent(delete))
	        );
	        
	        layout.setVerticalGroup(
	            layout.createParallelGroup(Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addGap(10, 10, 10)
	                .addComponent(jLabel3)
	                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	                .addComponent(create)
	                .addComponent(delete)));
		
	}

	private void initMediator() {

		mediator = new TablesPanelMediator();
        
        mediator.registerTable(tables);
        mediator.registerGameTypeFilter(gameTypeFilter);
        mediator.registerBlindsFilter(blindsFilter);
        mediator.registerOccupancyFilter(occupancyFilter);
        mediator.registerCreateButton(create);
        mediator.registerDeleteButton(delete);
        mediator.registerSearchButton(btnSearch);
        mediator.registerSearchField(searchField);
		
	}
	
	private class TablesPanelMediator {

		private JComboBox blindsFilter;
	    
	    private JComboBox gameTypeFilter;
	    
	    private JTextField searchField;
	    
	    private JList occupancyFilter;
	    
	    private JTable table;
	    
	    private TablesDataModel model;
	    
	    private final String initialSearchText = "Table #";

	    
	    TablesPanelMediator() {}
	    
	    void registerTable(JTable table) {
	    	this.table = table;
	    	model = new TablesDataModel();
	    	table.setModel(model);
	    	
	    	table.setRowSorter(new TableRowSorter<TablesDataModel>(model));
	    	
	    	this.table.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						JTable tbl = (JTable) e.getSource();
						TablesDataModel model = (TablesDataModel) tbl.getModel();
						
						TableEditorDialog dlg = new TableEditorDialog(mainWindow, true, model.getRow(tbl.convertRowIndexToModel(tbl.getSelectedRow())));
					}else{
						super.mouseClicked(e);
					}
				}	    		
			});
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
	    	TableRowSorter<TablesDataModel> sorter = (TableRowSorter<TablesDataModel>) table.getRowSorter();
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
	    	    
	    void registerOccupancyFilter(JList filter) {
	    	
	    	occupancyFilter = filter;
	        
	    	filter.setModel(new TableOccupancyListModel());
	        
	    	filter.setSelectionInterval(0, 2);
	        
	    	filter.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					new TablesDataWorker(false).execute();
				}
			});
	    }
	    
	    void registerCreateButton(JButton btn) {
	    	
	    	btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					TableEditorDialog dlg = new TableEditorDialog(mainWindow, true, null);

					if(!dlg.isCanceled()) {
						new TablesDataWorker(false).execute();
					}
				}
			});
	    }
	    
	    void registerDeleteButton(JButton btn) {
	    	
	    	btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int rowsCount = table.getSelectedRowCount();
					
					if(rowsCount == 0) return;
					
					String [] options = new String [] {"Yes", "No"};
					
					int dlgResult = JOptionPane.showOptionDialog(
							mainWindow, 
							String.format("Delete %1$d %2$s?",rowsCount, "table(s)"), 
							"Delete table(s)", 
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, 
							null, 
							options, 
							options[1]);
					
					if (dlgResult == JOptionPane.NO_OPTION) return;
					
					try {
						
						int [] rows = table.getSelectedRows();
						
						for(int i = 0; i < rows.length;i++) {
							rows[i] = table.convertRowIndexToModel(rows[i]);
						}
						
						List<TableInfo> list = model.getList(rows);
						
						AdminRemoteService.getAdminServer().deleteTables(list);
						
						model.remove(list);
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
	    }
	    
	    void registerSearchField(JTextField field) {
	    	
	    	searchField = field;
	    	
	    	setInitialText(field);
	    	
	    	field.addFocusListener(new FocusAdapter() {

				@Override
				public void focusGained(FocusEvent e) {
					JTextField field = (JTextField) e.getSource();
					
					if(field.getText() != null && field.getText().equals(initialSearchText)) {
						
						field.setForeground(Color.BLACK);
						
						field.setText(null);
					}
					
					super.focusGained(e);
				}

				@Override
				public void focusLost(FocusEvent e) {
					JTextField field = (JTextField) e.getSource();
					
					if(field.getText() == null || field.getText().isEmpty()) {
						
						setInitialText(field);
					}
					
					super.focusLost(e);
				}
			});
	    	
	    	field.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						search();
					}
				}	    		
			});
	    }
	    
	    @SuppressWarnings("unchecked")
		final void search() {
	    	TableRowSorter<TablesDataModel> sorter = (TableRowSorter<TablesDataModel>) table.getRowSorter();
	    	
	    	if(searchField.getText() == null 
	    			|| searchField.getText().isEmpty() 
	    			|| searchField.getText().equals(initialSearchText)) {
	    		sorter.setRowFilter(null);
	    	}else {
	    		
	    		Integer id = null;
	    		
	    		try {
	    		
	    			id = Integer.valueOf(searchField.getText());
	    			
	    			sorter.setRowFilter(RowFilter.numberFilter(ComparisonType.EQUAL, id, 0));
	    		
	    		}catch(NumberFormatException e) {
	    		
	    			sorter.setRowFilter(null);
	    			
	    			JOptionPane.showMessageDialog(null, "Table # must be an integer", "Error", JOptionPane.OK_OPTION);
	    			
	    			searchField.setText(null);
	    		}
	    	}					
	    }
	    
	    final void setInitialText(JTextField field) {
			field.setForeground(new Color(200,200,200));
			field.setText(initialSearchText);	    	
	    }
	    
	    void registerSearchButton(JButton btn) {
	    	
	    	btn.addActionListener(new ActionListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent arg0) {
					search();
				}
			});
	    }
	    
	    TablesFilter getTablesFilter() {
	    	
	    	TablesFilter filter = new TablesFilter();
	    	
	    	GameTypeItem gameType = (GameTypeItem) gameTypeFilter.getSelectedItem();
	    	filter.setGameType(gameType.getGameType());
	    	
	    	Object [] list = occupancyFilter.getSelectedValues();
	    	filter.setOccupancy(Arrays.copyOf(list, list.length, TableOccupancyType[].class));
	    	
	    	return filter;
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

    //data models   	
	private class TableOccupancyListModel extends AbstractListModel {

		TableOccupancyType[] list = TableOccupancyType.values();
		
		@Override
		public Object getElementAt(int index) {
			return list[index];
		}

		@Override
		public int getSize() {
			return list.length;
		}
	}
	
	private class TablesDataModel extends AbstractTableModel { 

		private String [] headers = {"Table #", "Seats", "Game type", "Blinds", "Avg. pot", "Avg. rake", "Total rake","Hands/hr"};
		
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
			
			if(data.size() == 0) return "";
			
			TableInfoWrapper rowItem = data.get(row);
			
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
				return rowItem.getAverageRake();
			case 6:
				return rowItem.getTotalRake();
			case 7:
				return rowItem.getTableInfo().getStatistics().getHandsPerHour();
			}
			return null;
		}

		@Override
		public Class<?> getColumnClass(int column) {			
			return getValueAt(0, column).getClass();
		}
		
		public synchronized void loadData(TablesFilter filter, boolean update) {
			try {
				List<TableInfo> list = AdminRemoteService.getAdminServer().getTables(filter);
				
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
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		
		public List<TableInfo> getList(int [] rowIndeces){
			LinkedList<TableInfo> list = new LinkedList<TableInfo>();
			
			if(data.size() == 0) return list;
			
			for(int index : rowIndeces) {
				list.add(data.get(index).getTableInfo());
			}
			
			return list;
		}
		
		public void remove(List<TableInfo> list) {
			data.removeAll(list);
			
			fireTableDataChanged();
		}
		
		public TableInfo getRow(int rowIndex) {
			if(data.size() == 0 || data.size() <= rowIndex) return null;
			return data.get(rowIndex).getTableInfo();
		}
	}

	public void loadData(boolean update) {
		new TablesDataWorker(update).execute();
	}
	
	//the reference to main application window
	private JFrame mainWindow;
	
	//the reference to GUI mediator object
	private TablesPanelMediator mediator;

	
	//the bunch of tables screen components
    private JComboBox gameTypeFilter;
    
    private JComboBox blindsFilter;

    private JButton btnSearch;
    
    private JTextField searchField;

    private JButton create;

    private JButton delete;

    private JList occupancyFilter;

    private JPanel topPanel;
    
	private JPanel rightPanel;
    
	private JScrollPane jScrollPane1;
    
	private JTable tables;
}
