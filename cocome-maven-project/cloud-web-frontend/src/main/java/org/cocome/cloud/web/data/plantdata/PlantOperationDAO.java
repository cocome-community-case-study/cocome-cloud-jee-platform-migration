package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.cloud.web.data.enterprisedata.AbstractEnterpriseDAO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class PlantOperationDAO extends AbstractEnterpriseDAO<PlantOperationTO> {

    @Override
    protected Collection<PlantOperationTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long plantId) throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryPlantOperationsByPlantId(plantId);
    }

    @Override
    public ViewData<PlantOperationTO> createViewDataInstance(PlantOperationTO target) {
        return new PlantOperationViewData(target);
    }

    @Override
    protected long createImpl(IEnterpriseManager iEnterpriseManager, PlantOperationTO obj) throws CreateException_Exception {
        return iEnterpriseManager.createPlantOperation(obj);
    }

    @Override
    protected void updateImpl(IEnterpriseManager iEnterpriseManager, PlantOperationTO obj) throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.updatePlantOperation(obj);
    }

    @Override
    protected void deleteImpl(IEnterpriseManager iEnterpriseManager, PlantOperationTO obj) throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.deletePlantOperation(obj);
    }

    @Override
    protected PlantOperationTO queryById(IEnterpriseManager serviceClient, long dbId) throws NotInDatabaseException_Exception {
        return serviceClient.queryPlantOperationById(dbId);
    }
}
