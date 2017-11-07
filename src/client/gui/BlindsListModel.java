package client.gui;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import client.admin.remote.AdminRemoteService;

import commons.model.TablesFilter;

public class BlindsListModel extends AbstractListModel implements ComboBoxModel {

	private Vector<BlindAmountItem> data = new Vector<BlindAmountItem>();
	
	private AtomicBoolean beingUpdated = new AtomicBoolean();
	
	private BlindAmountItem selected;
	
	@Override
	public Object getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public int getSize() {
		return data.size();
	}

	public void loadData(TablesFilter filter) {
		if(beingUpdated.compareAndSet(false, true)){
			try {
				List<BigDecimal> list = AdminRemoteService.getAdminServer().getBlinds(filter);
				
				data.clear();
				data.add(new BlindAmountItem(null, "Все"));
				selected = data.get(0);
				
				if(list != null) {
					for(BigDecimal blind : list) {
						data.add(new BlindAmountItem(blind));
					}
				}
				
				fireContentsChanged(this, 0, getSize());
			}catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				beingUpdated.set(false);
			}
		}
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@Override
	public void setSelectedItem(Object item) {
		selected = (BlindAmountItem) item;
	}
}
