package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.IEnterpriseQuery;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Named
@ApplicationScoped
public class PlantPersistence implements IPlantPersistence {

    @Inject
    IEnterpriseQuery enterpriseQuery;

    @Override
    public String updatePlant(@NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        plant.updatePlantInformation();
        if (enterpriseQuery.updatePlant(plant)) {
            plant.setEditingEnabled(false);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            Messages.getLocalizedMessage("plant.update.success"), null));
            return null;
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        Messages.getLocalizedMessage("plant.update.failed"), null));

        return "error";
    }

    @Override
    public String createPlant(long enterpriseID,
                              @NotNull String name,
                              @NotNull String location) throws NotInDatabaseException_Exception {
        if (enterpriseQuery.createPlant(enterpriseID, name, location)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            Messages.getLocalizedMessage("plant.create.success"), null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            Messages.getLocalizedMessage("plant.create.failed"), null));
        }

        return NavigationElements.SHOW_ENTERPRISES.getNavigationOutcome();
    }
}
