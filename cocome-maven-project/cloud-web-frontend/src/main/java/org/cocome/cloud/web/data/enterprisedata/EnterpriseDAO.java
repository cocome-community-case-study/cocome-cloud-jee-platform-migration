package org.cocome.cloud.web.data.enterprisedata;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.IEnterpriseQuery;
import org.cocome.cloud.web.data.storedata.ProductWrapper;

@Named
@RequestScoped
public class EnterpriseDAO implements IEnterpriseDAO {

	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	@Override
	public Collection<EnterpriseViewData> getAllEnterprises() throws NotInDatabaseException_Exception {
		return enterpriseQuery.getEnterprises();
	}

	@Override
	public EnterpriseViewData getEnterpriseByID(long enterpriseID) throws NotInDatabaseException_Exception {
		return enterpriseQuery.getEnterpriseByID(enterpriseID);
	}

	@Override
	public List<ProductWrapper> getAllProducts() throws NotInDatabaseException_Exception {
		return enterpriseQuery.getAllProducts();
	}

}
