package org.cocome.cloud.web.data.storedata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
import org.cocome.cloud.web.connector.storeconnector.StoreQuery;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.OnDemandItemTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;

@Named
@RequestScoped
public class StoreDAO implements IStoreDAO {

    @Inject
    private EnterpriseQuery enterpriseQuery;

    @Inject
    private StoreQuery storeQuery;

    @Override
    public Collection<StoreViewData> getStoresInEnterprise(long enterpriseID) throws NotInDatabaseException_Exception {
        return enterpriseQuery.getStores(enterpriseID);
    }

    @Override
    public StoreViewData getStoreByID(long storeID) throws NotInDatabaseException_Exception {
        return enterpriseQuery.getStoreByID(storeID);
    }

    @Override
    public List<ProductWrapper<StockItemTO>> queryStockItems(StoreViewData store) throws NotInDatabaseException_Exception {
        return storeQuery.queryStockItems(store);
    }

    @Override
    public List<ProductWrapper<OnDemandItemTO>> queryOnDemandItems(StoreViewData store) throws NotInDatabaseException_Exception {
        return storeQuery.queryOnDemandItems(store);
    }

    @Override
    public List<ComplexOrderTO> getAllOrders(StoreViewData store) {
        return storeQuery.getAllOrders(store);
    }

    @Override
    public ComplexOrderTO getOrderByID(StoreViewData store, long orderID) {
        return storeQuery.getOrderByID(store, orderID);
    }

}
