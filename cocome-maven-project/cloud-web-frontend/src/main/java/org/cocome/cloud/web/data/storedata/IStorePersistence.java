package org.cocome.cloud.web.data.storedata;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;

public interface IStorePersistence {
	public String updateStore(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;
	
	public String createStore(long enterpriseID, @NotNull String name, @NotNull String location) throws NotInDatabaseException_Exception;
	
	public boolean updateStockItem(@NotNull ProductWrapper stockItem);
	
	public boolean orderProducts(@NotNull StoreViewData store, @NotNull Collection<OrderItem> items);
	
	public boolean rollInOrder(@NotNull StoreViewData store, long orderID);

	public boolean createStockItem(@NotNull StoreViewData store, @NotNull ProductWrapper product);
	
	public boolean updateStockItem(@NotNull StoreViewData store, @NotNull ProductWrapper stockItem);
}
