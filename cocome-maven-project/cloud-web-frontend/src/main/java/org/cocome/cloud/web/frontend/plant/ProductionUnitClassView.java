package org.cocome.cloud.web.frontend.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.ThrowingProcedure;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;
import org.cocome.cloud.web.data.plantdata.PUCWrapper;
import org.cocome.cloud.web.data.plantdata.PlantViewData;
import org.cocome.cloud.web.data.plantdata.ProductionUnitClassDAO;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;

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

    private List<PUCWrapper> pucs;

    @PostConstruct
    public void queryPUCs() {
        LOG.info("Query PUCs");
        try {
            this.pucs = productionUnitClassDAO.queryAll(plantInformation.getActivePlant());
        } catch (NotInDatabaseException_Exception e) {
            LOG.error("Unable to load PUC list", e);
        }
    }

    public String createPUC(@NotNull String name,
                         @NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(plant.getID());
                    final ProductionUnitClassTO pucTO = new ProductionUnitClassTO();
                    pucTO.setName(name);
                    pucTO.setPlant(plant.getPlantTO());
                    pucTO.setId(plantManager.createProductionUnitClass(pucTO));
                },
                Messages.get("message.create.success", Messages.get("puc.short.text")),
                Messages.get("message.create.failed", Messages.get("puc.short.text")),
                NavigationElements.PLANT_PUC);
    }

    public String updatePUC(@NotNull PUCWrapper puc) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(puc.getPlant().getID());
                    plantManager.updateProductionUnitClass(puc.getPUC());
                },
                Messages.get("message.update.success", Messages.get("puc.short.text")),
                Messages.get("message.update.failed", Messages.get("puc.short.text")),
                NavigationElements.PLANT_PUC);
    }

    public String deletePUC(@NotNull PUCWrapper puc) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(puc.getPlant().getID());
                    plantManager.deleteProductionUnitClass(puc.getPUC());
                },
                Messages.get("message.delete.success", Messages.get("puc.short.text")),
                Messages.get("message.delete.failed", Messages.get("puc.short.text")),
                NavigationElements.PLANT_PUC);
    }

    public List<PUCWrapper> getPucs() {
        return pucs;
    }

    public void setPucs(final List<PUCWrapper> pucs) {
        this.pucs = pucs;
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
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            onFailureMessage, null));
        }
        return null;
    }

}
