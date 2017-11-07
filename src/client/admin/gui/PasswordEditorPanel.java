package client.admin.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class PasswordEditorPanel extends JPanel implements BaseEditorPanel<String []> {

	public PasswordEditorPanel() {
		initComponents();
	}
	
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        JLabel jLabel1 = new JLabel();
        JLabel jLabel3 = new JLabel();
        JLabel jLabel4 = new JLabel();
        
        oldPassword = new JPasswordField();
        newPassword = new JPasswordField();
        newPasswordConfirmation = new JPasswordField();

        setLayout(new java.awt.GridBagLayout());

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        
        jLabel1.setText("Old password:");
        add(jLabel1, gridBagConstraints);

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        
        oldPassword.setPreferredSize(new java.awt.Dimension(150, 28));
        add(oldPassword, gridBagConstraints);

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        
        jLabel3.setText("New password:");
        add(jLabel3, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(newPassword, gridBagConstraints);

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        
        jLabel4.setText("Repeat password:");
        add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(newPasswordConfirmation, gridBagConstraints);
    }
    
    private JPasswordField newPassword;
    private JPasswordField newPasswordConfirmation;
    private JPasswordField oldPassword;
    
	@Override
	public void setData(String[] data) {
		//no op
	}

	@Override
	public String[] getData() {
		String [] result = new String[2];
		result[0] = String.valueOf(oldPassword.getPassword());
		result[1] = String.valueOf(newPassword.getPassword());
		return result;
	}

    public boolean validatePassword() {
    	if(newPassword.getPassword() == null || newPassword.getPassword().length == 0) return false;
    	
    	if(!String.valueOf(newPassword.getPassword()).equals(String.valueOf(newPasswordConfirmation.getPassword()))) {
    		return false;
    	}
    	
    	return true;
    }
    
    public void clear() {
    	oldPassword.setText(null);
    	newPassword.setText(null);
    	newPasswordConfirmation.setText(null);
    }
}
