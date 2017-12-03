package org.cocome.cloud.web.connector.storeconnector;

import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.storedata.OrderItem;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * Interface to retrieve stock items from a specific store and to account sales at that store.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IStoreQuery {
    List<ProductWrapper> queryStockItems(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;

    ProductWrapper getStockItemByProductID(@NotNull StoreViewData store, long productID);

    ProductWrapper getStockItemByBarcode(@NotNull StoreViewData store, long barcode);

    boolean updateStockItem(@NotNull StoreViewData store, @NotNull ProductWrapper stockItem);

    boolean orderProducts(@NotNull StoreViewData store, @NotNull Collection<OrderItem> items);

    List<ComplexOrderTO> getAllOrders(@NotNull StoreViewData store);

    ComplexOrderTO getOrderByID(@NotNull StoreViewData store, long orderID);

    boolean rollInOrder(@NotNull StoreViewData store, long orderID);

    boolean createStockItem(@NotNull StoreViewData store, @NotNull ProductWrapper product);

    IStoreManager lookupStoreManager(long storeID) throws NotInDatabaseException_Exception;
}
