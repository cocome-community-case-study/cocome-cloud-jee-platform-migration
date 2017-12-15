package org.cocome.cloud.web.frontend.store;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.EnterpriseDAO;
import org.cocome.cloud.web.data.storedata.IStoreDAO;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.cloud.web.events.ChangeViewEvent;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;

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
    private IStoreDAO storeDAO;

    @Inject
    private EnterpriseDAO enterpriseDAO;

    @Inject
    private Event<ChangeViewEvent> changeViewEvent;

    private List<ProductWrapper> stockItems = Collections.emptyList();

    private Map<Long, ProductWrapper> productsWithStockItems = new LinkedHashMap<>();

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

    public String submitStore() {
        LOG.debug("Submit store was called");
        if (isStoreSet()) {
            hasChanged = true;
            return NavigationElements.STORE_MAIN.getNavigationOutcome();
        } else {
            return "error";
        }
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

    public List<ProductWrapper> getAllStockItems() {
        return stockItems;
    }

    public List<ProductWrapper> getStockReport(long storeID) {
        long currentStoreID = getActiveStoreID();
        setActiveStoreID(storeID);
        queryStockItems();
        List<ProductWrapper> stockItems = getAllStockItems();
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

    private void mergeProductInformation() {
        productsWithStockItems = new LinkedHashMap<>();
        for (ProductWrapper stockItem : stockItems) {
            productsWithStockItems.put(stockItem.getBarcode(), stockItem);
        }
    }

    public Collection<ProductWrapper> getAllProductsWithStockItems() {
        return productsWithStockItems.values();
    }

    public void queryProductsWithStockItems() throws NotInDatabaseException_Exception {
        updateStockItems();
        for (ProductWrapper product : enterpriseDAO.getAllProducts()) {
            if (!productsWithStockItems.containsKey(product.getBarcode())) {
                productsWithStockItems.put(product.getBarcode(), product);
            }
        }
    }
}
