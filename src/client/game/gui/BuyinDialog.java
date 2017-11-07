package client.game.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.gui.GUIUtils;


@SuppressWarnings("serial")
public class BuyinDialog extends JDialog {

	private int dialogStatus;
	
	private final BigDecimal minBuyin;
	
	private final BigDecimal maxBuyin;
	
	private BuyinPanel panel;
	
	public BuyinDialog(Frame parent, boolean modal, BigDecimal minBuyin, BigDecimal maxBuyin) {
		
		super(parent, modal);
		
		setSize(new Dimension(250, 150));
		
		setResizable(false);
		
		setTitle("Enter buy in amount");
		
		panel = new BuyinPanel(minBuyin, maxBuyin);
		
		this.minBuyin = minBuyin;
		
		this.maxBuyin = maxBuyin;
		
		getContentPane().add(panel,BorderLayout.PAGE_START);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton btnNext = new JButton("Next");
		buttonsPanel.add(btnNext);
		
		JButton btnCancel = new JButton("Cancel");		
		buttonsPanel.add(btnCancel);
		
		getContentPane().add(buttonsPanel,BorderLayout.PAGE_END);
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogStatus = JOptionPane.CANCEL_OPTION;
				setVisible(false);
			}
		});
		
		btnNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				String [] opts = {"Close"};
				
				if(BuyinDialog.this.minBuyin != null && panel.getSelectedBuyin().compareTo(BuyinDialog.this.minBuyin) < 0) {
					

					
					JOptionPane.showOptionDialog(
							BuyinDialog.this, 
							"Amount should not be less than mininum buy in: $" + GUIUtils.decimal2text(BuyinDialog.this.minBuyin), 
							"Error",
							JOptionPane.OK_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							null, opts, opts[0]);
					
					return;
				}

				if(BuyinDialog.this.maxBuyin != null && panel.getSelectedBuyin().compareTo(BuyinDialog.this.maxBuyin) > 0) {
					
					JOptionPane.showOptionDialog(
							BuyinDialog.this, 
							"Amount should not exceed maximum buy in: $" + GUIUtils.decimal2text(BuyinDialog.this.maxBuyin), 
							"Error",
							JOptionPane.OK_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							null, opts, opts[0]);
					
					return;
				}
				
				dialogStatus = JOptionPane.OK_OPTION;
				setVisible(false);
			}
		});
		
		setLocationRelativeTo(parent);		
		
		setVisible(true);
	}
	
	public int getDialogStatus() {
		return dialogStatus;
	}
	
	public BigDecimal getBuyinValue() {
		return panel.getSelectedBuyin();
	}
	
}
