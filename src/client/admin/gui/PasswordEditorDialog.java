package client.admin.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.admin.remote.AdminRemoteService;

@SuppressWarnings("serial")
public class PasswordEditorDialog extends BaseEditorDialog<String[]> {

	public PasswordEditorDialog(JFrame parent, boolean modal) {
		super(parent, modal);
		
		setTitle("Admin password change");
		
		initComponents();
		
		setSize(new Dimension(300, 150));
		
		setLocationRelativeTo(parent);
		
		setVisible(true);
	}
	
	private void initComponents() {
		
		passwordPanel = new PasswordEditorPanel();		
		add(passwordPanel, BorderLayout.CENTER);
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				String [] opts = {"Close"};
				
				if(!passwordPanel.validatePassword()) {
					JOptionPane.showOptionDialog(null, "Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE, JOptionPane.OK_OPTION, null, opts, opts[0]);
					return;
				}
				
				String [] passwords = passwordPanel.getData();
				if(passwords[0] == null || passwords[0].isEmpty()) {
					JOptionPane.showOptionDialog(null, "Old password is missing", "Error", JOptionPane.ERROR_MESSAGE, JOptionPane.OK_OPTION, null, opts, opts[0]);
					return;
				}
				try {
					boolean success = AdminRemoteService.getAdminServer().changeAdminPassword(passwords[1], passwords[0]);
					if(!success) {
						JOptionPane.showOptionDialog(null, "Old password is incorrect", "Error", JOptionPane.ERROR_MESSAGE, JOptionPane.OK_OPTION, null, opts, opts[0]);
						return;
					}else {
						JOptionPane.showOptionDialog(null, "Password was successfully changed!", "Success", JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_OPTION, null, opts, opts[0]);
					}
				}catch(Exception exc) {
					JOptionPane.showOptionDialog(null, "Changes were not changed. See log file for details.", "Error", JOptionPane.ERROR_MESSAGE, JOptionPane.OK_OPTION, null, opts, opts[0]);
					//exc.printStackTrace();
				}
				passwordPanel.clear();
				PasswordEditorDialog.this.setVisible(false);
			}
		});
	}
	
	private PasswordEditorPanel passwordPanel;

	@Override
	public String[] getData() {
		return null;
	}
}
