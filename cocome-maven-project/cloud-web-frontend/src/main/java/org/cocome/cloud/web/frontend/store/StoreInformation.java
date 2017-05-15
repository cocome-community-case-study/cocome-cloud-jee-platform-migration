package org.cocome.cloud.web.frontend.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.IEnterpriseDAO;
import org.cocome.cloud.web.data.storedata.IStoreDAO;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.cloud.web.events.ChangeViewEvent;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;

/**
 * Holds information about the currently active store.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
@SessionScoped
public class StoreInformation implements IStoreInformation, Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(StoreInformation.class);

	private long activeStoreID = IStoreInformation.STORE_ID_NOT_SET;
	private StoreViewData activeStore;
	private boolean hasChanged = false;

//	@Inject
//	IEnterpriseQuery enterpriseQuery;
//
//	@Inject
//	IStoreQuery storeQuery;

	@Inject
	IStoreDAO storeDAO;
	
	@Inject
	IEnterpriseDAO enterpriseDAO;
	
	@Inject
	Event<ChangeViewEvent> changeViewEvent;

	private List<ProductWrapper> stockItems = Collections.emptyList();

	private Map<Long, ProductWrapper> productsWithStockItems = new LinkedHashMap<>();

	@Override
	public StoreViewData getActiveStore() {
		if ((activeStore == null || hasChanged == true) && activeStoreID != STORE_ID_NOT_SET) {
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

	@Override
	public void setActiveStoreID(long storeID) {
		LOG.debug("Active store was set to id " + storeID);
		if (activeStoreID != storeID) {
			activeStoreID = storeID;
			hasChanged = true;
		}
	}

	@Override
	public long getActiveStoreID() {
		return activeStoreID;
	}

	@Override
	public String submitStore() {
		LOG.debug("Submit store was called");
		if (isStoreSet()) {
			hasChanged = true;
			return NavigationElements.STORE_MAIN.getNavigationOutcome();
		} else {
			return "error";
		}
	}

	@Override
	public boolean isStoreSet() {
		return activeStoreID != IStoreInformation.STORE_ID_NOT_SET;
	}

	public void observeLoginEvent(@Observes LoginEvent event) {
		setActiveStoreID(event.getStoreID());
	}

	@Override
	public String switchToStore(@NotNull StoreViewData store, String destination) {
		setActiveStoreID(store.getID());
		activeStore = store;
		hasChanged = true;
		changeViewEvent.fire(new ChangeViewEvent(NavigationViewStates.STORE_VIEW));
		return destination != null ? destination : NavigationElements.STORE_MAIN.getNavigationOutcome();
	}

	@Override
	public List<ProductWrapper> getAllStockItems() {
		return stockItems;
	}

	@Override
	public List<ProductWrapper> getStockReport(long storeID) {
		long currentStoreID = getActiveStoreID();
		setActiveStoreID(storeID);
		queryStockItems();
		List<ProductWrapper> stockItems = getAllStockItems();
		setActiveStoreID(currentStoreID);
		return stockItems;
	}

	@Override
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

	@Override
	public Collection<ProductWrapper> getAllProductsWithStockItems() {
		return productsWithStockItems.values();
	}

	@Override
	public void queryProductsWithStockItems() throws NotInDatabaseException_Exception {
		updateStockItems();
		for (ProductWrapper product : enterpriseDAO.getAllProducts()) {
			if (!productsWithStockItems.containsKey(product.getBarcode())) {
				productsWithStockItems.put(product.getBarcode(), product);
			}
		}
	}
}
