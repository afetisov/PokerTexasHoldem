package client.admin.gui.players;

import java.awt.Dimension;
import java.awt.Frame;
import java.math.BigDecimal;

import javax.swing.JOptionPane;

import client.admin.remote.AdminRemoteService;

import commons.model.Player;

@SuppressWarnings("serial")
public class WithdrawDialog extends BaseBalanceDialog {

	private Player player;
	
	public WithdrawDialog(Frame parent, boolean modal, Player player) {
		super(parent, modal);
		setTitle("Withdraw money");
		this.player = player;
		setSize(new Dimension(250, 120));
		setLocationRelativeTo(parent);
		setVisible(true);
		
	}
	
	@Override
	protected boolean validateAmount() {
		if(!super.validateAmount()) return false;
		
		BigDecimal amount = getData();
		if(player.getBalance().compareTo(amount) < 0) {
			JOptionPane.showMessageDialog(null, "Withdrawal amount should not exceed the total amount", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}

	@Override
	protected void updateData() throws Exception {
		BigDecimal amount = (BigDecimal) inputField.getValue();
		player.setBalance(player.getBalance().subtract(amount));
		AdminRemoteService.getAdminServer().updateBalance(player.getId(), amount.negate());
	}
}
