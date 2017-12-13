package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class InputEntryPointDAO extends AbstractEnterpriseDAO<EntryPointTO> {

    @Override
    protected Collection<EntryPointTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long plantId) throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryInputEntryPointsByRecipeOperationId(plantId);
    }

    @Override
    public ViewData<EntryPointTO> createViewDataInstance(EntryPointTO target) {
        target.setDirection(EntryPointTO.DirectionTO.INPUT);
        return new EntryPointViewData(target);
    }

    @Override
    protected long createImpl(IEnterpriseManager iEnterpriseManager, EntryPointTO obj)
            throws CreateException_Exception, NotInDatabaseException_Exception {
        return iEnterpriseManager.createEntryPoint(obj);
    }

    @Override
    protected void updateImpl(IEnterpriseManager iEnterpriseManager, EntryPointTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.updateEntryPoint(obj);
    }

    @Override
    protected void deleteImpl(IEnterpriseManager iEnterpriseManager, EntryPointTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.deleteEntryPoint(obj);
    }

    @Override
    protected EntryPointTO queryById(IEnterpriseManager serviceClient, long dbId) throws NotInDatabaseException_Exception {
        return serviceClient.queryEntryPointById(dbId);
    }
}
