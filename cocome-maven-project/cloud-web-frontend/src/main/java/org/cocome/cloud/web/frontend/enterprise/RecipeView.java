package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.cloud.web.data.enterprisedata.*;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeNodeTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;
import org.omnifaces.cdi.Param;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@ViewScoped
public class RecipeView extends AbstractView<RecipeTO> {

    private static final Logger LOG = Logger.getLogger(RecipeView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Param
    private Long operationId;

    @Inject
    private RecipeDAO dao;

    @Inject
    private EnterpriseInformation plantInformation;

    @Inject
    private RecipeNodeDAO recipeNodeDAO;

    @Inject
    private InputEntryPointDAO inputEntryPointDAO;

    @Inject
    private OutputEntryPointDAO outputEntryPointDAO;

    @Inject
    private BooleanParameterDAO booleanParameterDAO;

    @Inject
    private NominalParameterDAO nominalParameterDAO;

    @Inject
    private RecipeOperationQuery recipeOperationQuery;

    private RecipeViewData selected;

    @PostConstruct
    public void createNewInstance() {
        if (operationId != null) {
            try {
                this.selected = new RecipeViewData(dao.find(operationId));
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to load instance", e);
                e.printStackTrace();
            }
        } else {
            this.selected = new RecipeViewData(new RecipeTO());
            this.selected.getData().setEnterprise(
                    EnterpriseViewData.createEnterpriseTO(plantInformation.getActiveEnterprise()));
        }
    }

    public RecipeViewData getSelected() {
        return selected;
    }

    @Override
    protected RecipeDAO getDAO() {
        return this.dao;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        return NavigationElements.RECIPE;
    }

    public List<EntryPointTO> getUsableEntryPoints(final RecipeViewData viewData) throws NotInDatabaseException_Exception {
        final List<EntryPointTO> entryPointList = new LinkedList<>();
        entryPointList.addAll(inputEntryPointDAO.getAllByParent(viewData.getData()));
        entryPointList.addAll(outputEntryPointDAO.getAllByParent(viewData.getData()));
        for (final ViewData<RecipeNodeTO> recipeNode : recipeNodeDAO.getAllByParentObj(viewData)) {
            entryPointList.addAll(inputEntryPointDAO.getAllByParent(
                    recipeNode.getData().getOperation()));
            entryPointList.addAll(outputEntryPointDAO.getAllByParent(
                    recipeNode.getData().getOperation()));
        }
        return entryPointList;
    }

    public List<ParameterTO> getUsableParameters(final RecipeViewData viewData) throws NotInDatabaseException_Exception {
        final List<ParameterTO> parameterList = new LinkedList<>();
        parameterList.addAll(booleanParameterDAO.getAllByParent(viewData.getData()));
        parameterList.addAll(nominalParameterDAO.getAllByParent(viewData.getData()));
        for (final ViewData<RecipeNodeTO> recipeNode : recipeNodeDAO.getAllByParentObj(viewData)) {
            parameterList.addAll(booleanParameterDAO.getAllByParent(
                    recipeNode.getData().getOperation()));
            parameterList.addAll(nominalParameterDAO.getAllByParent(
                    recipeNode.getData().getOperation()));
        }
        return parameterList;
    }


    public List<RecipeOperationTO> getUsableOperations() throws NotInDatabaseException_Exception {
        return recipeOperationQuery.getAllRecipeOperations();
    }

    @Override
    protected String getObjectName() {
        return "Recipe";
    }
}
