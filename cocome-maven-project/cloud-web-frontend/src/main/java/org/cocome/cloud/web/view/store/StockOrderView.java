package org.cocome.cloud.web.view.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.inventory.connection.IStoreQuery;
import org.cocome.cloud.web.inventory.store.IStoreInformation;
import org.cocome.cloud.web.inventory.store.OrderItem;
import org.cocome.cloud.web.inventory.store.ProductWrapper;
import org.cocome.cloud.web.inventory.store.Store;

@Named
@ConversationScoped
//TODO resolve messages from localized strings
public class StockOrderView implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(StockOrderView.class);
	
	@Inject
	IStoreQuery storeQuery;
	
	@Inject
	StockOrderData orderData;
	
	@Inject
	IStoreInformation storeInformation;
	
	@Inject
	Conversation conversation;
	
	public String selectOrderItem(ProductWrapper item) {
		LOG.debug(String.format("Selecting item %s for ordering...", item.getName()));
		if (!isOrderInProgress()) {
			LOG.debug("Starting new order");
			startNewOrder();
		}
		
		orderData.setSelectedItem(item);
		
		return NavigationElements.ORDER_PRODUCTS.getNavigationOutcome();
	}
	
	public String addOrderAmount(int amount) {
		ProductWrapper selectedItem = orderData.getSelectedItem();
		Map<Long, OrderItem> itemMap = orderData.getItemMap();
		
		if (!isOrderInProgress() || selectedItem == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have to select a stock item first!"));
			return NavigationElements.SHOW_STOCK.getNavigationOutcome();
		} 
		
		LOG.debug(String.format("Adding amount %d of %s to order...", amount, selectedItem.getName()));
		
		itemMap.put(selectedItem.getBarcode(), new OrderItem(selectedItem, amount));
		selectedItem.setInCurrentOrder(true);
		deselectProduct();
		return NavigationElements.ORDER_PRODUCTS.getNavigationOutcome();
	}
	
	public String removeOrderItem(ProductWrapper item) {
		Map<Long, OrderItem> itemMap = orderData.getItemMap();
		OrderItem removedItem = itemMap.remove(item.getBarcode());
		removedItem.getProduct().setInCurrentOrder(false);
		return NavigationElements.SHOW_STOCK.getNavigationOutcome();
	}
	
	public String startNewOrder() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		
		if (isOrderInProgress()) {
			resetOrder();
		}

		
		orderData.setItemMap(new LinkedHashMap<Long, OrderItem>());
		orderData.setOrderInProgress(true);
		return NavigationElements.SHOW_STOCK.getNavigationOutcome();
	}
	
	public String submitOrder() {
		Store currentStore = storeInformation.getActiveStore();
		Collection<OrderItem> items = orderData.getItemMap().values();
		if (storeQuery.orderProducts(currentStore, items)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Order was placed successfully!"));
			resetOrder();
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error while placing the order!"));
		}
		
		return NavigationElements.SHOW_STOCK.getNavigationOutcome();
	}
	
	private void resetOrder() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		
		orderData.setOrderInProgress(false);
		orderData.setSelectedItem(null);
		
		emptyItemMap();
		
		orderData.setItemMap(null);
	}

	private void emptyItemMap() {
		Map<Long, OrderItem> itemMap = orderData.getItemMap();
		if (itemMap != null) {
			for (OrderItem item : itemMap.values()) {
				item.getProduct().setInCurrentOrder(false);
			}
			itemMap.clear();
		}
	}
	
	public String cancelOrder() {
		resetOrder();
		return NavigationElements.SHOW_STOCK.getNavigationOutcome();
	}
	
	public String showOrder() {
		return NavigationElements.ORDER_PRODUCTS.getNavigationOutcome();
	}
	
	public List<OrderItem> getOrderItems() {
		Map<Long, OrderItem> itemMap = orderData.getItemMap();
		return new LinkedList<>(itemMap.values());
	}
	
	public boolean hasOrderProducts() {
		Map<Long, OrderItem> itemMap = orderData.getItemMap();
		return itemMap != null ? (itemMap.size() > 0) : false;
	}
	
	public boolean isItemSelected() {
		return orderData.getSelectedItem() != null;
	}
	
	public ProductWrapper getSelectedItem() {
		return orderData.getSelectedItem();
	}
	
	public String deselectProduct() {
		orderData.setSelectedItem(null);
		return NavigationElements.SHOW_STOCK.getNavigationOutcome();
	}
	
	private boolean isOrderInProgress() {
		return orderData.isOrderInProgress();
	}
}
