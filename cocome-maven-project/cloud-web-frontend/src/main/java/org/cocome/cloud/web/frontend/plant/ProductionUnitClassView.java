package org.cocome.cloud.web.frontend.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.ThrowingProcedure;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;
import org.cocome.cloud.web.data.plantdata.PlantViewData;
import org.cocome.cloud.web.data.plantdata.ProductionUnitClassDAO;
import org.cocome.cloud.web.data.plantdata.ProductionUnitClassViewData;
import org.cocome.cloud.web.data.plantdata.ProductionUnitOperationViewData;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@ViewScoped
public class ProductionUnitClassView implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(ProductionUnitClassView.class);

    @Inject
    private PlantQuery plantQuery;

    @Inject
    private ProductionUnitClassDAO productionUnitClassDAO;

    @Inject
    private PlantInformation plantInformation;

    private List<ProductionUnitClassViewData> pucs;

    @PostConstruct
    public void queryPUCs() {
        LOG.info("Query PUCs");
        try {
            this.pucs = productionUnitClassDAO.queryPUCs(plantInformation.getActivePlant());
        } catch (NotInDatabaseException_Exception e) {
            LOG.error("Unable to load PUC list", e);
        }
    }

    public String importPUC(
            @NotNull String name,
            @NotNull String interfaceUrl,
            @NotNull PlantViewData plant) throws NotInDatabaseException_Exception {

        return processFacesAction(() -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(plant.getID());
                    plantManager.importProductionUnitClass(name, interfaceUrl, plant.getPlantTO());
                },
                Messages.get("message.import.success", Messages.get("puc.short.text")),
                Messages.get("message.import.failed", Messages.get("puc.short.text")),
                NavigationElements.PLANT_PUC);
    }

    public String createPUC(@NotNull String name,
                            @NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        return createAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(plant.getID());
                    final ProductionUnitClassTO pucTO = new ProductionUnitClassTO();
                    pucTO.setName(name);
                    pucTO.setPlant(plant.getPlantTO());
                    pucTO.setId(plantManager.createProductionUnitClass(pucTO));
                },
                "puc.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String updatePUC(@NotNull ProductionUnitClassViewData puc) throws NotInDatabaseException_Exception {
        return updateAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(puc.getPUC().getPlant().getId());
                    plantManager.updateProductionUnitClass(puc.getPUC());
                },
                "puc.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String deletePUC(@NotNull ProductionUnitClassViewData puc) throws NotInDatabaseException_Exception {
        return deleteAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(puc.getPUC().getPlant().getId());
                    plantManager.deleteProductionUnitClass(puc.getPUC());
                },
                "puc.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String createPUOperation(@NotNull String name,
                                    @NotNull String operationId,
                                    @NotNull long executionDurationInMillis,
                                    @NotNull long pucId) throws NotInDatabaseException_Exception {
        return createAction(
                () -> {
                    final PlantTO plant = this.plantInformation.getActivePlant().getPlantTO();
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(plant.getId());
                    final ProductionUnitOperationTO puOperationTO = new ProductionUnitOperationTO();
                    puOperationTO.setName(name);
                    puOperationTO.setOperationId(operationId);
                    puOperationTO.setExecutionDurationInMillis(executionDurationInMillis);
                    puOperationTO.setProductionUnitClass(plantManager.queryProductionUnitClassByID(pucId));
                    puOperationTO.setId(plantManager.createProductionUnitOperation(puOperationTO));
                },
                "puc_opr.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String updatePUOperation(@NotNull ProductionUnitOperationViewData puOperation)
            throws NotInDatabaseException_Exception {
        return updateAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(puOperation.getOperation()
                            .getProductionUnitClass().getPlant().getId());
                    plantManager.updateProductionUnitOperation(puOperation.getOperation());
                },
                "puc_opr.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String deletePUOperation(@NotNull ProductionUnitOperationViewData puOperation)
            throws NotInDatabaseException_Exception {
        return deleteAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(puOperation.getOperation()
                            .getProductionUnitClass().getPlant().getId());
                    plantManager.deleteProductionUnitOperation(puOperation.getOperation());
                },
                "puc_opr.short.text",
                NavigationElements.PLANT_PUC);
    }

    public List<ProductionUnitClassViewData> getPucs() {
        return pucs;
    }

    public void setPucs(final List<ProductionUnitClassViewData> pucs) {
        this.pucs = pucs;
    }

    private String createAction(final ThrowingProcedure<Exception> proc,
                                final String objectNameKey,
                                final NavigationElements nextNavigationElement)
            throws NotInDatabaseException_Exception {
        return processFacesAction(
                proc,
                Messages.get("message.create.success", Messages.get(objectNameKey)),
                Messages.get("message.create.failed", Messages.get(objectNameKey)),
                nextNavigationElement);
    }

    private String updateAction(final ThrowingProcedure<Exception> proc,
                                final String objectNameKey,
                                final NavigationElements nextNavigationElement)
            throws NotInDatabaseException_Exception {
        return processFacesAction(
                proc,
                Messages.get("message.update.success", Messages.get(objectNameKey)),
                Messages.get("message.update.failed", Messages.get(objectNameKey)),
                nextNavigationElement);
    }

    private String deleteAction(final ThrowingProcedure<Exception> proc,
                                final String objectNameKey,
                                final NavigationElements nextNavigationElement)
            throws NotInDatabaseException_Exception {
        return processFacesAction(proc,
                Messages.get("message.delete.success", Messages.get(objectNameKey)),
                Messages.get("message.delete.failed", Messages.get(objectNameKey)),
                nextNavigationElement);
    }

    private String processFacesAction(final ThrowingProcedure<Exception> proc,
                                      final String onSuccessMessage,
                                      final String onFailureMessage,
                                      final NavigationElements nextNavigationElement) {
        try {
            proc.run();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            onSuccessMessage, null));
            if (nextNavigationElement != null) {
                return nextNavigationElement.getNavigationOutcome();
            }
        } catch (final Exception e) {
            LOG.error(e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            onFailureMessage, null));
        }
        return null;
    }

}
