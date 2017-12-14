package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ParameterInteractionTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class ParameterInteractionDAO extends AbstractEnterpriseDAO<ParameterInteractionTO> {

    @Override
    protected Collection<ParameterInteractionTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long recipeId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryParameterInteractionsByRecipeId(recipeId);
    }

    @Override
    public ViewData<ParameterInteractionTO> createViewDataInstance(ParameterInteractionTO target) {
        return new ParameterInteractionViewData(target);
    }

    @Override
    protected long createImpl(IEnterpriseManager iEnterpriseManager, ParameterInteractionTO obj)
            throws CreateException_Exception {
        return iEnterpriseManager.createParameterInteraction(obj);
    }

    @Override
    protected void updateImpl(IEnterpriseManager iEnterpriseManager, ParameterInteractionTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.updateParameterInteraction(obj);
    }

    @Override
    protected void deleteImpl(IEnterpriseManager iEnterpriseManager, ParameterInteractionTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.deleteParameterInteraction(obj);
    }

    @Override
    protected ParameterInteractionTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryParameterInteractionById(dbId);
    }
}
