package client.admin;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.rmi.RemoteException;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import client.admin.gui.CardPanel;
import client.admin.gui.PasswordEditorDialog;
import client.admin.gui.players.PlayersPanel;
import client.admin.gui.tables.TablesPanel;
import client.admin.remote.AdminRemoteService;
import client.gui.LoginPanel;

public class AdminClientApplication extends JFrame{

	private static final long serialVersionUID = 2715324565738557357L;

	enum DisplayMode {
		LOGIN,
		USERS,
		TABLES
	}
	
	private DisplayMode currentMode = DisplayMode.LOGIN;
	
	private JPanel screens;
	
	private TablesPanel tablesPanel;
	
	private PlayersPanel playersPanel;
	
	private PasswordEditorDialog pswdDialog;
	
	private Map<DisplayMode, Timer> timers = new HashMap<DisplayMode, Timer>();
	
	private Map<DisplayMode, Boolean> isFirstTime = new HashMap<DisplayMode, Boolean>();
	
	private Map<DisplayMode, CardPanel> panels = new HashMap<DisplayMode, CardPanel>();
	
	public AdminClientApplication () {
		createGUI();
	}

	
	public static void main(String[] args) {
		
		if(System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new AdminClientApplication();
			}
		});

	}
	
	private void createGUI() {

		updateWindowTitle();
		this.setSize(new Dimension(800, 600));
		this.setMinimumSize(new Dimension(800,300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setLocationRelativeTo(null);
		 	
		createMenuBar();
		
		screens = new JPanel(new CardLayout());
		this.getContentPane().add(screens);
		
		LoginPanel login = new LoginPanel();
		login.setUserName("Admin");
		login.setUserNameEnabled(false);
		
		login.assignLoginButtonActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {

				JButton btn = (JButton) event.getSource();

				LoginPanel panel = (LoginPanel) btn.getParent();

				String pswd = panel.getPassword();
				
				try {
					
					if(pswd == null || pswd.isEmpty()) {
						throw new Exception("Please enter your password");
					}
					
					boolean accepted = false;
					
					try {
						accepted = AdminRemoteService.getAdminServer().loginAdmin(pswd);
					}catch(RemoteException exc) {
						//exc.printStackTrace();
						throw new Exception("Can't connect to a remote server. Please see a log file.");
					}
					
					if(!accepted) {
						throw new Exception("Incorrect password");
					}

					AdminClientApplication.this.getJMenuBar().setVisible(true);
	
					swapScreen(DisplayMode.TABLES);
					
				}catch(Exception e) {
					String [] opts = {"Close"};
					JOptionPane.showOptionDialog(AdminClientApplication.this, e.getMessage(), "Error occurred", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, opts,null);
				}
			}
		});

		tablesPanel = new TablesPanel(this);
		
		panels.put(DisplayMode.TABLES, tablesPanel);
		
		
		playersPanel = new PlayersPanel(this);
		
		panels.put(DisplayMode.USERS, playersPanel);
		
		
		screens.add(login, DisplayMode.LOGIN.name());
		screens.add(tablesPanel, DisplayMode.TABLES.name());
		screens.add(playersPanel, DisplayMode.USERS.name());
		
		this.setVisible(true);
	}

	private void swapScreen(DisplayMode mode) {
		cancelTimer();
		
		currentMode = mode;
		
		updateMenuItems();
		updateWindowTitle();
		updateData();
		
		CardLayout cardsLayout = (CardLayout) screens.getLayout();
		cardsLayout.show(screens,currentMode.name());
	}
	
	private void updateData() {
		
		Boolean firstTime = isFirstTime.get(currentMode);
		firstTime = firstTime == null?true:firstTime;
		isFirstTime.put(currentMode, false);
		
		switch(currentMode) {
		case TABLES:
			tablesPanel.loadData(!firstTime);
			break;
		case USERS:
			playersPanel.loadData(!firstTime);
			break;
		default:
			break;
		}
		activateTimer(firstTime);
	}
	
	private void updateWindowTitle() {
		switch(currentMode) {
		case TABLES:
			setTitle("Admin application - List of poker tables");
			break;
		case USERS:
			setTitle("Admin application - List of users");
			break;
		default:
			setTitle("Admin application - Login window");
		}
	}
	
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);	
		
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);
		
		JMenuItem item = new JMenuItem("Poker tables");
		menu.add(item);
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				swapScreen(DisplayMode.TABLES);
			}
		});
		
		item = new JMenuItem("Users");
		
		menu.add(item);
		
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				swapScreen(DisplayMode.USERS);
			}
		});
		
		item = new JMenuItem("Change password...");
		
		menu.add(item);
		
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (pswdDialog == null) {
					pswdDialog = new PasswordEditorDialog(AdminClientApplication.this, true);
				}else {
					pswdDialog.setVisible(true);
				}
			}
		});
		
		item = new JMenuItem("Exit");
		menu.add(item);
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		menuBar.setVisible(false);
	}
	
	private void updateMenuItems() {
		switch(currentMode) {
		case TABLES:
			this.getJMenuBar().getMenu(0).getItem(0).setEnabled(false);
			this.getJMenuBar().getMenu(0).getItem(1).setEnabled(true);
			break;
		case USERS:
			this.getJMenuBar().getMenu(0).getItem(0).setEnabled(true);
			this.getJMenuBar().getMenu(0).getItem(1).setEnabled(false);			
			break;
		default:
			break;
		}		
	}
	
	private void activateTimer(boolean isFirstTime) {
		int delay = isFirstTime? 60000 : 0;
		timers.put(currentMode,new Timer());
		timers.get(currentMode).schedule(new DataUpdateTask(), delay, 60000);
	}
	
	private void cancelTimer() {
		Timer timer = timers.get(currentMode);
		if(timer != null) timer.cancel();
	}
	
	private class DataUpdateTask extends TimerTask {

		@Override
		public void run() {
			panels.get(currentMode).loadData(true);
		}		
	}
}
