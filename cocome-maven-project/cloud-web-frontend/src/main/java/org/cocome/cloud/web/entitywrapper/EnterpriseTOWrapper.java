package org.cocome.cloud.web.entitywrapper;

import org.cocome.logic.stub.EnterpriseTO;


public class EnterpriseTOWrapper {
	private EnterpriseTO enterpriseTO;
	
	public EnterpriseTOWrapper(EnterpriseTO enterprise) {
		this.enterpriseTO = enterprise;
	}
	
	public EnterpriseTO getEnterpriseTO() {
		return enterpriseTO;
	}
	
	public void setEnterpriseTO(EnterpriseTO enteprise) {
		this.enterpriseTO = enteprise;
	}
	
	public String getName() {
		return enterpriseTO.getName();
	}
	
	public void setName(String name) {
		enterpriseTO.setName(name);
	}
	
	public long getId() {
		return enterpriseTO.getId();
	}
	
	public void setId(long id) {
		enterpriseTO.setId(id);
	}
}
