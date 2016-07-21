package org.cocome.cloud.web.inventory.store;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import org.cocome.cloud.web.events.ChangeViewEvent;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;
import org.cocome.cloud.web.inventory.connection.IEnterpriseQuery;
import org.cocome.cloud.web.inventory.connection.IStoreQuery;

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
	private Store activeStore;
	private boolean hasChanged = false;

	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	@Inject
	IStoreQuery storeQuery;
	
	@Inject
	Event<ChangeViewEvent> changeViewEvent;
	
	
	@Override
	public Store getActiveStore() {
		LOG.debug("Active store is being retrieved from the database");
		if ((activeStore == null || hasChanged == true) && activeStoreID != STORE_ID_NOT_SET) {
			try {
				activeStore = enterpriseQuery.getStoreByID(activeStoreID);
			} catch (NotInDatabaseException_Exception e) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not retrieve the store!", null));
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
		activeStoreID = storeID;
		hasChanged = true;
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
	public String switchToStore(@NotNull Store store, String destination) {
		setActiveStoreID(store.getID());
		activeStore = store;
		changeViewEvent.fire(new ChangeViewEvent(NavigationViewStates.STORE_VIEW));
		return destination != null ? destination : NavigationElements.STORE_MAIN.getNavigationOutcome();
	}

	@Override
	public List<ProductWrapper> getAllStockItems() {
		if (isStoreSet()) {
			try {
				Store activeStore = getActiveStore();
				if (activeStore != null) {
					return storeQuery.queryStockItems(activeStore);
				}
			} catch (NotInDatabaseException_Exception e) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not retrieve the stock items!", null));
			}
		}
		return Collections.emptyList();
	}

	@Override
	public List<ProductWrapper> getStockReport(long storeID) {
		long currentStoreID = getActiveStoreID();
		setActiveStoreID(storeID);
		List<ProductWrapper> stockItems = getAllStockItems();
		setActiveStoreID(currentStoreID);
		return stockItems;
	}
}
