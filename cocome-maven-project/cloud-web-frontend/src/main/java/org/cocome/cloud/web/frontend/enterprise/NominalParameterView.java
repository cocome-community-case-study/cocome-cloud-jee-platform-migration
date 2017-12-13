package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.NominalParameterDAO;
import org.cocome.cloud.web.data.enterprisedata.NominalParameterViewData;
import org.cocome.cloud.web.data.plantdata.PlantOperationDAO;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;
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
public class NominalParameterView extends AbstractView<NominalParameterTO> {

    private static final Logger LOG = Logger.getLogger(NominalParameterView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    @Param
    private Long operationId;

    @Inject
    private PlantOperationDAO plantOperationDAO;

    @Inject
    private NominalParameterDAO dao;

    private NominalParameterViewData selected;

    @PostConstruct
    public void createNewInstance() {
        this.selected = new NominalParameterViewData(new NominalParameterTO());
        if (operationId != null) {
            try {
                this.selected.getData().setOperation(plantOperationDAO.find(operationId));
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to load instance", e);
                e.printStackTrace();
            }
        }
    }

    public NominalParameterViewData getSelected() {
        return selected;
    }

    @Override
    protected NominalParameterDAO getDAO() {
        return this.dao;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        return NavigationElements.PLANT_OPERATION;
    }

    @Override
    protected String getObjectName() {
        return "Nominal Parameter";
    }

}
