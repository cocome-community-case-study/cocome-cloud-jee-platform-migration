package org.cocome.cloud.web.data.storedata;

import java.util.Collection;

import javax.validation.constraints.NotNull;

public interface IStorePersistence {
	public String updateStore(@NotNull Store store);
	
	public String createStore(long enterpriseID, @NotNull String name, @NotNull String location);
	
	public boolean updateStockItem(@NotNull ProductWrapper stockItem);
	
	public boolean orderProducts(@NotNull Store store, @NotNull Collection<OrderItem> items);
	
	public boolean rollInOrder(@NotNull Store store, long orderID);

	public boolean createStockItem(@NotNull Store store, @NotNull ProductWrapper product);
	
	public boolean updateStockItem(@NotNull Store store, @NotNull ProductWrapper stockItem);
}
