package org.cocome.cloud.logic.webservice.plantservice;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.registry.service.Names;
import org.cocome.logic.webservice.plantservice.IPlantManager;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

@WebService(serviceName = "IPlantManagerService",
        name = "IPlantManager",
        endpointInterface = "org.cocome.logic.webservice.plantservice.IPlantManager",
        targetNamespace = "http://plant.webservice.logic.cocome.org/")
@Stateless
public class PlantManager implements IPlantManager {
    /*@Inject
    private IEnterpriseQuery enterpriseQuery;*/

    @Inject
    private IPersistenceContext persistenceContext;

    @Inject
    private IApplicationHelper applicationHelper;

    @Inject
    private IPlantDataFactory plantFactory;

    @Inject
    private IPlantQuery plantQuery;

    @Inject
    private String plantManagerWSDL;

    @Inject
    private long defaultPlantIndex;

    private static final Logger LOG = Logger.getLogger(PlantManager.class);

    /*
    private void setContextRegistry(long plantID) throws NotInDatabaseException {
        LOG.debug("Setting plant to store with id " + plantID);
        IPlant store = enterpriseQuery.queryPlant(plantID);
        long enterpriseID = store.getEnterprise().getId();

        IContextRegistry registry = new CashDeskRegistry("plant#" + plantID);
        registry.putLong(RegistryKeys.PLANT_ID, plantID);
        registry.putLong(RegistryKeys.ENTERPRISE_ID, enterpriseID);

        try {
            applicationHelper.registerComponent(
                    Names.getPlantManagerRegistryName(defaultPlantIndex), plantManagerWSDL, false);
            applicationHelper.registerComponent(
                    Names.getStoreManagerRegistryName(plantID), plantManagerWSDL, false);
        } catch (URISyntaxException e) {
            LOG.error("Error registering component: " + e.getMessage());
        }
    }
    */

    @Override
    public Collection<ProductionUnitOperationTO> queryProductionUnitOperationsByEnterpriseID(long enterpriseId) throws NotInDatabaseException {
        return null;
    }

    @Override
    public ProductionUnitOperationTO queryProductionUnitOperationByID(long productionUnitOperationId) throws NotInDatabaseException {
        return null;
    }

    @Override
    public void createProductionUnitOperation(ProductionUnitOperationTO productionUnitOperationTO) throws CreateException {
    }

    @Override
    public void updateProductionUnitOperation(ProductionUnitOperationTO productionUnitOperationTO) throws  NotInDatabaseException, UpdateException{

    }

    @Override
    public void deleteProductionUnitOperation(ProductionUnitOperationTO productionUnitOperationTO) throws NotInDatabaseException, UpdateException {

    }
}
