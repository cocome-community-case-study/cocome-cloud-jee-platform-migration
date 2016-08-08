package org.cocome.cloud.web.backend.store;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.store.OrderItem;
import org.cocome.cloud.web.data.store.ProductWrapper;
import org.cocome.cloud.web.data.store.Store;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

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
	
	public boolean orderProducts(@NotNull Store store, @NotNull Collection<OrderItem> items);
	
	public List<ComplexOrderTO> getAllOrders(@NotNull Store store);
	
	public ComplexOrderTO getOrderByID(@NotNull Store store, long orderID);
	
	public boolean rollInOrder(@NotNull Store store, long orderID);

	public boolean createStockItem(@NotNull Store store, @NotNull ProductWrapper product);
	
	public IStoreManager lookupStoreManager(long storeID) throws NotInDatabaseException_Exception;
}
