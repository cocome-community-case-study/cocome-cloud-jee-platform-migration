package org.cocome.cloud.web.frontend.enterprise;

import java.util.Collection;
import java.util.LinkedList;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.backend.enterprise.IEnterpriseQuery;
import org.cocome.cloud.web.data.enterprise.Enterprise;
import org.cocome.cloud.web.data.store.Store;

import java.io.Serializable;

/**
 * Holds information about the currently active enterprise.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
@SessionScoped
public class EnterpriseInformation implements Serializable, IEnterpriseInformation {
	private static final long serialVersionUID = 1L;

	private long activeEnterpriseID = Long.MIN_VALUE;
	private Enterprise activeEnterprise;
	
	private String newEnterpriseName = null;
	
	private static final Logger LOG = Logger.getLogger(EnterpriseInformation.class);

	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	private boolean enterpriseSubmitted = false;
	
	@Override
	public Collection<Enterprise> getEnterprises() {
		return enterpriseQuery.getEnterprises();
	}

	@Override
	public Collection<Store> getStores() throws NotInDatabaseException_Exception {
		if (activeEnterpriseID != Long.MIN_VALUE) {
			return enterpriseQuery.getStores(activeEnterpriseID);
		}
		// TODO Throw custom exception to signal error here
		return new LinkedList<Store>();
	}

	@Override
	public long getActiveEnterpriseID() {
		return activeEnterpriseID;
	}

	@Override
	public void setActiveEnterpriseID(long enterpriseID) {
		LOG.debug("Active enterprise was set to " + enterpriseID);
		this.activeEnterpriseID = enterpriseID;
	}

	@Override
	public Enterprise getActiveEnterprise() {
		if (activeEnterprise == null && activeEnterpriseID != Long.MIN_VALUE) {
			activeEnterprise = enterpriseQuery.getEnterpriseByID(activeEnterpriseID);
		}
		return activeEnterprise;
	}

	@Override
	public String submitActiveEnterprise() {
		if (isEnterpriseSet()) {
			enterpriseSubmitted = true;
			// return null to only refresh the current form and not create a new view
			return null;
		} else {
			return "error";
		}
	}
	
	@Override
	public boolean isEnterpriseSubmitted() {
		return enterpriseSubmitted;
	}

	@Override
	public boolean isEnterpriseSet() {
		return activeEnterpriseID != Long.MIN_VALUE;
	}

	@Override
	public void setEnterpriseSubmitted(boolean submitted) {
		this.enterpriseSubmitted = submitted;
	}

	@Override
	public void setActiveEnterprise(@NotNull Enterprise enterprise) {
		activeEnterpriseID = enterprise.getId();
		this.activeEnterprise = enterprise;
	}

	@Override
	public void setNewEnterpriseName(String name) {
		newEnterpriseName = name;
	}

	@Override
	public String getNewEnterpriseName() {
		return newEnterpriseName;
	}
}
