package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeNodeTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class RecipeNodeDAO extends AbstractEnterpriseDAO<RecipeNodeTO> {

    @Override
    protected Collection<RecipeNodeTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long recipeId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryRecipeNodesByRecipeId(recipeId);
    }

    @Override
    public ViewData<RecipeNodeTO> createViewDataInstance(RecipeNodeTO target) {
        return new RecipeNodeViewData(target);
    }

    @Override
    protected long createImpl(IEnterpriseManager iEnterpriseManager, RecipeNodeTO obj)
            throws CreateException_Exception, NotInDatabaseException_Exception {
        return iEnterpriseManager.createRecipeNode(obj);
    }

    @Override
    protected void updateImpl(IEnterpriseManager iEnterpriseManager, RecipeNodeTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.updateRecipeNode(obj);
    }

    @Override
    protected void deleteImpl(IEnterpriseManager iEnterpriseManager, RecipeNodeTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.deleteRecipeNode(obj);
    }

    @Override
    protected RecipeNodeTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryRecipeNodeById(dbId);
    }
}
