package org.cocome.cloud.web.frontend.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.plantdata.PlantOperationDAO;
import org.cocome.cloud.web.data.plantdata.PlantOperationViewData;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
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
public class PlantOperationView extends AbstractView<PlantOperationTO> {

    private static final Logger LOG = Logger.getLogger(PlantOperationView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Param
    private Long operationId;

    @Inject
    private PlantOperationDAO dao;

    @Inject
    private PlantInformation plantInformation;

    private PlantOperationViewData selected;

    @PostConstruct
    public void createNewInstance() {
        if (operationId != null) {
            try {
                this.selected = new PlantOperationViewData(dao.find(operationId));
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to load instance", e);
                e.printStackTrace();
            }
        } else {
            this.selected = new PlantOperationViewData(new PlantOperationTO());
            this.selected.getData().setPlant(plantInformation.getActivePlant().getData());
        }
    }

    public PlantOperationViewData getSelected() {
        return selected;
    }

    @Override
    protected PlantOperationDAO getDAO() {
        return this.dao;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        return NavigationElements.PLANT_OPERATION;
    }

    @Override
    protected String getObjectName() {
        return "Plant Operation";
    }
}
