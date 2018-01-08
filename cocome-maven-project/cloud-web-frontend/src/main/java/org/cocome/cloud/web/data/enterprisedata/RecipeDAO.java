package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

@Named
@SessionScoped
public class RecipeDAO extends AbstractEnterpriseDAO<RecipeTO> {

    @Override
    protected Collection<RecipeTO> queryAllByParentObj(IEnterpriseManager iEnterpriseManager, long enterpriseId)
            throws NotInDatabaseException_Exception {
        return iEnterpriseManager.queryRecipesByEnterpriseId(enterpriseId);
    }

    @Override
    public ViewData<RecipeTO> createViewDataInstance(RecipeTO target) {
        return new RecipeViewData(target);
    }

    @Override
    protected long createImpl(IEnterpriseManager iEnterpriseManager, RecipeTO obj)
            throws CreateException_Exception {
        return iEnterpriseManager.createRecipe(obj);
    }

    @Override
    protected void updateImpl(IEnterpriseManager iEnterpriseManager, RecipeTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.updateRecipe(obj);
    }

    @Override
    protected void deleteImpl(IEnterpriseManager iEnterpriseManager, RecipeTO obj)
            throws UpdateException_Exception, NotInDatabaseException_Exception {
        iEnterpriseManager.deleteRecipe(obj);
    }

    @Override
    protected RecipeTO queryById(IEnterpriseManager serviceClient, long dbId)
            throws NotInDatabaseException_Exception {
        return serviceClient.queryRecipeById(dbId);
    }

    public RecipeTO queryRecipe(long barcode) throws NotInDatabaseException_Exception {
        return this.createServiceClient().queryRecipeByCustomProductBarcode(barcode);
    }
}
