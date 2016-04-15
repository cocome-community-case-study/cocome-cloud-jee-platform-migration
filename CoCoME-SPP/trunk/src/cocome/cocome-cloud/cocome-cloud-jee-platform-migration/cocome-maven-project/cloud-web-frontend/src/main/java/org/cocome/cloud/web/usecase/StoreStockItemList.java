package org.cocome.cloud.web.usecase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.cocome.cloud.web.entitywrapper.ProductWSSTOWrapper;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierAndStockItemTO;

public class StoreStockItemList {
	private boolean reloadNeeded;
	
	private HashMap<Long, ProductWSSTOWrapper> stockItems = new HashMap<Long, ProductWSSTOWrapper>();
	
	public List<ProductWSSTOWrapper> getListOfStockItems() {
		return new ArrayList<ProductWSSTOWrapper>(stockItems.values());
	}
	
	public void setListOfStockItems(List<ProductWSSTOWrapper> listOfStockItems) {
		stockItems.clear();
		for (ProductWSSTOWrapper item : listOfStockItems) {
			stockItems.put(item.getStockItemTO().getId(), item);
		}
	}
	
	public ProductWSSTOWrapper getStockItemWithID(long stockItemID) {
		return stockItems.get(stockItemID);
	}
	
	public void setStockItemWithID(long stockItemID, ProductWSSTOWrapper stockItem) {
		stockItems.put(stockItemID, stockItem);
	}
	
	public void insertProducts(Collection<ProductWithSupplierAndStockItemTO> products) {
		for (ProductWithSupplierAndStockItemTO product : products) {
			this.setStockItemWithID(product.getStockItemTO()
					.getId(), new ProductWSSTOWrapper(product));
		}
	}
	
	public boolean isReloadNeeded() {
		return reloadNeeded;
	}
	
	public void setReloadNeeded(boolean reloadNeeded) {
		this.reloadNeeded = reloadNeeded;
	}
	
	public void clear() {
		stockItems.clear();
	}
}
