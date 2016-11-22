package org.cocome.cloud.web.data.enterprisedata;

import javax.validation.constraints.NotNull;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

/**
 * Holds information on an enterprise.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class EnterpriseViewData {
	private long id;
	private String name;
	
	public EnterpriseViewData() {
		id = 0;
		name = "N/A";
	}
	
	public EnterpriseViewData(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Creates a new EnterpriseTO object from an existing Enterprise object.
	 * 
	 * @param enterprise
	 * @return
	 */
	public static EnterpriseTO createEnterpriseTO(@NotNull EnterpriseViewData enterprise) {
		EnterpriseTO enterpriseTO = new EnterpriseTO();
		enterpriseTO.setId(enterprise.getId());
		enterpriseTO.setName(enterprise.getName());
		return enterpriseTO;
	}
	
	/**
	 * Creates a new Enterprise object from an existing EnterpriseTO object.
	 * 
	 * @param enterpriseTO
	 * @return
	 */
	public static EnterpriseViewData fromEnterpriseTO(@NotNull EnterpriseTO enterpriseTO) {
		return new EnterpriseViewData(enterpriseTO.getId(), enterpriseTO.getName());
	}
}
