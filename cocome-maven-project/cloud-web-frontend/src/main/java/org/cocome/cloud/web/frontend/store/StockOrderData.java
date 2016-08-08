package org.cocome.cloud.web.frontend.store;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.cocome.cloud.web.data.store.OrderItem;
import org.cocome.cloud.web.data.store.ProductWrapper;

@Named
@ConversationScoped
public class StockOrderData implements Serializable {
	private static final long serialVersionUID = -4500925840688109911L;
	
	private boolean orderInProgress = false;
	private Map<Long, OrderItem> itemMap;
	private ProductWrapper selectedItem;
	
	public boolean isOrderInProgress() {
		return orderInProgress;
	}
	
	public void setOrderInProgress(boolean orderInProgress) {
		this.orderInProgress = orderInProgress;
	}
	
	public Map<Long, OrderItem> getItemMap() {
		return itemMap;
	}
	
	public void setItemMap(Map<Long, OrderItem> itemMap) {
		this.itemMap = itemMap;
	}
	
	public ProductWrapper getSelectedItem() {
		return selectedItem;
	}
	
	public void setSelectedItem(ProductWrapper selectedItem) {
		this.selectedItem = selectedItem;
	}
}
