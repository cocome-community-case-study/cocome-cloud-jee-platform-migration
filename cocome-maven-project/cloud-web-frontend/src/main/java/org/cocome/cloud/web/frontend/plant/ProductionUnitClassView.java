package org.cocome.cloud.web.frontend.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.cloud.web.data.plantdata.*;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@ViewScoped
public class ProductionUnitClassView extends AbstractView {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(ProductionUnitClassView.class);

    @Inject
    private ProductionUnitClassDAO productionUnitClassDAO;

    @Inject
    private ProductionUnitOperationDAO productionUnitOperationDAO;

    @Inject
    private PlantInformation plantInformation;

    private Collection<ViewData<ProductionUnitClassTO>> pucs;

    @PostConstruct
    public void queryPUCs() {
        LOG.info("Query PUCs");
        try {
            this.pucs = productionUnitClassDAO.getAllByParentObj(plantInformation.getActivePlant());
        } catch (NotInDatabaseException_Exception e) {
            LOG.error("Unable to load PUC list", e);
        }
    }

    public String importPUC(
            @NotNull String name,
            @NotNull String interfaceUrl,
            @NotNull PlantViewData plant) throws NotInDatabaseException_Exception {

        return processFacesAction(() -> {
                    productionUnitClassDAO.importPUC(name, interfaceUrl, plant);
                },
                Messages.get("message.import.success", Messages.get("puc.short.text")),
                Messages.get("message.import.failed", Messages.get("puc.short.text")),
                NavigationElements.PLANT_PUC);
    }

    public String createPUC(@NotNull String name,
                            @NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        return createAction(
                () -> {
                    final ProductionUnitClassTO pucTO = new ProductionUnitClassTO();
                    pucTO.setName(name);
                    pucTO.setPlant(plant.getPlantTO());
                    productionUnitClassDAO.create(new ProductionUnitClassViewData(pucTO));
                },
                "puc.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String updatePUC(@NotNull ProductionUnitClassViewData puc) throws NotInDatabaseException_Exception {
        return updateAction(
                () -> productionUnitClassDAO.update(puc),
                "puc.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String deletePUC(@NotNull ProductionUnitClassViewData puc) throws NotInDatabaseException_Exception {
        return deleteAction(
                () -> productionUnitClassDAO.delete(puc),
                "puc.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String createPUOperation(@NotNull String name,
                                    @NotNull String operationId,
                                    @NotNull long executionDurationInMillis,
                                    @NotNull long pucId) throws NotInDatabaseException_Exception {
        return createAction(
                () -> {
                    final ProductionUnitOperationTO puOperationTO = new ProductionUnitOperationTO();
                    puOperationTO.setName(name);
                    puOperationTO.setOperationId(operationId);
                    puOperationTO.setExecutionDurationInMillis(executionDurationInMillis);

                    puOperationTO.setProductionUnitClass(productionUnitClassDAO
                            .get(this.plantInformation.getActivePlant(), pucId));
                    productionUnitOperationDAO.create(new ProductionUnitOperationViewData(puOperationTO));
                },
                "puc_opr.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String updatePUOperation(@NotNull ProductionUnitOperationViewData puOperation)
            throws NotInDatabaseException_Exception {
        return updateAction(
                () -> productionUnitOperationDAO.update(puOperation),
                "puc_opr.short.text",
                NavigationElements.PLANT_PUC);
    }

    public String deletePUOperation(@NotNull ProductionUnitOperationViewData puOperation)
            throws NotInDatabaseException_Exception {
        return deleteAction(
                () -> productionUnitOperationDAO.delete(puOperation),
                "puc_opr.short.text",
                NavigationElements.PLANT_PUC);
    }

    public Collection<ViewData<ProductionUnitClassTO>> getPucs() {
        return pucs;
    }

    public void setPucs(final Collection<ViewData<ProductionUnitClassTO>> pucs) {
        this.pucs = pucs;
    }
}
