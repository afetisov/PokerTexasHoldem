package client.admin.gui.players;

import java.awt.Dimension;
import java.awt.Frame;
import java.math.BigDecimal;
import commons.model.Player;
import client.admin.remote.AdminRemoteService;

@SuppressWarnings("serial")
public class DepositDialog extends BaseBalanceDialog {

	private Player player;
	
	public DepositDialog(Frame parent, boolean modal, Player player) {
		super(parent, modal);
		setTitle("Deposit a money");
		this.player = player;
		setSize(new Dimension(250, 120));
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	@Override
	protected void updateData() throws Exception {
		BigDecimal amount = (BigDecimal) inputField.getValue();
		player.setBalance(player.getBalance().add(amount));
		AdminRemoteService.getAdminServer().updateBalance(player.getId(), amount);		
	}
}
