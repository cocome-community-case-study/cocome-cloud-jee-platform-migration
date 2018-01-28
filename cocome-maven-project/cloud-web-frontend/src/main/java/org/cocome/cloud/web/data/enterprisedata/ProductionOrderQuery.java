package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ProductionOrderTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class ProductionOrderQuery extends AbstractEnterpriseQuery<ProductionOrderTO> {

    @Override
    protected Collection<ProductionOrderTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long parentId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryProductionOrdersByEnterpriseId(parentId);
    }

    @Override
    public ViewData<ProductionOrderTO> createViewDataInstance(ProductionOrderTO target) {
        return new ProductionOrderViewData(target);
    }

    @Override
    protected ProductionOrderTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryProductionOrderById(dbId);
    }
}
