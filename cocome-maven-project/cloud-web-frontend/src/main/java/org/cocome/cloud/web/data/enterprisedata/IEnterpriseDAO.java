package org.cocome.cloud.web.data.enterprisedata;

import java.util.Collection;
import java.util.List;

import org.cocome.cloud.web.data.storedata.ProductWrapper;

public interface IEnterpriseDAO {
	public Collection<EnterpriseViewData> getAllEnterprises();
	
	public EnterpriseViewData getEnterpriseByID(long enterpriseID);
	
	public List<ProductWrapper> getAllProducts();
}
