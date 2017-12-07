package org.cocome.cloud.web.data.plantdata;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.cloud.logic.webservice.ThrowingProcedure;
import org.cocome.cloud.web.connector.enterpriseconnector.IEnterpriseQuery;
import org.cocome.cloud.web.connector.plantconnector.PlantQuery;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class ProductionUnitClassDAO {
    private static final Logger LOG = Logger.getLogger(ProductionUnitClassDAO.class);

    @Inject
    private PlantQuery plantQuery;

    public String create(@NotNull String name,
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

    public String update(@NotNull PUCWrapper puc) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(puc.getPlant().getID());
                    plantManager.updateProductionUnitClass(puc.getPUC());
                },
                Messages.get("message.update.success", Messages.get("puc.short.text")),
                Messages.get("message.update.failed", Messages.get("puc.short.text")),
                NavigationElements.PLANT_PUC);
    }

    public String delete(@NotNull PUCWrapper puc) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> {
                    final IPlantManager plantManager = plantQuery.lookupPlantManager(puc.getPlant().getID());
                    plantManager.deleteProductionUnitClass(puc.getPUC());
                },
                Messages.get("message.delete.success", Messages.get("puc.short.text")),
                Messages.get("message.delete.failed", Messages.get("puc.short.text")),
                NavigationElements.PLANT_PUC);
    }

    public List<PUCWrapper> queryAll(@NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        LOG.debug("Querying production unit classes");

        final IPlantManager plantManager = plantQuery.lookupPlantManager(plant.getID());
        return StreamUtil.ofNullable(plantManager.queryProductionUnitClassesByPlantID(plant.getID()))
                .map(e -> new PUCWrapper(e, plant)).collect(Collectors.toList());
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
