package org.cocome.cloud.web.usecase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.WebServiceRef;

import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierAndStockItemTO;

public class StockItemListFactory {	
	@WebServiceRef(IStoreManagerService.class)
	IStoreManager storeManager;
	
	private static Map<Long, StoreStockItemList> storeLists = new HashMap<Long, StoreStockItemList>();
	
	public StoreStockItemList getStockItemList(long storeID) throws NotInDatabaseException_Exception {
		StoreStockItemList stockItems = null;
		stockItems = storeLists.get(storeID);
		
		if(stockItems == null) {
			stockItems = new StoreStockItemList();
			stockItems.insertProducts(getProductsWithStockItems(storeID));
			stockItems.setReloadNeeded(false);
			storeLists.put(storeID, stockItems);
		} else if (stockItems.isReloadNeeded()) {
			stockItems.clear();
			stockItems.insertProducts(getProductsWithStockItems(storeID));
			stockItems.setReloadNeeded(false);
		}
		
		return stockItems;
	}
	
	private Collection<ProductWithSupplierAndStockItemTO> getProductsWithStockItems(long storeID)
			throws NotInDatabaseException_Exception {
		return storeManager.getProductsWithStockItems(storeID);
	}

	public void setReloadNeeded(long storeID, boolean reload) {
		StoreStockItemList stockItems = storeLists.get(storeID);
		if (stockItems != null) {
			stockItems.setReloadNeeded(reload);
		}
	}
}
