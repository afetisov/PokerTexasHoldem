package client.admin.gui.players;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import client.admin.gui.CardPanel;
import client.admin.remote.AdminRemoteService;
import client.gui.PlayerWrapper;

import commons.model.Player;
import commons.model.PlayersFilter;
//import commons.util.MiscUtils;

@SuppressWarnings("serial")
public class PlayersPanel extends JPanel implements CardPanel {

    public PlayersPanel(JFrame frame) {
    	mainWindow = frame;
        initComponents();
        initMediator();
    }
    
    private void initComponents() {

    	selectBlockedUsers = new JCheckBox("Show blocked users");
    	selectBlockedUsers.setSelected(true);
    	
    	searchUser = new JTextField();
        searchUser.setPreferredSize(new java.awt.Dimension(130, 28));
        
        btnSearch = new JButton();
        btnSearch.setText("Search");
        
        btnAdd = new JButton();
        btnAdd.setText("New");
        btnAdd.setPreferredSize(new Dimension(160,28));
        
        btnDelete = new JButton();
        btnDelete.setText("Delete");
        btnDelete.setPreferredSize(new Dimension(160,28));        

        btnDeposit = new JButton();
        btnDeposit.setText("Deposit money");
        btnDeposit.setPreferredSize(new Dimension(160,28));
                
        btnWithdraw = new JButton();
        btnWithdraw.setText("Withdrow money");
        btnWithdraw.setPreferredSize(new Dimension(160,28));
        
        JLabel jLabel1 = new JLabel();
        jLabel1.setText("Search:");
      

        players = new JTable();
        
        jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(players);
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                    .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDeposit, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnWithdraw, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addGap(10)
                        .addComponent(selectBlockedUsers)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(searchUser, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch)
                            .addComponent(selectBlockedUsers))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40)
                        .addComponent(btnAdd)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeposit)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnWithdraw)))
                .addContainerGap())
        );
    }
	
    private void initMediator() {
    	mediator = new UsersPanelMediator();
    	mediator.registerTable(players);
    	mediator.registerCreateButton(btnAdd);
    	mediator.registerDeleteButton(btnDelete);
    	mediator.registerDepositButton(btnDeposit);
    	mediator.registerWithdrawButton(btnWithdraw);
    	mediator.registerSearchButton(btnSearch);
    	mediator.registerSearchField(searchUser);
    	mediator.registerBlockedCheckBox(selectBlockedUsers);
    }
    
    private class UsersPanelMediator {
    	
    	private JTextField searchField;
    	
    	private JTable table;
    	
    	private PlayersDataModel model;
    	
    	private JCheckBox selectBlockedUsers;
    	
	    private final String initialSearchText = "# of player";
	    
    	UsersPanelMediator() {}
    	
    	void registerTable(JTable table) {
    		this.table = table;
    		
    		model = new PlayersDataModel(); 
            
    		players.setModel(model);
            
            players.setColumnSelectionAllowed(true);  
            players.getTableHeader().setReorderingAllowed(false);
            players.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            table.setRowSorter(new TableRowSorter<PlayersDataModel>(model));            
    	}

    	void registerCreateButton(JButton button) {
    		button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					PlayerEditorDialog dlg = new PlayerEditorDialog(mainWindow, true);
					
					if(!dlg.isCanceled()) {
						new UsersDataWorker(false).execute();
					}
				}
			});
    	}
    	
    	void registerDeleteButton(JButton button) {
    		button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int rowsCount = table.getSelectedRowCount();
					if(rowsCount == 0) return;
					String [] options = new String [] {"Yes", "No"};
					int dlgResult = JOptionPane.showOptionDialog(
							mainWindow,
							String.format("Delete %1$d %2$s?",rowsCount, "user(s)"), 
							"Delete user(s)", 
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, 
							null, 
							options, 
							options[1]);
					
					if (dlgResult == JOptionPane.NO_OPTION) return;
					
					try {
						int rows[] = table.getSelectedRows();
						for(int i = 0; i < rows.length; i++) {
							rows[i] = table.convertRowIndexToModel(rows[i]);
						}
						
						List<Player> list = model.getList(rows);
						
						AdminRemoteService.getAdminServer().deletePlayers(list);
						
						model.remove(list);
						
					}catch(Exception exc) {
						exc.printStackTrace();
					}
				}
			});
    	}
    	
    	void registerDepositButton(JButton button) {
    		button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(table.getSelectedRowCount() == 0 || table.getSelectedRows().length > 1) {
						JOptionPane.showMessageDialog(null, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					int row = table.convertRowIndexToModel(table.getSelectedRow());
					Player p = model.getRow(row);
					BaseBalanceDialog dlg = new DepositDialog(mainWindow, true, p);
					if(!dlg.isCanceled()) {
						new UsersDataWorker(true).execute();
					}
				}
			});
    	}

    	void registerWithdrawButton(JButton button) {
    		button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(table.getSelectedRowCount() == 0 || table.getSelectedRows().length > 1) {
						JOptionPane.showMessageDialog(null, "Please select a user", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					int row = table.convertRowIndexToModel(table.getSelectedRow());
					Player p = model.getRow(row);
					BaseBalanceDialog dlg = new WithdrawDialog(mainWindow, true, p);
					if(!dlg.isCanceled()) {
						new UsersDataWorker(true).execute();
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
    	
    	void registerSearchButton(JButton button) {
    		button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					search();
				}
			});
    	}
    	
    	void registerBlockedCheckBox(JCheckBox component) {
    		
    		selectBlockedUsers = component;
    		
    		component.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new UsersDataWorker(false).execute();
				}
			});
    	}
    	
    	@SuppressWarnings("unchecked")
		final void search() {
	    	TableRowSorter<PlayersDataModel> sorter = (TableRowSorter<PlayersDataModel>) table.getRowSorter();
	    	final String playerID = searchField.getText();
	    	if(playerID == null 
	    			|| playerID.isEmpty() 
	    			|| playerID.equals(initialSearchText)) {
	    		sorter.setRowFilter(null);
	    	}else {
	    		Long id = null;
	    		try {
	    			id = Long.valueOf(playerID);
	    			sorter.setRowFilter(RowFilter.numberFilter(ComparisonType.EQUAL, id, 0));
	    		}catch(NumberFormatException exc) {
	    			sorter.setRowFilter(null);
	    			JOptionPane.showMessageDialog(null, "Please enter a number of an user.", "Error", JOptionPane.OK_OPTION);
	    			searchField.setText(null);	    		
	    		}
	    			 
	    	}					
    	}
    	
	    final void setInitialText(JTextField field) {
			field.setForeground(new Color(200,200,200));
			field.setText(initialSearchText);	    	
	    }
	    
    	PlayersFilter getPlayersFilter() {
    		PlayersFilter filter = new PlayersFilter();
    		filter.setBlockedUsers(selectBlockedUsers.isSelected());
    		return filter;
    	}
    }
    
    private class PlayersDataModel extends AbstractTableModel {

    	private Vector<PlayerWrapper> data = new Vector<PlayerWrapper>();
    	
    	private String [] headers = new String [] {"User #","Name", "Amount", "Last login", "Date created", "Blocked", "Comments"};
    	
		@Override
		public int getColumnCount() {
			return headers.length;
		}
		
		@Override
		public String getColumnName(int column) {
			return headers[column];
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex > 4)? true : false;
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(data.size() == 0) return "";
			
			PlayerWrapper playerWrapper = data.get(rowIndex);
			
			switch(columnIndex) {
			case 0:
				return playerWrapper.getPlayer().getId();
			case 1:
				return playerWrapper.getPlayer().getName();
			case 2:
				return playerWrapper.getBalance();
			case 3:
				return playerWrapper.getLoginDate() == null?"":playerWrapper.getLoginDate();
			case 4:
				return playerWrapper.getCreatedDate();
			case 5:
				return playerWrapper.getPlayer().isBlocked();
			case 6:
				return playerWrapper.getPlayer().getComment() == null?"":playerWrapper.getPlayer().getComment();
			}
			
			return null;
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		}
    	
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			
			if(data.size() == 0 || data.size() <= rowIndex) return;
			
			PlayerWrapper pw = data.get(rowIndex);
			
			switch(columnIndex) {
			case 4:
				pw.getPlayer().setBlocked((Boolean) value);
				break;
			case 5:
				pw.getPlayer().setComment((String) value);
				break;
			default:
				return;
			}

			try {
				AdminRemoteService.getAdminServer().savePlayer(pw.getPlayer());
			}catch(Exception exc) {
				exc.printStackTrace();
			}					
		}

		public synchronized void loadData(PlayersFilter filter, boolean update) {
			try {
				List<Player> list = AdminRemoteService.getAdminServer().getPlayers(filter);
				
				data.clear();
				
				if(list != null) {
					for(Player p : list) {
						data.add(new PlayerWrapper(p));
					}
				}
				if(update) {
					if(data.size() > 0) {
						fireTableRowsUpdated(0, data.size() - 1);
					}
				}else {
					fireTableDataChanged();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		public List<Player> getList(int [] rowIndeces){
			
			LinkedList<Player> list = new LinkedList<Player>();
			
			if(data.size() == 0) return list;
			
			for(int index : rowIndeces) {
				list.add(data.get(index).getPlayer());
			}
			
			return list;
		}
		
		public void remove(List<Player> list) {
			data.removeAll(list);
			fireTableDataChanged();
		}
		
		public Player getRow(int rowIndex) {
			
			if(data.size() == 0 || data.size() <= rowIndex) return null;
			
			return data.get(rowIndex).getPlayer();
		}
    }
    
    private class UsersDataWorker extends SwingWorker<Void, Void> {

    	private boolean update;
    	
    	public UsersDataWorker(boolean update) {
    		this.update = update;
    	}
    	
		@Override
		protected Void doInBackground() throws Exception {
			PlayersDataModel model = (PlayersDataModel) players.getModel();
			model.loadData(mediator.getPlayersFilter(), update);
			return null;
		}
    }
    
    public void loadData(boolean update) {
    	new UsersDataWorker(update).execute();
    }
    
    //the reference to main application window
	private JFrame mainWindow;
	
	//the reference to mediator
	private UsersPanelMediator mediator;
    
    //the bunch of users screen components
    
	private JCheckBox selectBlockedUsers;
	
    private JButton btnAdd;
    
    private JButton btnDelete;
    
    private JButton btnDeposit;
    
    private JButton btnSearch;
    
    private JButton btnWithdraw;
    
    private JScrollPane jScrollPane1;
    
    private JTextField searchUser;
    
    private JTable players;
}
