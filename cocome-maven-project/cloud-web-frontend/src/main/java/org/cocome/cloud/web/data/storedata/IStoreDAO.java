package org.cocome.cloud.web.data.storedata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

public interface IStoreDAO {
    Collection<StoreViewData> getStoresInEnterprise(long enterpriseID) throws NotInDatabaseException_Exception;

    StoreViewData getStoreByID(long storeID) throws NotInDatabaseException_Exception;

    List<ProductWrapper> queryStockItems(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;

    ProductWrapper getStockItemByProductID(@NotNull StoreViewData store, long productID);

    ProductWrapper getStockItemByBarcode(@NotNull StoreViewData store, long barcode);

    List<ComplexOrderTO> getAllOrders(@NotNull StoreViewData store);

    ComplexOrderTO getOrderByID(@NotNull StoreViewData store, long orderID);
}
