package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.AbstractPlantDAO;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class ProductionUnitOperationDAO extends AbstractPlantDAO<ProductionUnitOperationTO> {

    @Override
    protected Collection<ProductionUnitOperationTO> queryAllByParentObj(IPlantManager iPlantManager, long id)
            throws NotInDatabaseException_Exception {
        return iPlantManager.queryProductionUnitOperationsByProductionUnitClassID(id);
    }

    @Override
    public ViewData<ProductionUnitOperationTO> createViewDataInstance(ProductionUnitOperationTO target) {
        return new ProductionUnitOperationViewData(target);
    }

    @Override
    protected long createImpl(IPlantManager iPlantManager, ProductionUnitOperationTO operation)
            throws CreateException_Exception {
        return iPlantManager.createProductionUnitOperation(operation);
    }

    @Override
    protected void updateImpl(IPlantManager iPlantManager, ProductionUnitOperationTO operation)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iPlantManager.updateProductionUnitOperation(operation);
    }

    @Override
    protected void deleteImpl(IPlantManager iPlantManager, ProductionUnitOperationTO operation)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iPlantManager.deleteProductionUnitOperation(operation);
    }

    @Override
    protected ProductionUnitOperationTO queryById(IPlantManager serviceClient, long dbId) throws NotInDatabaseException_Exception {
        return serviceClient.queryProductionUnitOperationByID(dbId);
    }
}
