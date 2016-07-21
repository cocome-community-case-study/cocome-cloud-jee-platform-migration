package org.cocome.cloud.web.inventory.connection;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.inventory.store.ProductWrapper;
import org.cocome.cloud.web.inventory.store.Store;

/**
 * Interface to retrieve stock items from a specific store and to account sales at that store.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IStoreQuery {
	public List<ProductWrapper> queryStockItems(@NotNull Store store) throws NotInDatabaseException_Exception;
	
	public ProductWrapper getStockItemByProductID(@NotNull Store store, long productID);
	
	public ProductWrapper getStockItemByBarcode(@NotNull Store store, long barcode);
	
	public boolean updateStockItem(@NotNull Store store, @NotNull ProductWrapper stockItem);
}
