package org.cocome.cloud.web.inventory.store;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;

/**
 * Interface to retrieve information about the currently active store.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IStoreInformation {
	public static final long STORE_ID_NOT_SET = Long.MIN_VALUE;
	
	public void setActiveStoreID(long storeID);
	public long getActiveStoreID();
	public Store getActiveStore();
	public String submitStore();
	
	public boolean isStoreSet();
	
	public List<ProductWrapper> getAllStockItems();
	
	public Collection<ProductWrapper> getAllProductsWithStockItems();
	
	public void queryStockItems();
	
	public void queryProductsWithStockItems();
	
	public List<ProductWrapper> getStockReport(long storeID);
	
	public String switchToStore(@NotNull Store store, String destination);
}
