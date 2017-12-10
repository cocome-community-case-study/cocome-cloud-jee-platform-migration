package org.cocome.cloud.web.frontend.plant;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.cloud.web.data.plantdata.ProductionUnitClassDAO;
import org.cocome.cloud.web.data.plantdata.ProductionUnitDAO;
import org.cocome.cloud.web.data.plantdata.ProductionUnitViewData;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@ViewScoped
public class ProductionUnitView extends AbstractView<ProductionUnitTO> {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProductionUnitDAO productionUnitDAO;

    @Inject
    private ProductionUnitClassDAO productionUnitClassDAO;

    @Inject
    private PlantInformation plantInformation;

    private ProductionUnitViewData newInstance;

    @PostConstruct
    public void createNewInstance() {
        this.newInstance = new ProductionUnitViewData(new ProductionUnitTO());
        this.newInstance.getData().setPlant(plantInformation.getActivePlant().getData());
    }

    public Collection<ProductionUnitClassTO> getPUCs() throws NotInDatabaseException_Exception {
        return productionUnitClassDAO.getAllByParentObj(plantInformation.getActivePlant())
                .stream().map(ViewData::getData).collect(Collectors.toList());
    }

    @Override
    protected ProductionUnitDAO getDAO() {
        return this.productionUnitDAO;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        return NavigationElements.PLANT_PU;
    }

    @Override
    protected String getObjectName() {
        return Messages.get("pu.short.text");
    }

    public ProductionUnitViewData getNewInstance() {
        return newInstance;
    }
}
