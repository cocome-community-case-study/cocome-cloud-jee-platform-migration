package org.cocome.cloud.web.data.storedata;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
import org.cocome.cloud.web.connector.storeconnector.StoreQuery;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

@Named
@RequestScoped
public class StoreDAO implements IStoreDAO {
	
	@Inject
	EnterpriseQuery enterpriseQuery;
	
	@Inject
	StoreQuery storeQuery;

	@Override
	public Collection<Store> getStoresInEnterprise(long enterpriseID) throws NotInDatabaseException_Exception {
		return enterpriseQuery.getStores(enterpriseID);
	}

	@Override
	public Store getStoreByID(long storeID) throws NotInDatabaseException_Exception {
		return enterpriseQuery.getStoreByID(storeID);
	}

	@Override
	public List<ProductWrapper> queryStockItems(Store store) throws NotInDatabaseException_Exception {
		return storeQuery.queryStockItems(store);
	}

	@Override
	public ProductWrapper getStockItemByProductID(Store store, long productID) {
		return storeQuery.getStockItemByProductID(store, productID);
	}

	@Override
	public ProductWrapper getStockItemByBarcode(Store store, long barcode) {
		return storeQuery.getStockItemByBarcode(store, barcode);
	}

	@Override
	public List<ComplexOrderTO> getAllOrders(Store store) {
		return storeQuery.getAllOrders(store);
	}

	@Override
	public ComplexOrderTO getOrderByID(Store store, long orderID) {
		return storeQuery.getOrderByID(store, orderID);
	}

}
