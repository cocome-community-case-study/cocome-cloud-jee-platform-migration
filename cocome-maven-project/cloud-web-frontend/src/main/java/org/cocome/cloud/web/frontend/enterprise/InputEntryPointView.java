package org.cocome.cloud.web.frontend.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.data.enterprisedata.EntryPointViewData;
import org.cocome.cloud.web.data.enterprisedata.InputEntryPointDAO;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.plant.PlantInformation;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;

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
public class InputEntryPointView extends AbstractView<EntryPointTO> {

    private static final Logger LOG = Logger.getLogger(InputEntryPointView.class);

    private static final long serialVersionUID = 1L;

    @Inject
    private InputEntryPointDAO dao;

    @Inject
    private PlantInformation plantInformation;

    private EntryPointViewData selected;

    @PostConstruct
    public void createNewInstance() {
        this.selected = new EntryPointViewData(new EntryPointTO());
    }

    public EntryPointViewData getSelected() {
        return selected;
    }

    @Override
    protected InputEntryPointDAO getDAO() {
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

    public void setOperationId(long operationId) {
        /*if (this.selected != null && this.getOperationId() == operationId) {
            return;
        }
        final PlantOperationTO operation;
        try {
            operation = this.dao.find(operationId);
        } catch (NotInDatabaseException_Exception e) {
            LOG.error("Unable to fetch Plant Operation", e);
            throw new IllegalArgumentException(e);
        }
        this.selected = new PlantOperationViewData(operation);*/
    }

    public long getOperationId() {
        return this.selected.getData().getId();
    }
}
