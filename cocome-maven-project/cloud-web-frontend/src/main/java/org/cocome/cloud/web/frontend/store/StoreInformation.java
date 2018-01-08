package org.cocome.cloud.web.frontend.store;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
import org.cocome.cloud.web.data.enterprisedata.EnterpriseDAO;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreDAO;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.cloud.web.events.ChangeViewEvent;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.store.OnDemandItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * Holds information about the currently active store.
 *
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
@SessionScoped
public class StoreInformation implements Serializable {
    public static final long STORE_ID_NOT_SET = Long.MIN_VALUE;

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(StoreInformation.class);

    private long activeStoreID = StoreInformation.STORE_ID_NOT_SET;
    private StoreViewData activeStore;
    private boolean hasChanged = false;

    @Inject
    private StoreDAO storeDAO;

    @Inject
    private EnterpriseDAO enterpriseDAO;

    @Inject
    private Event<ChangeViewEvent> changeViewEvent;

    private List<ProductWrapper<StockItemTO>> stockItems = Collections.emptyList();
    private List<ProductWrapper<OnDemandItemTO>> onDemandItems = Collections.emptyList();

    private Map<Long, ProductWrapper<StockItemTO>> productsWithStockItems = new LinkedHashMap<>();
    private Map<Long, ProductWrapper<OnDemandItemTO>> productsWithOnDemandItems = new LinkedHashMap<>();

    public StoreViewData getActiveStore() {
        if ((activeStore == null || hasChanged) && activeStoreID != STORE_ID_NOT_SET) {
            LOG.debug("Active store is being retrieved from the database");
            try {
                activeStore = storeDAO.getStoreByID(activeStoreID);
                hasChanged = false;
            } catch (NotInDatabaseException_Exception e) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not retrieve the store!", null));
                return null;
            }
        } else {
            hasChanged = false;
        }
        return activeStore;
    }

    public void setActiveStoreID(long storeID) {
        LOG.debug("Active store was set to id " + storeID);
        if (activeStoreID != storeID) {
            activeStoreID = storeID;
            hasChanged = true;
        }
    }

    public long getActiveStoreID() {
        return activeStoreID;
    }

    public boolean isStoreSet() {
        return activeStoreID != StoreInformation.STORE_ID_NOT_SET;
    }

    public void observeLoginEvent(@Observes LoginEvent event) {
        setActiveStoreID(event.getStoreID());
    }

    public String switchToStore(@NotNull StoreViewData store, String destination) {
        setActiveStoreID(store.getID());
        activeStore = store;
        hasChanged = true;
        changeViewEvent.fire(new ChangeViewEvent(NavigationViewStates.STORE_VIEW));
        return destination != null ? destination : NavigationElements.STORE_MAIN.getNavigationOutcome();
    }

    public List<ProductWrapper<StockItemTO>> getAllStockItems() {
        return stockItems;
    }

    public List<ProductWrapper<OnDemandItemTO>> getAllOnDemandItems() {
        return onDemandItems;
    }

    public List<ProductWrapper<StockItemTO>> getStockReport(long storeID) {
        long currentStoreID = getActiveStoreID();
        setActiveStoreID(storeID);
        queryStockItems();
        List<ProductWrapper<StockItemTO>> stockItems = getAllStockItems();
        setActiveStoreID(currentStoreID);
        return stockItems;
    }

    public void queryStockItems() {
        LOG.debug("Looking up stock items");
        boolean updated = false;

        if (isStoreSet()) {
            updated = updateStockItems();
        }

        if (!updated) {
            stockItems = Collections.emptyList();
        }
    }

    public void queryOnDemandItems() {
        LOG.debug("Looking up update items");
        boolean updated = false;

        if (isStoreSet()) {
            updated = updateOnDemandItems();
        }

        if (!updated) {
            onDemandItems = Collections.emptyList();
        }
    }

    private boolean updateStockItems() {
        StoreViewData activeStore = getActiveStore();
        if (activeStore != null) {
            try {
                stockItems = storeDAO.queryStockItems(activeStore);
            } catch (NotInDatabaseException_Exception e) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not retrieve the stock items!", null));
                return false;
            }

            mergeProductInformation();
        }
        return true;
    }

    private boolean updateOnDemandItems() {
        StoreViewData activeStore = getActiveStore();
        if (activeStore != null) {
            try {
                onDemandItems = storeDAO.queryOnDemandItems(activeStore);
            } catch (NotInDatabaseException_Exception e) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not retrieve the on-demand items!", null));
                return false;
            }

            mergeProductInformation();
        }
        return true;
    }

    private void mergeProductInformation() {
        productsWithStockItems = new LinkedHashMap<>();
        for (ProductWrapper<StockItemTO> stockItem : stockItems) {
            productsWithStockItems.put(stockItem.getBarcode(), stockItem);
        }
        productsWithOnDemandItems = new LinkedHashMap<>();
        for (ProductWrapper<OnDemandItemTO> onDemandItem : onDemandItems) {
            productsWithOnDemandItems.put(onDemandItem.getBarcode(), onDemandItem);
        }
    }

    public Collection<ProductWrapper<StockItemTO>> getAllProductsWithStockItems() {
        return productsWithStockItems.values();
    }

    public Collection<ProductWrapper<OnDemandItemTO>> getAllProductsWithOnDemandItems() {
        return productsWithOnDemandItems.values();
    }

    public void queryRegularProducts() throws NotInDatabaseException_Exception {
        updateStockItems();
        for (ProductWrapper<StockItemTO> product : enterpriseDAO.getAllProducts()) {
            if (!productsWithStockItems.containsKey(product.getBarcode())
                    && product.getProduct().getClass().equals(ProductTO.class)) {
                productsWithStockItems.put(product.getBarcode(), product);
            }
        }
    }

    public void queryCustomProducts() throws NotInDatabaseException_Exception {
        updateOnDemandItems();
        for (ProductWrapper<OnDemandItemTO> product : enterpriseDAO.getAllProducts()) {
            if (!productsWithOnDemandItems.containsKey(product.getBarcode())
                    && product.getProduct().getClass().equals(CustomProductTO.class)) {
                productsWithOnDemandItems.put(product.getBarcode(), product);
            }
        }
    }
}
