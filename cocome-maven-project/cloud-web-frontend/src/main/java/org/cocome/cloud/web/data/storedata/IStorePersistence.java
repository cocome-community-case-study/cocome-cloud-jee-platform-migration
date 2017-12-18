package org.cocome.cloud.web.data.storedata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface IStorePersistence {
    String updateStore(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;

    String createStore(long enterpriseID, @NotNull String name, @NotNull String location) throws NotInDatabaseException_Exception;

    boolean orderProducts(@NotNull StoreViewData store, @NotNull Collection<OrderItem> items);

    boolean rollInOrder(@NotNull StoreViewData store, long orderID);

    boolean createItem(@NotNull StoreViewData store, @NotNull ProductWrapper product);

    boolean updateItem(@NotNull StoreViewData store, @NotNull ProductWrapper stockItem);
}
