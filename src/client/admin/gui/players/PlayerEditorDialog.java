package client.admin.gui.players;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import commons.model.Player;

import client.admin.gui.BaseEditorDialog;
import client.admin.remote.AdminRemoteService;

@SuppressWarnings("serial")
public class PlayerEditorDialog extends BaseEditorDialog<Player> {

	public PlayerEditorDialog(Frame parent, boolean modal) {
		super(parent, modal);
		
		initComponents();
		
		setTitle("Create new user");
		setSize(new Dimension(300, 150));
		
		setLocationRelativeTo(parent);
		
		setVisible(true);
	}
	
	private void initComponents() {
		editorPanel = new PlayerEditorPanel();
		add(editorPanel, BorderLayout.CENTER);
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Player player = editorPanel.getData();
				
				if(!editorPanel.checkPasswordLength()) {
					JOptionPane.showMessageDialog(null, "Password must be between 6 and 12 chars", "Error", JOptionPane.OK_OPTION);
					return;
				}
				if(!editorPanel.validatePassword()) {
					JOptionPane.showMessageDialog(null, "Passwords don't match", "Error", JOptionPane.OK_OPTION);
					return;
				}
				try {
					AdminRemoteService.getAdminServer().savePlayer(player);
				}catch(Exception exc) {
					exc.printStackTrace();
					JOptionPane.showMessageDialog(null, "Failed to update data. See a log file for details.", "Error", JOptionPane.OK_OPTION);					
				}
				PlayerEditorDialog.this.setVisible(false);
			}
		});
	}
	
	private PlayerEditorPanel editorPanel;

	@Override
	public Player getData() {
		return null;
	}
}
