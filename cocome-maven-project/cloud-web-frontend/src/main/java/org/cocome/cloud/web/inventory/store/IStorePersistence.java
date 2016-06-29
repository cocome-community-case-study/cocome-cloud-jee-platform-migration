package org.cocome.cloud.web.inventory.store;

public interface IStorePersistence {
	public String updateStore(Store store);
	
	public String createStore(long enterpriseID, String name, String location);
}
