package org.cocome.tradingsystem.inventory.data.store;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.connection.QueryParameterEncoder;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;


/**
 * The objects returned will only have their basic datatype attributes filled.
 *
 * @author Tobias Pöppke
 */
@Stateless
@Local(IStoreQuery.class)
public class EnterpriseStoreQueryProvider implements IStoreQuery {

    // TODO either cache the retrieved objects or provide faster queries which
    // return objects with only the simple attribute types set and other queries which
    // query all attributes of the objects

    private static final Logger LOG = Logger.getLogger(EnterpriseStoreQueryProvider.class);

    @Inject
    private IBackendQuery backendConnection;

    @Inject
    private IBackendConversionHelper csvHelper;

    @Override
    public IStore queryStore(String name, String location) {
        name = QueryParameterEncoder.encodeQueryString(name);
        location = QueryParameterEncoder.encodeQueryString(location);
        String locationQuery = "*";

        if (!location.equals("")) {
            locationQuery = location;
        }

        List<IStore> stores = (List<IStore>) csvHelper.getStores(
                backendConnection.getStores("name=LIKE%20'" + name + "';Store.location=LIKE%20'" + locationQuery + "'"));

        if (stores.size() > 1) {
            LOG.warn("More than one store with name " + name +
                    " and location " + location + " was found!");
        }

        try {
            return stores.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public IStore queryStoreById(long storeId) throws NotInDatabaseException {
        try {
            return csvHelper.getStores(
                    backendConnection.getStores("id==" + storeId)).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException(
                    "Store with ID " + storeId + " could not be found!");
        }
    }

    @Override
    public IItem queryItemById(long itemId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getItem, "Item", itemId);
    }

    @Override
    public IStockItem queryStockItemById(long stockItemId) throws NotInDatabaseException {
        try {
            return csvHelper.getStockItems(
                    backendConnection.getStockItems("id==" + stockItemId)).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("StockItem with ID "
                    + stockItemId + " could not be found!");
        }
    }

    @Override
    public IOnDemandItem queryOnDemandItemById(long onDemandItemId) throws NotInDatabaseException {
        return getSingleEntity(csvHelper::getOnDemandItem, "OnDemandItem", onDemandItemId);
    }

    @Override
    public IProduct queryProductById(long productId) throws NotInDatabaseException {
        try {
            final IProduct product = csvHelper.getProducts(
                    backendConnection.getProducts("id==" + productId)).iterator().next();
            product.setId(productId);
            return product;
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("Product with ID " + productId + " could not be found!");
        }
    }

    @Override
    public IProduct queryProductByBarcode(long barcode) {
        IProduct product = null;
        try {
            product = csvHelper.getProducts(
                    backendConnection.getProducts("barcode==" + barcode)).iterator().next();
        } catch (NoSuchElementException e) {
            // Do nothing, no product found
        }
        return product;
    }

    @Override
    public IProductOrder queryOrderById(long orderId) throws NotInDatabaseException {
        IProductOrder productOrder;
        try {
            productOrder = csvHelper.getProductOrders(
                    backendConnection.getProductOrder("id==" + orderId)).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("Order with ID " + orderId + " could not be found!");
        }
        return productOrder;
    }

    @Override
    public Collection<IProduct> queryProducts(long storeId) {
        Collection<IProduct> products = new LinkedList<>();
        for (IStockItem item : queryAllStockItems(storeId)) {
            products = csvHelper.getProducts(
                    backendConnection.getProducts("barcode==" + item.getProductBarcode()));
        }
        return products;
    }

    @Override
    public Collection<IProductOrder> queryOutstandingOrders(long storeId) {
        return csvHelper.getProductOrders(
                backendConnection.getProductOrder("store.id==" + storeId + ";ProductOrder.deliveryDate=<e.orderingDate"));
    }

    @Override
    public Collection<IStockItem> queryAllStockItems(long storeId) {
        return csvHelper.getStockItems(
                backendConnection.getStockItems("store.id==" + storeId));
    }

    @Override
    public Collection<IStockItem> queryLowStockItems(long storeId) {
        // Hacky way to get the result. We have to use e.minStock as comparison because
        // using StockItem.minStock will not be parsed and the query will return an error
        return csvHelper.getStockItems(
                backendConnection.getStockItems("store.id==" + storeId + ";StockItem.amount=<e.minStock"));
    }

    @Override
    public IStockItem queryStockItem(long storeId, long productBarcode) {
        IStockItem item = null;
        try {
            item = csvHelper.getStockItems(
                    backendConnection.getStockItems("product.barcode==" + productBarcode
                            + ";StockItem.store.id==" + storeId)).iterator().next();
        } catch (NoSuchElementException e) {
            // Do nothing, just return null and don't crash
        }
        return item;
    }

    @Override
    public Collection<IStockItem> queryStockItemsByProductId(long storeId,
                                                             long[] productIds) {
        List<IStockItem> items = new LinkedList<>();
        for (long productId : productIds) {
            Collection<IStockItem> stockItems = csvHelper.getStockItems(
                    backendConnection.getStockItems("store.id==" + storeId + ";product.id==" + productId));
            items.addAll(stockItems);
        }
        return items;
    }

    @Override
    public IProductOrder queryProductOrder(long storeId,
                                           long productBarcode, long amount) throws NotInDatabaseException {
        Collection<IProductOrder> productOrders = csvHelper.getProductOrders(
                backendConnection.getProductOrder("amount==" + amount
                        + "store.id==" + storeId
                        + ";product.barcode==" + productBarcode));
        try {
            return productOrders.iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("The product order for product barcode " + productBarcode
                    + " with amount " + amount + " could not be found in store " + storeId);
        }
    }

    @Override
    public Collection<IProductOrder> queryAllOrders(long storeId) {
        return csvHelper.getProductOrders(
                backendConnection.getProductOrder("store.id==" + storeId));
    }

    private <T> T getSingleEntity(Function<String, Collection<T>> converter,
                                  String entity,
                                  long entityId) throws NotInDatabaseException {
        try {
            return converter.apply(backendConnection.getEntity(entity, "id==" + entityId)).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException(String.format(
                    "No matching entity of type '%s' and id '%d' found in database!",
                    entity, entityId));
        }
    }
}
