package org.cocome.cloud.web.entitywrapper;

import org.cocome.tradingsystem.inventory.application.store.StockItemTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;

public class StockItemTOWrapper {

	private StockItemTO stockItemTO;
	
	private String enterpriseName;
	
	private String storeName;
	
	private String storeLocation;
	
	private boolean editingEnabled;
	
	public StockItemTOWrapper(StockItemTO stockItem, StoreWithEnterpriseTO store) {
		setStockItemTO(stockItem);
		setStoreName(store.getName());
		setStoreLocation(store.getLocation());
		setEnterpriseName(store.getEnterpriseTO().getName());
		setEditingEnabled(false);
	}

	public StockItemTOWrapper() {
		setStockItemTO(new StockItemTO());
		stockItemTO.setAmount(0);
		stockItemTO.setId(0);
		stockItemTO.setMaxStock(0);
		stockItemTO.setMinStock(0);
		stockItemTO.setSalesPrice(0.0);
		
		setStoreLocation("");
		setStoreName("");
		setEnterpriseName("");
		
		setEditingEnabled(false);
		
	}

	public StockItemTO getStockItemTO() {
		return stockItemTO;
		
	}

	public void setStockItemTO(StockItemTO stockItemTO) {
		this.stockItemTO = stockItemTO;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	public boolean isEditingEnabled() {
		return editingEnabled;
	}

	public void setEditingEnabled(boolean editingEnabled) {
		this.editingEnabled = editingEnabled;
	}
	
}
