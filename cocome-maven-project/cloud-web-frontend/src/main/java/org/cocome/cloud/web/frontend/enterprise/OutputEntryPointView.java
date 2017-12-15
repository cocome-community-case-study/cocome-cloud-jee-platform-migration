package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.EntryPointViewData;
import org.cocome.cloud.web.data.enterprisedata.OutputEntryPointDAO;
import org.cocome.cloud.web.data.enterprisedata.RecipeOperationQuery;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;
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
public class OutputEntryPointView extends AbstractView<EntryPointTO> {

    private static final Logger LOG = Logger.getLogger(OutputEntryPointView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Param
    private Long operationId;

    @Inject
    private RecipeOperationQuery recipeOperationQuery;
    @Inject
    private OutputEntryPointDAO dao;

    private EntryPointViewData selected;

    @PostConstruct
    public void createNewInstance() {
        this.selected = new EntryPointViewData(new EntryPointTO());
        if (operationId != null) {
            try {
                this.selected.getData().setDirection(EntryPointTO.DirectionTO.OUTPUT);
                this.selected.getData().setOperation(recipeOperationQuery.find(operationId));
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to load instance", e);
                e.printStackTrace();
            }
        }
    }

    public EntryPointViewData getSelected() {
        return selected;
    }

    @Override
    protected OutputEntryPointDAO getDAO() {
        return this.dao;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        if(this.selected.getData().getOperation() instanceof PlantOperationTO) {
            return NavigationElements.PLANT_OPERATION;
        }
        return NavigationElements.RECIPE;
    }

    @Override
    protected String getObjectName() {
        return "Output Entry Point";
    }

}
