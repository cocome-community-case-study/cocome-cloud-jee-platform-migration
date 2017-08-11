package org.cocome.cloud.web.frontend.store;

import java.util.Collection;
import java.util.List;

import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;

/**
 * Interface to retrieve information about the currently active store.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
public interface IStoreInformation {
	long STORE_ID_NOT_SET = Long.MIN_VALUE;
	
	void setActiveStoreID(long storeID);
	long getActiveStoreID();
	StoreViewData getActiveStore();
	String submitStore();
	
	boolean isStoreSet();
	
	List<ProductWrapper> getAllStockItems();
	
	Collection<ProductWrapper> getAllProductsWithStockItems();
	
	void queryStockItems();
	
	void queryProductsWithStockItems() throws NotInDatabaseException_Exception;
	
	List<ProductWrapper> getStockReport(long storeID);
	
	String switchToStore(@NotNull StoreViewData store, String destination);
}
