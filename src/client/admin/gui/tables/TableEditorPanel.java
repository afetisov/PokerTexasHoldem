package client.admin.gui.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.admin.gui.BaseEditorPanel;

import commons.game.GameType;
import commons.model.TableInfo;

@SuppressWarnings("serial")
public class TableEditorPanel extends JPanel implements BaseEditorPanel<TableInfo>{
    public TableEditorPanel() {
        initComponents();
    }
    
    private void initComponents() {
 
        DecimalFormat blindAmountFormat = new DecimalFormat("#,##0.00");
        blindAmountFormat.setMaximumFractionDigits(2);
        blindAmountFormat.setParseBigDecimal(true);
 
    	java.awt.GridBagConstraints gridBagConstraints;

        JLabel jLabel1 = new JLabel("Game Type:");
        gameType = new JComboBox();
        
        gameType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(blindAmount.getValue() != null) {
					BigDecimal blind = (BigDecimal) blindAmount.getValue();
					if(gameType.getSelectedItem() == GameType.FIXED_BETS) {
						maxBuyin.setValue(blind.multiply(BigDecimal.valueOf(100)));
					} else {
						maxBuyin.setValue(null);
					}
				}
			}
		});
        
        JLabel jLabel2 = new JLabel("Big blind:");
        blindAmount = new JFormattedTextField(blindAmountFormat);
        
        blindAmount.addPropertyChangeListener("value", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getNewValue() != null) {
					BigDecimal blind = (BigDecimal) evt.getNewValue();
					if(gameType.getSelectedItem() == GameType.FIXED_BETS) {
						maxBuyin.setValue(blind.multiply(BigDecimal.valueOf(100)));
					} else {
						maxBuyin.setValue(null);
					}
					minBuyin.setValue(blind.multiply(BigDecimal.valueOf(20)));
				}
			}
		});
      
        
        JLabel jLabel3 = new JLabel("Max raises count:");
        
        DecimalFormat capFormat = new DecimalFormat("#");
        capFormat.setMaximumFractionDigits(0);
        capFormat.setParseIntegerOnly(true);
        
        capSize = new JFormattedTextField(capFormat);
        capSize.setValue(3);
        
        JLabel jLabel4 = new JLabel("Min buy in:");
        minBuyin = new JFormattedTextField(blindAmountFormat);
        
        JLabel jLabel5 = new JLabel("Max buy in:");
        maxBuyin = new JFormattedTextField(blindAmountFormat);
        
        JLabel jLabel6 = new JLabel("Password:");
        password = new JTextField();

        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel1, gridBagConstraints);

        gameType.setModel(new javax.swing.DefaultComboBoxModel(GameType.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(gameType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(blindAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(capSize, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(minBuyin, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(maxBuyin, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(password, gridBagConstraints);
    }                        

    // Variables declaration - do not modify                     
    private JFormattedTextField blindAmount;
    
    private JComboBox gameType;
    
    private JFormattedTextField capSize;
    
    private JFormattedTextField minBuyin;
    
    private JFormattedTextField maxBuyin;
    
    private JTextField password;
    
    private BigDecimal getBlindAmount() {
    	return (BigDecimal) blindAmount.getValue(); 
    }
    
    private GameType getGameType() {
    	return (GameType) gameType.getSelectedItem();
    }
    
    private String getPassword() {
    	return password.getText();
    }

	public Integer getCapSize() {
		return (Integer) capSize.getValue();
	}

	public BigDecimal getMinBuyin() {
		return (BigDecimal) minBuyin.getValue();
	}

	public BigDecimal getMaxBuyin() {
		return (BigDecimal) maxBuyin.getValue();
	}

	@Override
	public void setData(TableInfo data) {
		
		if(data == null) return;
		
		gameType.setSelectedItem(data.getGameType());
		
		blindAmount.setValue(data.getBigBlind());
		
		capSize.setValue(data.getCapSize());
		
		minBuyin.setValue(data.getMinBayin());
		
		maxBuyin.setValue(data.getMaxBayin());
		
		password.setText(data.getPassword());
		
		gameType.setEnabled(false);
		blindAmount.setEditable(false);
		capSize.setEditable(false);
		minBuyin.setEditable(false);
		maxBuyin.setEnabled(false);
		password.setEditable(false);
	}

	@Override
	public TableInfo getData() {
		return new TableInfo(getGameType(), getBlindAmount(), getCapSize(), getMinBuyin(), getMaxBuyin(), getPassword());
	}
}
