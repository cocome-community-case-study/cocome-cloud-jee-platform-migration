package org.cocome.cloud.web.data.enterprisedata;

import java.util.Collection;
import java.util.List;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.storedata.ProductWrapper;

public interface IEnterpriseDAO {
	public Collection<EnterpriseViewData> getAllEnterprises() throws NotInDatabaseException_Exception;
	
	public EnterpriseViewData getEnterpriseByID(long enterpriseID) throws NotInDatabaseException_Exception;
	
	public List<ProductWrapper> getAllProducts() throws NotInDatabaseException_Exception;
}
