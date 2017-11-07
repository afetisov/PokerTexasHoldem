package client.game.gui.components;

import javax.swing.JButton;

public class SeatButton extends JButton {

	private short seatNumber;
	
	public SeatButton() {
		super();
	}
	
	public SeatButton(String name) {
		super(name);
	}

	public short getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(short seatNumber) {
		this.seatNumber = seatNumber;
	}
}
