package org.cocome.cloud.web.frontend.enterprise;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.EnterpriseViewData;
import org.cocome.cloud.web.data.storedata.StoreViewData;

/**
 * Interface for information regarding the currently active enterprise.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
//TODO: Why does this interface exist?
public interface IEnterpriseInformation {
	 Collection<EnterpriseViewData> getEnterprises() throws NotInDatabaseException_Exception;
	Collection<StoreViewData> getStores() throws NotInDatabaseException_Exception;
	
	long getActiveEnterpriseID();
	void setActiveEnterpriseID(long enterpriseID);
	EnterpriseViewData getActiveEnterprise() throws NotInDatabaseException_Exception;
	void setActiveEnterprise(@NotNull EnterpriseViewData enterprise);
	String submitActiveEnterprise();
	boolean isEnterpriseSubmitted();
	void setEnterpriseSubmitted(boolean submitted);
	boolean isEnterpriseSet();
	
	void setNewEnterpriseName(String name);
	String getNewEnterpriseName();
}
