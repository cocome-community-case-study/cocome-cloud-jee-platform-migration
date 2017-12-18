package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;

@Named
@RequestScoped
public class EnterpriseDAO {

    @Inject
    private EnterpriseQuery enterpriseQuery;

    public Collection<EnterpriseViewData> getAllEnterprises() throws NotInDatabaseException_Exception {
        return enterpriseQuery.getEnterprises();
    }

    public EnterpriseViewData getEnterpriseByID(long enterpriseID) throws NotInDatabaseException_Exception {
        return enterpriseQuery.getEnterpriseByID(enterpriseID);
    }

    public List<CustomProductTO> getAllCustomProducts() throws NotInDatabaseException_Exception {
        return enterpriseQuery.getAllCustomProducts();
    }

    public List<ProductWrapper> getAllProducts() throws NotInDatabaseException_Exception {
        return enterpriseQuery.getAllProducts();
    }

}
