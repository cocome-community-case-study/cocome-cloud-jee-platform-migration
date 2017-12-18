package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.ThrowingSupplier;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
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
public class PlantPersistence {
    @Inject
    private EnterpriseQuery enterpriseQuery;

    public String updatePlant(@NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> {
                    plant.updatePlantInformation();
                    return enterpriseQuery.updatePlant(plant);
                },
                Messages.get("message.create.success", Messages.get("plant.short.text")),
                Messages.get("message.create.success", Messages.get("plant.short.text")),
                null);
    }

    public String createPlant(@NotNull long enterpriseID,
                              @NotNull String name,
                              @NotNull String location) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> enterpriseQuery.createPlant(enterpriseID, name, location),
                Messages.get("message.create.success", Messages.get("plant.short.text")),
                Messages.get("message.create.success", Messages.get("plant.short.text")),
                NavigationElements.SHOW_PLANTS);
    }

    private String processFacesAction(final ThrowingSupplier<Boolean, Exception> proc,
                                      final String onSuccessMessage,
                                      final String onFailureMessage,
                                      final NavigationElements nextNavigationElement) {
        try {
            proc.get();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            onSuccessMessage, null));
            if (nextNavigationElement != null) {
                return nextNavigationElement.getNavigationOutcome();
            }
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            onFailureMessage, null));
        }
        return null;
    }
}
