package client.admin.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import client.admin.gui.tables.TableEditorDialog;

@SuppressWarnings("serial")
public abstract class BaseEditorDialog<E> extends JDialog {
	
	public BaseEditorDialog(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}
	
	private void initComponents() {
		btnSave = new JButton("Save");
		btnCancel = new JButton("Cancel");
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BaseEditorDialog.this.setVisible(false);
				canceled = true;
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.add(btnSave);
		panel.add(btnCancel);
		getContentPane().add(panel,BorderLayout.PAGE_END);
	}

	public boolean isCanceled() {
		return canceled;
	}

	public abstract E getData();
	
	protected boolean canceled;

	protected JButton btnSave;

	protected JButton btnCancel;
}
