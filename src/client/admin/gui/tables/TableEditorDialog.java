package client.admin.gui.tables;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JOptionPane;

import client.admin.gui.BaseEditorDialog;
import client.admin.remote.AdminRemoteService;

import commons.model.TableInfo;

@SuppressWarnings("serial")
public class TableEditorDialog extends BaseEditorDialog<TableInfo> {

	public TableEditorDialog(Frame parent, boolean modal, TableInfo data) {
		super(parent, modal);
		
		setTitle(data == null? "Create new game table" : "View game table data");
		
		initComponents();
		
		btnSave.setVisible(data == null);
		editorPanel.setData(data);
		
		setSize(new Dimension(400, 250));
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	private void initComponents() {
		
		editorPanel = new TableEditorPanel();		
		add(editorPanel, BorderLayout.CENTER);
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				TableInfo data = editorPanel.getData();
				if(data.getBigBlind() == null
						|| data.getBigBlind() == BigDecimal.ZERO) {
					JOptionPane.showMessageDialog(null, "Big blind is not set", "Error", JOptionPane.OK_OPTION);
					return;
				}
				if(data.getBigBlind().signum() < 0) {
					JOptionPane.showMessageDialog(null, "Big blind is negative", "Error", JOptionPane.OK_OPTION);
					return;
				}
				try{	
					AdminRemoteService.getAdminServer().createTable(data);
				}catch(Exception exc) {
					//exc.printStackTrace();
					JOptionPane.showMessageDialog(null, "Failed to save changes. Please see a log file for details.", "Error", JOptionPane.OK_OPTION);
				}
				TableEditorDialog.this.setVisible(false);
			}
		});
	}
	
	
	private TableEditorPanel editorPanel;


	@Override
	public TableInfo getData() {
		return editorPanel.getData();
	}	
}
