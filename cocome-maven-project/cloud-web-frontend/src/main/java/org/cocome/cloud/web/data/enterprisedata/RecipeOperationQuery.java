package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
import org.cocome.cloud.web.frontend.enterprise.EnterpriseInformation;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeOperationTO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class RecipeOperationQuery {

    @Inject
    private EnterpriseQuery enterpriseQuery;

    @Inject
    private EnterpriseInformation enterpriseInfo;

    public RecipeOperationTO find(final long dbId) throws NotInDatabaseException_Exception {
        final IEnterpriseManager enterpriseManager = enterpriseQuery.lookupEnterpriseManager(
                enterpriseInfo.getActiveEnterprise().getId());
        return enterpriseManager.queryRecipeOperationById(dbId);
    }

    public List<RecipeOperationTO> getAllRecipeOperations() throws NotInDatabaseException_Exception {
        final IEnterpriseManager enterpriseManager = enterpriseQuery.lookupEnterpriseManager(
                enterpriseInfo.getActiveEnterprise().getId());
        return enterpriseManager.queryRecipeOperationsByEnterpriseId(enterpriseInfo.getActiveEnterprise().getId());
    }
}
