package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.AbstractPlantDAO;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class ProductionUnitClassDAO extends AbstractPlantDAO<ProductionUnitClassTO, PlantTO> {

    @Inject
    private PlantQuery plantQuery;

    public ProductionUnitClassTO get(final PlantViewData plant, final long pucId) throws NotInDatabaseException_Exception {
        final IPlantManager manager = plantQuery.lookupPlantManager(plant.getID());
        return manager.queryProductionUnitClassByID(pucId);
    }

    @Override
    public Collection<ProductionUnitClassTO> queryAllByParentObj(IPlantManager iPlantManager, long id) throws NotInDatabaseException_Exception {
        return iPlantManager.queryProductionUnitClassesByPlantID(id);
    }

    @Override
    public ViewData<ProductionUnitClassTO> createViewDataInstance(ProductionUnitClassTO target) {
        return new ProductionUnitClassViewData(target);
    }

    @Override
    protected long createImpl(IPlantManager iPlantManager, ProductionUnitClassTO viewData) throws CreateException_Exception {
        return iPlantManager.createProductionUnitClass(viewData);
    }

    @Override
    protected void updateImpl(IPlantManager iPlantManager, ProductionUnitClassTO viewData) throws UpdateException_Exception, NotInDatabaseException_Exception {
        iPlantManager.updateProductionUnitClass(viewData);
    }

    @Override
    protected void deleteImpl(IPlantManager iPlantManager, ProductionUnitClassTO viewData) throws UpdateException_Exception, NotInDatabaseException_Exception {
        iPlantManager.deleteProductionUnitClass(viewData);
    }
}
