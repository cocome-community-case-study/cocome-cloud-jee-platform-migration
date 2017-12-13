package org.cocome.cloud.web.frontend.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.plantdata.ProductionUnitClassDAO;
import org.cocome.cloud.web.data.plantdata.ProductionUnitOperationDAO;
import org.cocome.cloud.web.data.plantdata.ProductionUnitOperationViewData;
import org.cocome.cloud.web.frontend.AbstractView;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.omnifaces.cdi.Param;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@ViewScoped
public class ProductionUnitOperationView extends AbstractView<ProductionUnitOperationTO> {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProductionUnitOperationDAO.class);

    @Inject
    @Param
    private Long pucId;

    @Inject
    private ProductionUnitClassDAO productionUnitClassDAO;

    @Inject
    private ProductionUnitOperationDAO productionUnitOperationDAO;

    private ProductionUnitOperationViewData selected;

    @PostConstruct
    public void createNewInstance() {
        if(pucId != null) {
            final ProductionUnitClassTO puc;
            try {
                puc = this.productionUnitClassDAO.find(pucId);
            } catch (NotInDatabaseException_Exception e) {
                LOG.error("Unable to fetch PUC", e);
                throw new IllegalArgumentException(e);
            }
            this.selected = new ProductionUnitOperationViewData(new ProductionUnitOperationTO());
            this.selected.getData().setProductionUnitClass(puc);
        }
    }

    @Override
    protected ProductionUnitOperationDAO getDAO() {
        return this.productionUnitOperationDAO;
    }

    @Override
    protected NavigationElements getNextNavigationElement() {
        return NavigationElements.PLANT_PUC;
    }

    @Override
    protected String getObjectName() {
        return Messages.get("puc_opr.short.text");
    }

    public ProductionUnitOperationViewData getSelected() {
        return selected;
    }
}
