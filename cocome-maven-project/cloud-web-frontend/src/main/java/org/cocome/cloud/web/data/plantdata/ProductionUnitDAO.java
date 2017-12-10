package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.AbstractPlantDAO;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class ProductionUnitDAO extends AbstractPlantDAO<ProductionUnitTO, PlantTO> {

    @Override
    public Collection<ProductionUnitTO> queryAllByParentObj(IPlantManager iPlantManager, long id)
            throws NotInDatabaseException_Exception {
        return iPlantManager.queryProductionUnitsByPlantID(id);
    }

    @Override
    public ViewData<ProductionUnitTO> createViewDataInstance(ProductionUnitTO productionUnitTO) {
        return new ProductionUnitViewData(productionUnitTO);
    }

    @Override
    protected long createImpl(IPlantManager iPlantManager, ProductionUnitTO productionUnitTO)
            throws CreateException_Exception {
        return iPlantManager.createProductionUnit(productionUnitTO);
    }

    @Override
    protected void updateImpl(IPlantManager iPlantManager, ProductionUnitTO productionUnitTO)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iPlantManager.updateProductionUnit(productionUnitTO);
    }

    @Override
    protected void deleteImpl(IPlantManager iPlantManager, ProductionUnitTO productionUnitTO)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iPlantManager.deleteProductionUnit(productionUnitTO);
    }
}
