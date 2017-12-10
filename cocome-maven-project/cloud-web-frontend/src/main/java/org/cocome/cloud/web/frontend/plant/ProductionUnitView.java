package org.cocome.cloud.web.frontend.plant;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.cloud.web.data.plantdata.*;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@ViewScoped
public class ProductionUnitView extends AbstractView {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProductionUnitDAO productionUnitDAO;

    @Inject
    private ProductionUnitClassDAO productionUnitClassDAO;

    @Inject
    private PlantInformation plantInformation;

    public String createPU(@NotNull String location,
                           @NotNull String interfaceUrl,
                           @NotNull boolean doubleFlag,
                           @NotNull PlantViewData plant,
                           @NotNull ProductionUnitClassViewData puc) throws NotInDatabaseException_Exception {
        return createAction(
                () -> {
                    final ProductionUnitTO puTO = new ProductionUnitTO();
                    puTO.setLocation(location);
                    puTO.setInterfaceUrl(interfaceUrl);
                    puTO.setDouble(doubleFlag);
                    puTO.setPlant(plant.getData());
                    puTO.setProductionUnitClass(puc.getData());
                    productionUnitDAO.create(new ProductionUnitViewData(puTO));
                },
                "pu.short.text",
                NavigationElements.PLANT_PU);
    }

    public String updatePU(@NotNull ProductionUnitViewData pu) throws NotInDatabaseException_Exception {
        return updateAction(
                () -> {
                    productionUnitDAO.update(pu);
                },
                "pu.short.text",
                NavigationElements.PLANT_PU);
    }

    public String deletePU(@NotNull ProductionUnitViewData pu) throws NotInDatabaseException_Exception {
        return deleteAction(
                () -> {
                    productionUnitDAO.delete(pu);
                },
                "pu.short.text",
                NavigationElements.PLANT_PU);
    }

    public Collection<ProductionUnitClassTO> getPUCs() throws NotInDatabaseException_Exception {
        return productionUnitClassDAO.getAllByParentObj(plantInformation.getActivePlant())
                .stream().map(ViewData::getData).collect(Collectors.toList());
    }
}
