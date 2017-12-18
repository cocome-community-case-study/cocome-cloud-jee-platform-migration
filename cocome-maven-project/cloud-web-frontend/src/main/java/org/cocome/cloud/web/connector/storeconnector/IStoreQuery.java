package org.cocome.cloud.web.connector.storeconnector;

import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.storedata.OrderItem;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.OnDemandItemTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;

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
    List<ProductWrapper<StockItemTO>> queryStockItems(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;

    List<ProductWrapper<OnDemandItemTO>> queryOnDemandItems(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;

    boolean createItem(@NotNull StoreViewData store, @NotNull ProductWrapper product);

    boolean updateItem(@NotNull StoreViewData store, @NotNull ProductWrapper stockItem);

    boolean orderProducts(@NotNull StoreViewData store, @NotNull Collection<OrderItem> items);

    List<ComplexOrderTO> getAllOrders(@NotNull StoreViewData store);

    ComplexOrderTO getOrderByID(@NotNull StoreViewData store, long orderID);

    boolean rollInOrder(@NotNull StoreViewData store, long orderID);

    IStoreManager lookupStoreManager(long storeID) throws NotInDatabaseException_Exception;
}
