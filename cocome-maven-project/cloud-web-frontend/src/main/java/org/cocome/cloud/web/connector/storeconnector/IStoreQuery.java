package org.cocome.cloud.web.connector.storeconnector;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.storedata.OrderItem;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

/**
 * Interface to retrieve stock items from a specific store and to account sales at that store.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IStoreQuery {
	public List<ProductWrapper> queryStockItems(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;
	
	public ProductWrapper getStockItemByProductID(@NotNull StoreViewData store, long productID);
	
	public ProductWrapper getStockItemByBarcode(@NotNull StoreViewData store, long barcode);
	
	public boolean updateStockItem(@NotNull StoreViewData store, @NotNull ProductWrapper stockItem);
	
	public boolean orderProducts(@NotNull StoreViewData store, @NotNull Collection<OrderItem> items);
	
	public List<ComplexOrderTO> getAllOrders(@NotNull StoreViewData store);
	
	public ComplexOrderTO getOrderByID(@NotNull StoreViewData store, long orderID);
	
	public boolean rollInOrder(@NotNull StoreViewData store, long orderID);

	public boolean createStockItem(@NotNull StoreViewData store, @NotNull ProductWrapper product);
	
	public IStoreManager lookupStoreManager(long storeID) throws NotInDatabaseException_Exception;
}
