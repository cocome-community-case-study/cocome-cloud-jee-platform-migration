package org.cocome.cloud.web.data.enterprisedata;

import java.util.Collection;
import java.util.List;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.storedata.ProductWrapper;

public interface IEnterpriseDAO {
    Collection<EnterpriseViewData> getAllEnterprises() throws NotInDatabaseException_Exception;

    EnterpriseViewData getEnterpriseByID(long enterpriseID) throws NotInDatabaseException_Exception;

    List<ProductWrapper> getAllProducts() throws NotInDatabaseException_Exception;
}
