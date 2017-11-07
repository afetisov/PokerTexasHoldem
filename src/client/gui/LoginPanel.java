package client.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author alexfetisov
 */
public class LoginPanel extends JPanel {

	private Properties settings = new Properties();
	
	
    public LoginPanel() {
    	
    	try {
    		
        	FileInputStream fis = new FileInputStream("settings");
    	
        	settings.load(fis);
        
        	String host = settings.getProperty("host");
        	
        	if((host != null && !host.isEmpty())) {
        		
        		System.setProperty("host", host);
        	
        	}
    	
    	}catch(Exception exc) {}
    	
        initComponents();
    }

    private class EnterKeyAdaptor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			
			super.keyPressed(e);
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				btnLogin.doClick();
			}
		}
    }
    
    private void initComponents() {
       
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();

        nameLabel = new JLabel("Name:");
        
        userNameField = new JTextField();
        userNameField.setPreferredSize(new Dimension(130, 28));
        
        JLabel jLabel2 = new JLabel("Password:");
        
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(130, 28));
        
        hostLabel = new JLabel("Host name:");
        hostLabel.setVisible(false);
        
        
        hostField = new JTextField();
        hostField.setPreferredSize(new Dimension(130, 28));
        hostField.setVisible(false);
        hostField.setText(System.getProperty("host","localhost"));
        
        btnLogin = new JButton("Login");
        
        btnMore = new JButton("Settings");
        
        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(nameLabel, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(userNameField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(passwordField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(hostLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        add(hostField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(btnLogin, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(btnMore, gridBagConstraints);

        
        EnterKeyAdaptor keyListener = new EnterKeyAdaptor(); 
        
        userNameField.addKeyListener(keyListener);
        
        passwordField.addKeyListener(keyListener);
    
        btnMore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(btnMore.getText().equals("Settings")) {
				
					hostLabel.setVisible(true);
					
					hostField.setVisible(true);
					
					btnMore.setText("Hide");
				
				} else {
					
					hostLabel.setVisible(false);
					
					hostField.setVisible(false);
					
					btnMore.setText("Settings");
				}
			}

        });
     }

    private JButton btnLogin;
    
    private JButton btnMore;
    
    private JLabel nameLabel;
    
    private JLabel hostLabel;
    
    private JTextField userNameField;
    
    private JPasswordField passwordField;
    
    private JTextField hostField;
    
    public void assignLoginButtonActionListener(ActionListener listener) {
    	btnLogin.addActionListener(listener);
    }
    
    public void saveSettings() {

    	if(hostField.getText() == null || hostField.getText().equals(System.getProperty("host"))) return;
		
		settings.setProperty("host", hostField.getText());
		
		System.setProperty("host", hostField.getText());
		
		try {
			
			FileOutputStream fos = new FileOutputStream("settings");

			settings.store(fos, null);
			
		}catch(Exception exc) {}
    }
    
    public String getUserName() {
    	return userNameField.getText();
    }
    
    public void setUserName(String name) {
    	userNameField.setText(name);
    }
    
    public void setUserNameEnabled(boolean enabled) {
    	userNameField.setEditable(enabled);
    }
    
    public String getPassword() {
    	
    	String pswd = new String(passwordField.getPassword());
    	
    	passwordField.setText(null);
    	
    	return pswd;
    }
    
    public void setNameLabelText(String name) {
    	nameLabel.setText(name);
    }
}
