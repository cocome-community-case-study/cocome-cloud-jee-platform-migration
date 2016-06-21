package org.cocome.cloud.web.inventory.store;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.inventory.connection.IEnterpriseQuery;

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
	
	
	@Override
	public Store getActiveStore() throws NotInDatabaseException_Exception {
		LOG.debug("Active store is being retrieved from the database");
		if ((activeStore == null || hasChanged == true) && activeStoreID != STORE_ID_NOT_SET) {
			activeStore = enterpriseQuery.getStoreByID(activeStoreID);
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
			return "showStockItems";
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
}
