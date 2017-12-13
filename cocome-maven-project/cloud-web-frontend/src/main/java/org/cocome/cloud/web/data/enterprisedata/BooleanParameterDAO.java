package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class BooleanParameterDAO extends AbstractEnterpriseDAO<BooleanParameterTO> {

    @Override
    protected Collection<BooleanParameterTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long parentId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryBooleanParametersByRecipeOperationId(parentId);
    }

    @Override
    public ViewData<BooleanParameterTO> createViewDataInstance(BooleanParameterTO obj) {
        return new BooleanParameterViewData(obj);
    }

    @Override
    protected long createImpl(IEnterpriseManager iEnterpriseManager, BooleanParameterTO obj)
            throws CreateException_Exception, NotInDatabaseException_Exception {
        return iEnterpriseManager.createBooleanParameter(obj);
    }

    @Override
    protected void updateImpl(IEnterpriseManager iEnterpriseManager, BooleanParameterTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.updateBooleanParameter(obj);
    }

    @Override
    protected void deleteImpl(IEnterpriseManager iEnterpriseManager, BooleanParameterTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.deleteBooleanParameter(obj);
    }

    @Override
    protected BooleanParameterTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryBooleanParameterById(dbId);
    }
}
