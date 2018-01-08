package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterTO;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@ViewScoped
public class ParameterQuery extends AbstractEnterpriseQuery<ParameterTO> {

    @Override
    protected Collection<ParameterTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long parentId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryParametersByRecipeOperationId(parentId);
    }

    @Override
    public ViewData<ParameterTO> createViewDataInstance(ParameterTO obj) {
        return new ParameterViewData(obj);
    }

    @Override
    protected ParameterTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryBooleanParameterById(dbId);
    }
}
