package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class NominalParameterDAO extends AbstractEnterpriseDAO<NominalParameterTO> {

    @Override
    protected Collection<NominalParameterTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long parentId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryNominalParametersByRecipeOperationId(parentId);
    }

    @Override
    public ViewData<NominalParameterTO> createViewDataInstance(NominalParameterTO obj) {
        return new NominalParameterViewData(obj);
    }

    @Override
    protected long createImpl(IEnterpriseManager iEnterpriseManager, NominalParameterTO obj)
            throws CreateException_Exception, NotInDatabaseException_Exception {
        return iEnterpriseManager.createNominalParameter(obj);
    }

    @Override
    protected void updateImpl(IEnterpriseManager iEnterpriseManager, NominalParameterTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.updateNominalParameter(obj);
    }

    @Override
    protected void deleteImpl(IEnterpriseManager iEnterpriseManager, NominalParameterTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.deleteNominalParameter(obj);
    }

    @Override
    protected NominalParameterTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryNominalParameterById(dbId);
    }
}
