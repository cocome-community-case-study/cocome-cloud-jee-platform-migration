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
	public static final long STORE_ID_NOT_SET = Long.MIN_VALUE;
	
	public void setActiveStoreID(long storeID);
	public long getActiveStoreID();
	public StoreViewData getActiveStore();
	public String submitStore();
	
	public boolean isStoreSet();
	
	public List<ProductWrapper> getAllStockItems();
	
	public Collection<ProductWrapper> getAllProductsWithStockItems();
	
	public void queryStockItems();
	
	public void queryProductsWithStockItems() throws NotInDatabaseException_Exception;
	
	public List<ProductWrapper> getStockReport(long storeID);
	
	public String switchToStore(@NotNull StoreViewData store, String destination);
}
