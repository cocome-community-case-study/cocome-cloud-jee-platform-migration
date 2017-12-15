package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.BooleanParameterViewData;
import org.cocome.cloud.web.data.enterprisedata.ParameterInteractionDAO;
import org.cocome.cloud.web.data.enterprisedata.ParameterInteractionViewData;
import org.cocome.cloud.web.data.enterprisedata.RecipeDAO;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;
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
public class ParameterInteractionView extends AbstractView<ParameterInteractionTO> {

    private static final Logger LOG = Logger.getLogger(ParameterInteractionView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Param
    private Long operationId;

    @Inject
    private RecipeDAO recipeDAO;

    @Inject
    private ParameterInteractionDAO dao;

    private ParameterInteractionViewData selected;

    @PostConstruct
    public void createNewInstance() {
        this.selected = new ParameterInteractionViewData(new ParameterInteractionTO());
        if (operationId != null) {
            try {
                this.selected.getData().setRecipe(recipeDAO.find(operationId));
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to load instance", e);
                e.printStackTrace();
            }
        }
    }

    public ParameterInteractionViewData getSelected() {
        return selected;
    }

    @Override
    protected ParameterInteractionDAO getDAO() {
        return this.dao;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        return NavigationElements.RECIPE;
    }

    @Override
    protected String getObjectName() {
        return "Parameter Interaction";
    }
}
