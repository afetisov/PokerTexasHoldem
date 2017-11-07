package client.admin.gui.players;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import commons.model.Player;
import client.admin.gui.BaseEditorPanel;
import client.service.EncryptionService;

@SuppressWarnings("serial")
public class PlayerEditorPanel extends JPanel implements BaseEditorPanel<Player> {

	public PlayerEditorPanel() {
		initComponents();
	}
	
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        userNameField = new JTextField();
        userNameField.setPreferredSize(new java.awt.Dimension(150, 28));
        
        passwordField = new JPasswordField();
        
        passwordField2 = new JPasswordField();

        setLayout(new java.awt.GridBagLayout());

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        
        JLabel jLabel1 = new JLabel("Login name:");
        add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        
        add(userNameField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;

        JLabel jLabel2 = new JLabel("Password:");
        add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;

        add(passwordField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        
        JLabel jLabel3 = new JLabel("Repeat password:");
        add(jLabel3, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        
        add(passwordField2, gridBagConstraints);
    }
	
    public boolean validatePassword() {
    	if(passwordField.getPassword() == null || passwordField.getPassword().length == 0) return false;
    	
    	if(!String.valueOf(passwordField.getPassword()).equals(String.valueOf(passwordField2.getPassword()))) {
    		return false;
    	}
    	
    	return true;
    }
    
    public boolean checkPasswordLength() {
    	if(passwordField.getPassword() == null) return false;
    	
    	String password = String.valueOf(passwordField.getPassword());
    	
    	if(password.length() >= 6 && password.length() <= 12) {
    		return true;
    	} 
    	
    	return false;
    }
    
    
    @Override
	public void setData(Player data) {
    	//no op
	}

	@Override
	public Player getData() {
		Player player = new Player();
		player.setName(userNameField.getText());
		player.setPassword(getEncryptedPassword());
		return player;
	}

	private String getEncryptedPassword() {
		if(passwordField.getPassword() == null) return null;
		
		String password = String.valueOf(passwordField.getPassword());
		
		if(password.isEmpty()) return null;
		
		return EncryptionService.ecrypt(password);
	}
	
    private JPasswordField passwordField2;
    private JPasswordField passwordField;
    private JTextField userNameField;
}
