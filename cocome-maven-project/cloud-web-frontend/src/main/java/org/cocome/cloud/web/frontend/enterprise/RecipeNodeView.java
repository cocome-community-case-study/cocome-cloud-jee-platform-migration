package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.RecipeDAO;
import org.cocome.cloud.web.data.enterprisedata.RecipeNodeDAO;
import org.cocome.cloud.web.data.enterprisedata.RecipeNodeViewData;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeNodeTO;
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
public class RecipeNodeView extends AbstractView<RecipeNodeTO> {

    private static final Logger LOG = Logger.getLogger(RecipeNodeView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Param
    private Long operationId;

    @Inject
    private RecipeDAO recipeDAO;

    @Inject
    private RecipeNodeDAO dao;

    private RecipeNodeViewData selected;

    @PostConstruct
    public void createNewInstance() {
        this.selected = new RecipeNodeViewData(new RecipeNodeTO());
        if (operationId != null) {
            try {
                this.selected.getData().setRecipe(recipeDAO.find(operationId));
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to load instance", e);
                e.printStackTrace();
            }
        }
    }

    public RecipeNodeViewData getSelected() {
        return selected;
    }

    @Override
    protected RecipeNodeDAO getDAO() {
        return this.dao;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        return NavigationElements.RECIPE;
    }

    @Override
    protected String getObjectName() {
        return "Recipe Node";
    }

}
