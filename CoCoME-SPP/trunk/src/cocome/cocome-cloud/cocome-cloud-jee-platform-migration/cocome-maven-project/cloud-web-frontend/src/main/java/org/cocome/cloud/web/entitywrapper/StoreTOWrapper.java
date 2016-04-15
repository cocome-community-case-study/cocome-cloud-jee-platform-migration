package org.cocome.cloud.web.entitywrapper;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;

public class StoreTOWrapper {

	private boolean editingEnabled;
	
	private StoreWithEnterpriseTO storeTO;
	
	private String newStoreName;
	
	private String newStoreLocation;
	
	private long newStoreID;
	
	public StoreTOWrapper(StoreWithEnterpriseTO store) {
		setStoreTO(store);
		setEnterpriseTO(store.getEnterpriseTO());
		setEditingEnabled(false);
	}

	public StoreTOWrapper() {
		setStoreTO(new StoreWithEnterpriseTO());
		storeTO.setId(0);
		storeTO.setLocation("");
		storeTO.setName("");
		
		setEnterpriseTO(new EnterpriseTO());
		getEnterpriseTO().setId(0);
		getEnterpriseTO().setName("");
	}

	public StoreWithEnterpriseTO getStoreTO() {
		return storeTO;
	}

	public void setStoreTO(StoreWithEnterpriseTO storeTO) {
		this.storeTO = storeTO;
	}
	
	public EnterpriseTO getEnterpriseTO() {
		return storeTO.getEnterpriseTO();
	}
	
	public void setEnterpriseTO(EnterpriseTO enterprise) {
		storeTO.setEnterpriseTO(enterprise);
	}

	public boolean isEditingEnabled() {
		return editingEnabled;
	}

	public void setEditingEnabled(boolean editingEnabled) {
		this.editingEnabled = editingEnabled;
	}

	public String getNewStoreName() {
		return newStoreName;
	}

	public void setNewStoreName(String newStoreName) {
		this.newStoreName = newStoreName;
	}

	public String getNewStoreLocation() {
		return newStoreLocation;
	}

	public void setNewStoreLocation(String newStoreLocation) {
		this.newStoreLocation = newStoreLocation;
	}

	public long getNewStoreID() {
		return newStoreID;
	}

	public void setNewStoreID(long newStoreID) {
		this.newStoreID = newStoreID;
	}
	
}
