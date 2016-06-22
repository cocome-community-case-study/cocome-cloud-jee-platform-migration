package org.cocome.cloud.web.inventory.store;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

/**
 * Holds informtaion about a Store.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class Store {
	private long id;
	private EnterpriseTO enterprise;
	private String name;
	private String location;
	private boolean editingEnabled = false;
	private String newName;
	private String newLocation;
	
	public Store(long id, EnterpriseTO enterprise, String location, String name) {
		this.id = id;
		setEnterprise(enterprise);
		this.name = name;
		this.location = location;
		this.setNewLocation(location);
		this.setNewName(name);
	}

	public long getID() {
		return id;
	}
	
	public void setID(long storeID) {
		this.id = storeID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String storeName) {
		this.name = storeName;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String storeLocation) {
		this.location = storeLocation;
	}

	public boolean isEditingEnabled() {
		return editingEnabled;
	}

	public void setEditingEnabled(boolean editingEnabled) {
		if (!editingEnabled) {
			newName = name;
			newLocation = location;
		}
		this.editingEnabled = editingEnabled;
	}

	public EnterpriseTO getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseTO enterprise) {
		this.enterprise = enterprise;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewLocation() {
		return newLocation;
	}

	public void setNewLocation(String newLocation) {
		this.newLocation = newLocation;
	}
	
	public void updateStoreInformation() {
		name = newName;
		location = newLocation;
	}
}
