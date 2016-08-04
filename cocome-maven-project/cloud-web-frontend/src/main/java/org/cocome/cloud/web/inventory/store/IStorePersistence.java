package org.cocome.cloud.web.inventory.store;

import javax.validation.constraints.NotNull;

public interface IStorePersistence {
	public String updateStore(@NotNull Store store);
	
	public String createStore(long enterpriseID, @NotNull String name, @NotNull String location);
	
	public boolean updateStockItem(@NotNull ProductWrapper stockItem);
}
