package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointInteractionTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class EntryPointInteractionDAO extends AbstractEnterpriseDAO<EntryPointInteractionTO> {

    @Override
    protected Collection<EntryPointInteractionTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long recipeId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryEntryPointInteractionsByRecipeId(recipeId);
    }

    @Override
    public ViewData<EntryPointInteractionTO> createViewDataInstance(EntryPointInteractionTO target) {
        return new EntryPointInteractionViewData(target);
    }

    @Override
    protected long createImpl(IEnterpriseManager iEnterpriseManager, EntryPointInteractionTO obj)
            throws CreateException_Exception {
        return iEnterpriseManager.createEntryPointInteraction(obj);
    }

    @Override
    protected void updateImpl(IEnterpriseManager iEnterpriseManager, EntryPointInteractionTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.updateEntryPointInteraction(obj);
    }

    @Override
    protected void deleteImpl(IEnterpriseManager iEnterpriseManager, EntryPointInteractionTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.deleteEntryPointInteraction(obj);
    }

    @Override
    protected EntryPointInteractionTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryEntryPointInteractionById(dbId);
    }
}
