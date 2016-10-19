package org.cocome.cloud.web.frontend.enterprise;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.Enterprise;
import org.cocome.cloud.web.data.storedata.Store;

/**
 * Interface for information regarding the currently active enterprise.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IEnterpriseInformation {
	public Collection<Enterprise> getEnterprises();
	public Collection<Store> getStores() throws NotInDatabaseException_Exception;
	
	public long getActiveEnterpriseID();
	public void setActiveEnterpriseID(long enterpriseID);
	public Enterprise getActiveEnterprise();
	public void setActiveEnterprise(@NotNull Enterprise enterprise);
	public String submitActiveEnterprise();
	public boolean isEnterpriseSubmitted();
	public void setEnterpriseSubmitted(boolean submitted);
	public boolean isEnterpriseSet();
	
	public void setNewEnterpriseName(String name);
	public String getNewEnterpriseName();
}
