package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.BooleanParameterDAO;
import org.cocome.cloud.web.data.enterprisedata.BooleanParameterViewData;
import org.cocome.cloud.web.data.enterprisedata.RecipeOperationQuery;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;
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
public class BooleanParameterView extends AbstractView<BooleanParameterTO> {

    private static final Logger LOG = Logger.getLogger(BooleanParameterView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Param
    private Long operationId;

    @Inject
    private RecipeOperationQuery recipeOperationQuery;

    @Inject
    private BooleanParameterDAO dao;

    private BooleanParameterViewData selected;

    @PostConstruct
    public void createNewInstance() {
        this.selected = new BooleanParameterViewData(new BooleanParameterTO());
        if (operationId != null) {
            try {
                this.selected.getData().setOperation(recipeOperationQuery.find(operationId));
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to load instance", e);
                e.printStackTrace();
            }
        }
    }

    public BooleanParameterViewData getSelected() {
        return selected;
    }

    @Override
    protected BooleanParameterDAO getDAO() {
        return this.dao;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        if (this.selected.getData().getOperation() instanceof PlantOperationTO) {
            return NavigationElements.PLANT_OPERATION;
        }
        return NavigationElements.RECIPE;
    }

    @Override
    protected String getObjectName() {
        return "Boolean Parameter";
    }

}
