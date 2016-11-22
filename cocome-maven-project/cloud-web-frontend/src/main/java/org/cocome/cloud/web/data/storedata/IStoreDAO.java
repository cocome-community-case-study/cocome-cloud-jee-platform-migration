package org.cocome.cloud.web.data.storedata;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

public interface IStoreDAO {
	public Collection<StoreViewData> getStoresInEnterprise(long enterpriseID) throws NotInDatabaseException_Exception;
	
	public StoreViewData getStoreByID(long storeID) throws NotInDatabaseException_Exception;
	
	public List<ProductWrapper> queryStockItems(@NotNull StoreViewData store) throws NotInDatabaseException_Exception;
	
	public ProductWrapper getStockItemByProductID(@NotNull StoreViewData store, long productID);
	
	public ProductWrapper getStockItemByBarcode(@NotNull StoreViewData store, long barcode);
	
	public List<ComplexOrderTO> getAllOrders(@NotNull StoreViewData store);
	
	public ComplexOrderTO getOrderByID(@NotNull StoreViewData store, long orderID);
}
