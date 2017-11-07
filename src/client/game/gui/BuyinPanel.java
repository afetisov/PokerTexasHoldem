package client.game.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class BuyinPanel extends JPanel {
	
	private BigDecimal minBuyin;
	
	private BigDecimal maxBuyin;
	
	private BigDecimal selectedBuyin;
	
	private JLabel buyinLabel;
	
	private JSlider buyinSlider; 

	private JFormattedTextField buyinText;
	
	public BuyinPanel(BigDecimal min, BigDecimal max) {
		
		minBuyin = min;
		maxBuyin = max;
		
		initComponents();
	}

	private void initComponents() {
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JPanel buyinPanel = new JPanel();
		add(buyinPanel);
		
		buyinPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		buyinLabel = new JLabel("Buy in amount:");		
		buyinPanel.add(buyinLabel);
		
		DecimalFormat format = new DecimalFormat("#,##0.00");
		format.setMaximumFractionDigits(2);
		format.setParseBigDecimal(true);
		
		buyinText = new JFormattedTextField(format);
		buyinText.setPreferredSize(new Dimension(100, 30));
		buyinText.setValue(minBuyin);
		buyinPanel.add(buyinText);
		selectedBuyin = minBuyin;
		
		buyinSlider = new JSlider();
		
		add(buyinSlider);
		
		buyinSlider.setMinimum(0);
		buyinSlider.setMaximum(100);
		buyinSlider.setValue(0);
		
		buyinSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = buyinSlider.getValue();
				BigDecimal diff = maxBuyin.subtract(minBuyin);
				if(diff.compareTo(BigDecimal.ONE) < 0) {
					selectedBuyin = minBuyin.add(diff.multiply(BigDecimal.valueOf(value / 100)).round(new MathContext(2, RoundingMode.HALF_UP)));
				}else {
					selectedBuyin = minBuyin.add(diff.multiply(BigDecimal.valueOf(value / 100)).round(new MathContext(0, RoundingMode.HALF_UP)));
				}
				buyinText.setValue(selectedBuyin);
			}
		});
	}

	public BigDecimal getSelectedBuyin() {
		return (BigDecimal) buyinText.getValue();
	}

	public void setSelectedBuyin(BigDecimal selectedBuyin) {
		this.selectedBuyin = selectedBuyin;
		buyinText.setValue(selectedBuyin);
	}

	public void setMinBuyin(BigDecimal minBuyin) {
		this.minBuyin = minBuyin;
	}

	public void setMaxBuyin(BigDecimal maxBuyin) {
		this.maxBuyin = maxBuyin;
	}
}
