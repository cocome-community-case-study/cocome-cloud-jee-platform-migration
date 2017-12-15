package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.*;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointInteractionTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ParameterInteractionTO;
import org.omnifaces.cdi.Param;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@ViewScoped
public class EntryPointInteractionView extends AbstractView<EntryPointInteractionTO> {

    private static final Logger LOG = Logger.getLogger(EntryPointInteractionView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Param
    private Long operationId;

    @Inject
    private RecipeDAO recipeDAO;

    @Inject
    private EntryPointInteractionDAO dao;

    private EntryPointInteractionViewData selected;

    @PostConstruct
    public void createNewInstance() {
        this.selected = new EntryPointInteractionViewData(new EntryPointInteractionTO());
        if (operationId != null) {
            try {
                this.selected.getData().setRecipe(recipeDAO.find(operationId));
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to load instance", e);
                e.printStackTrace();
            }
        }
    }

    public EntryPointInteractionViewData getSelected() {
        return selected;
    }

    @Override
    protected EntryPointInteractionDAO getDAO() {
        return this.dao;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        return NavigationElements.RECIPE;
    }

    @Override
    protected String getObjectName() {
        return "Entry Point Interaction";
    }

}
