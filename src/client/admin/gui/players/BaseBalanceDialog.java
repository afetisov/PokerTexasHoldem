package client.admin.gui.players;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.admin.gui.BaseEditorDialog;

@SuppressWarnings("serial")
public abstract class BaseBalanceDialog extends BaseEditorDialog<BigDecimal> {

	public BaseBalanceDialog(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();	
	}

	protected void initComponents() {
		panel = new JPanel();
		add(BorderLayout.CENTER, panel);
		
		panel.setLayout(new FlowLayout());
		panel.add(new JLabel("Amount:"));

        DecimalFormat amountFormat = new DecimalFormat("#,##0.00");
        amountFormat.setMaximumFractionDigits(2);
        amountFormat.setParseBigDecimal(true);
		inputField = new JFormattedTextField(amountFormat);
		inputField.setPreferredSize(new Dimension(150,28));
		panel.add(inputField);
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!validateAmount()) return;
				try {
					updateData();
				}catch(Exception exc) {
					//exc.printStackTrace();
					JOptionPane.showMessageDialog(null, "Failed to update data. See a log file for details.", "Error", JOptionPane.ERROR_MESSAGE);					
				}
				BaseBalanceDialog.this.setVisible(false);
			}
		});
	}

	protected abstract void updateData() throws Exception;
	
	protected boolean validateAmount() { 
		BigDecimal amount = getData();
		if(amount == null || amount == BigDecimal.ZERO || amount.signum() < 0) {
			
			JOptionPane.showMessageDialog(null, "Amount should be greater than 0.00", "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		} 
		return true;
	}
	
	@Override
	public BigDecimal getData() {
		return (BigDecimal) inputField.getValue();
	}

	protected JFormattedTextField inputField;
	
	private JPanel panel;
}
