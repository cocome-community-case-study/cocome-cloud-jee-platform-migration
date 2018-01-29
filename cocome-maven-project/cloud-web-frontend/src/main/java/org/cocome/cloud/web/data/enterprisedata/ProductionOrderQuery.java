package org.cocome.cloud.web.data.enterprisedata;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ProductionOrderTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class ProductionOrderQuery extends AbstractEnterpriseQuery<ProductionOrderTO> {

    private static final Logger LOG = Logger.getLogger(ProductionOrderQuery.class);

    @Override
    protected Collection<ProductionOrderTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long parentId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryProductionOrdersByEnterpriseId(parentId);
    }

    @Override
    public ViewData<ProductionOrderTO> createViewDataInstance(ProductionOrderTO target) {
        return new ProductionOrderViewData(target);
    }

    public Date getCurrentTime() {
        return new Date();
    }

    @Override
    protected ProductionOrderTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryProductionOrderById(dbId);
    }

    public void pollData(final EnterpriseViewData enterpriseViewData) {
        try {
            final IEnterpriseManager enterpriseManager = this.createServiceClient();
            final Collection<ProductionOrderTO> productionOrders =
                    enterpriseManager.queryProductionOrdersByEnterpriseId(enterpriseViewData.getId());
            cache.get(enterpriseViewData.getId()).clear();
            cache.get(enterpriseViewData.getId()).addAll(StreamUtil.ofNullable(productionOrders)
                    .map(this::createViewDataInstance)
                    .collect(Collectors.toList()));
        } catch (NotInDatabaseException_Exception e) {
            LOG.info(e);
            e.printStackTrace();
        }
    }
}
