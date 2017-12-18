package org.cocome.cloud.web.connector.storeconnector;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.*;
import org.cocome.cloud.registry.service.Names;
import org.cocome.cloud.web.data.storedata.OrderItem;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.application.store.ItemTO;
import org.cocome.tradingsystem.inventory.application.store.OnDemandItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Implements the store query interface to retrieve store related information.
 * Uses the web service interface from CoCoMEs logic.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@RequestScoped
public class StoreQuery implements IStoreQuery {
    private static final Logger LOG = Logger.getLogger(StoreQuery.class);

    private IStoreManager storeManager;

    @Inject
    private long defaultStoreIndex;

    @Inject
    private IApplicationHelper applicationHelper;

    public IStoreManager lookupStoreManager(long storeID) throws NotInDatabaseException_Exception {
        try {
            LOG.debug(String.format("Looking up responsible store manager for store %d", storeID));
            return applicationHelper.getComponent(Names.getStoreManagerRegistryName(storeID),
                    IStoreManagerService.SERVICE, IStoreManagerService.class).getIStoreManagerPort();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            if (storeID == defaultStoreIndex) {
                LOG.error("Got exception while retrieving store manager location: " + e.getMessage());
                e.printStackTrace();
                throw new NotInDatabaseException_Exception(e.getMessage());
            } else {
                return lookupStoreManager(defaultStoreIndex);
            }
        }
    }

    @Override
    public List<ProductWrapper<StockItemTO>> queryStockItems(@NotNull StoreViewData store)
            throws NotInDatabaseException_Exception {
        long storeID = store.getID();
        LOG.debug("Querying stock items: Looking up store server.");
        storeManager = lookupStoreManager(storeID);
        List<ProductWrapper<StockItemTO>> stockItems = new LinkedList<>();
        LOG.debug("Querying stock items: Querying stock items from store server.");
        List<ProductWithSupplierAndItemTO> items = storeManager.getProductsWithStockItems(storeID);
        LOG.debug("Querying stock items: Creating product wrappers.");
        for (ProductWithSupplierAndItemTO item : items) {
            stockItems.add(new ProductWrapper<>(item.getProductTO(), (StockItemTO) item.getItemTO(), store));
        }
        return stockItems;
    }

    @Override
    public List<ProductWrapper<OnDemandItemTO>> queryOnDemandItems(@NotNull StoreViewData store)
            throws NotInDatabaseException_Exception {
        long storeID = store.getID();
        LOG.debug("Querying stock items: Looking up store server.");
        storeManager = lookupStoreManager(storeID);
        List<ProductWrapper<OnDemandItemTO>> onDemamndItems = new LinkedList<>();
        LOG.debug("Querying stock items: Querying stock items from store server.");
        List<ProductWithSupplierAndItemTO> items = storeManager.getProductsWithOnDemandItems(storeID);
        LOG.debug("Querying stock items: Creating product wrappers.");
        for (ProductWithSupplierAndItemTO item : items) {
            onDemamndItems.add(new ProductWrapper<>(item.getProductTO(), (OnDemandItemTO) item.getItemTO(), store));
        }
        return onDemamndItems;
    }

    @Override
    public boolean createItem(@NotNull StoreViewData store, @NotNull ProductWrapper product) {
        long storeID = store.getID();

        try {
            storeManager = lookupStoreManager(storeID);
            ProductWithItemTO stockItemTO = convertToProductWithItemTO(product);
            storeManager.createItem(storeID, stockItemTO);
        } catch (CreateException_Exception | NotInDatabaseException_Exception e) {
            LOG.error(String.format("Error while creating item: %s\n", e.getMessage()), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateItem(@NotNull StoreViewData store, @NotNull ProductWrapper item) {
        long storeID = store.getID();
        try {
            storeManager = lookupStoreManager(storeID);
            storeManager.updateItem(storeID, new ProductWithItemTO(item.getItemTO(), item.getProduct()));
            return true;
        } catch (NotInDatabaseException_Exception | UpdateException_Exception e) {
            LOG.error(String.format("Error while updating item: %s\n", e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean orderProducts(@NotNull StoreViewData store, @NotNull Collection<OrderItem> items) {
        ComplexOrderTO orderTO = createComplexOrderTO(items);

        long storeID = store.getID();

        try {
            storeManager = lookupStoreManager(storeID);
            storeManager.orderProducts(storeID, orderTO);
            return true;
        } catch (NotInDatabaseException_Exception | CreateException_Exception | UpdateException_Exception e) {
            LOG.error(String.format("Error while ordering products: %s\n", e.getMessage()), e);
        }
        return false;
    }

    private ComplexOrderTO createComplexOrderTO(Collection<OrderItem> items) {
        ComplexOrderTO orderTO = new ComplexOrderTO();
        // Ordering and delivery date will be set by the store server
        orderTO.setDeliveryDate(null);
        orderTO.setOrderingDate(null);

        List<ComplexOrderEntryTO> orderEntries = new ArrayList<>(items.size());

        for (OrderItem item : items) {
            orderEntries.add(OrderItem.convertToOrderEntryTO(item));
        }
        orderTO.setOrderEntryTOs(orderEntries);
        return orderTO;
    }

    @Override
    public List<ComplexOrderTO> getAllOrders(@NotNull StoreViewData store) {
        long storeID = store.getID();

        try {
            storeManager = lookupStoreManager(storeID);
            return storeManager.getOutstandingOrders(storeID);
        } catch (NotInDatabaseException_Exception e) {
            LOG.error(String.format("Error while getting orders: %s\n", e.getMessage()), e);
        }
        return Collections.emptyList();
    }

    @Override
    public ComplexOrderTO getOrderByID(@NotNull StoreViewData store, long orderID) {
        long storeID = store.getID();

        try {
            storeManager = lookupStoreManager(storeID);
            return storeManager.getOrder(storeID, orderID);
        } catch (NotInDatabaseException_Exception e) {
            LOG.error(String.format("Error while getting order with id %d: %s\n", orderID, e.getMessage()), e);
        }
        return null;
    }

    @Override
    public boolean rollInOrder(@NotNull StoreViewData store, long orderID) {
        long storeID = store.getID();

        try {
            storeManager = lookupStoreManager(storeID);
            storeManager.rollInReceivedOrder(storeID, orderID);
            return true;
        } catch (NotInDatabaseException_Exception | InvalidRollInRequestException_Exception | UpdateException_Exception e) {
            LOG.error(String.format("Error while rolling in order: %s\n", e.getMessage()), e);
        }
        return false;
    }

    private static ProductWithItemTO convertToProductWithItemTO(ProductWrapper product) {
        final ProductWithItemTO productTO = new ProductWithItemTO();
        productTO.setProduct(product.getProduct());

        ItemTO itemTO = product.getItemTO();

        if (itemTO == null) {
            itemTO = getNewItemTO(productTO.getProduct());
        }
        productTO.setItem(itemTO);
        return productTO;
    }

    private static ItemTO getNewItemTO(final ProductTO productTO) {
        if (productTO instanceof CustomProductTO) {
            final OnDemandItemTO onDemandItemTO = new OnDemandItemTO();
            onDemandItemTO.setSalesPrice(0.0);
            return onDemandItemTO;
        }
        final StockItemTO stockItemTO = new StockItemTO();
        stockItemTO.setAmount(0);
        stockItemTO.setIncomingAmount(0);
        stockItemTO.setMaxStock(0);
        stockItemTO.setMinStock(0);
        stockItemTO.setSalesPrice(0.0);
        return stockItemTO;
    }

}
