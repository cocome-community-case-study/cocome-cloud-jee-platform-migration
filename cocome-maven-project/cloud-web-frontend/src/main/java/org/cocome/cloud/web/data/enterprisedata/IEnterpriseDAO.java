package org.cocome.cloud.web.data.enterprisedata;

import java.util.Collection;
import java.util.List;

import org.cocome.cloud.web.data.storedata.ProductWrapper;

public interface IEnterpriseDAO {
	public Collection<Enterprise> getAllEnterprises();
	
	public Enterprise getEnterpriseByID(long enterpriseID);
	
	public List<ProductWrapper> getAllProducts();
}
