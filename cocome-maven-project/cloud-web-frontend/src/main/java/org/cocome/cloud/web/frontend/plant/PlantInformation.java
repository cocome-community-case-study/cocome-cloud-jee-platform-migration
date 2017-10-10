package org.cocome.cloud.web.frontend.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.enterprisedata.IEnterpriseDAO;
import org.cocome.cloud.web.data.plantdata.PlantDAO;
import org.cocome.cloud.web.data.plantdata.PlantViewData;
import org.cocome.cloud.web.events.ChangeViewEvent;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@SessionScoped
public class PlantInformation implements IPlantInformation, Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(PlantInformation.class);

    private long activePlantID = IPlantInformation.PLANT_ID_NOT_SET;
    private PlantViewData activePlant;
    private boolean hasChanged = false;

    @Inject
    PlantDAO plantDAO;

    @Inject
    IEnterpriseDAO enterpriseDAO;

    @Inject
    Event<ChangeViewEvent> changeViewEvent;

    @Override
    public PlantViewData getActivePlant() {
        if ((activePlant == null || hasChanged) && activePlantID != PLANT_ID_NOT_SET) {
            LOG.debug("Active plant is being retrieved from the database");
            try {
                activePlant = plantDAO.getPlantByID(activePlantID);
                hasChanged = false;
            } catch (NotInDatabaseException_Exception e) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not retrieve the plant!", null));
                return null;
            }
        } else {
            hasChanged = false;
        }
        return activePlant;
    }

    @Override
    public void setActivePlantID(long plantID) {
        LOG.debug("Active plant was set to id " + plantID);
        if (activePlantID != plantID) {
            activePlantID = plantID;
            hasChanged = true;
        }
    }

    @Override
    public long getActivePlantID() {
        return activePlantID;
    }

    @Override
    public String submitPlant() {
        LOG.debug("Submit plant was called");
        if (isPlantSet()) {
            hasChanged = true;
            return NavigationElements.PLANT_MAIN.getNavigationOutcome();
        } else {
            return "error";
        }
    }

    @Override
    public boolean isPlantSet() {
        return activePlantID != IPlantInformation.PLANT_ID_NOT_SET;
    }

    public void observeLoginEvent(@Observes LoginEvent event) {
        setActivePlantID(event.getStoreID());
    }

    @Override
    public String switchToPlant(@NotNull PlantViewData store, String destination) {
        setActivePlantID(store.getID());
        activePlant = store;
        hasChanged = true;
        changeViewEvent.fire(new ChangeViewEvent(NavigationViewStates.STORE_VIEW));
        return destination != null ? destination : NavigationElements.STORE_MAIN.getNavigationOutcome();
    }
}
