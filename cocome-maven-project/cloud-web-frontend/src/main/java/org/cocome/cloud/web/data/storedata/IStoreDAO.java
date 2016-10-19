package org.cocome.cloud.web.data.storedata;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

public interface IStoreDAO {
	public Collection<Store> getStoresInEnterprise(long enterpriseID) throws NotInDatabaseException_Exception;
	
	public Store getStoreByID(long storeID) throws NotInDatabaseException_Exception;
	
	public List<ProductWrapper> queryStockItems(@NotNull Store store) throws NotInDatabaseException_Exception;
	
	public ProductWrapper getStockItemByProductID(@NotNull Store store, long productID);
	
	public ProductWrapper getStockItemByBarcode(@NotNull Store store, long barcode);
	
	public List<ComplexOrderTO> getAllOrders(@NotNull Store store);
	
	public ComplexOrderTO getOrderByID(@NotNull Store store, long orderID);
}
