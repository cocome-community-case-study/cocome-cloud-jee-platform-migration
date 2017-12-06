package org.cocome.cloud.web.data.plantdata;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.ThrowingSupplier;
import org.cocome.cloud.web.connector.enterpriseconnector.IEnterpriseQuery;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;
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
    private IEnterpriseQuery enterpriseQuery;

    @Inject
    private PlantQuery plantQuery;

    public String updatePlant(@NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> {
                    plant.updatePlantInformation();
                    return enterpriseQuery.updatePlant(plant);
                },
                "plant.update.success",
                "plant.update.failed",
                null);
    }

    public String createPlant(@NotNull long enterpriseID,
                              @NotNull String name,
                              @NotNull String location) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> enterpriseQuery.createPlant(enterpriseID, name, location),
                "plant.create.success",
                "plant.create.failed",
                NavigationElements.SHOW_ENTERPRISES);
    }

    public String createPUC(@NotNull String name,
                            @NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> plantQuery.createPUC(name, plant),
                "puc.create.success",
                "puc.create.failed",
                NavigationElements.PLANT_PUC);
    }

    public String updatePUC(@NotNull PUCWrapper puc) throws NotInDatabaseException_Exception {
        puc.submitEdit();
        return processFacesAction(
                () -> plantQuery.updatePUC(puc),
                "puc.create.success",
                "puc.create.failed",
                NavigationElements.PLANT_PUC);
    }

    public String deletePUC(@NotNull PUCWrapper puc) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> plantQuery.deletePUC(puc),
                "puc.create.success",
                "puc.create.failed",
                NavigationElements.PLANT_PUC);
    }

    private String processFacesAction(final ThrowingSupplier<Boolean, Exception> proc,
                                      final String onSuccessMessageKey,
                                      final String onFailureMessageKey,
                                      final NavigationElements nextNavigationElement) {
        try {
            if (proc.get()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                Messages.getLocalizedMessage(onSuccessMessageKey), null));
                if (nextNavigationElement != null) {
                    return nextNavigationElement.getNavigationOutcome();
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                Messages.getLocalizedMessage(onFailureMessageKey), null));
            }
            return null;
        } catch (final Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            Messages.getLocalizedMessage("internal.error.text"), null));
            return "error";
        }
    }
}
