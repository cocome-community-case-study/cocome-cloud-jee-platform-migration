package org.cocome.cloud.web.connector.plantconnector;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.*;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.cloud.registry.service.Names;
import org.cocome.cloud.web.data.plantdata.PUCWrapper;
import org.cocome.cloud.web.data.plantdata.PlantViewData;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithItemTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to retrieve data structures specific to a plant
 *
 * @author Rudolf Biczok
 */
@Named
@ApplicationScoped
public class PlantQuery {
    private static final Logger LOG = Logger.getLogger(PlantQuery.class);

    @Inject
    private long defaultPlantIndex;

    @Inject
    private IApplicationHelper applicationHelper;

    public IPlantManager lookupPlantManager(long plantID) throws NotInDatabaseException_Exception {
        try {
            LOG.debug(String.format("Looking up responsible plant manager for plant %d", plantID));
            return applicationHelper.getComponent(Names.getPlantManagerRegistryName(plantID),
                    IPlantManagerService.SERVICE,
                    IPlantManagerService.class).getIPlantManagerPort();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            if (plantID == defaultPlantIndex) {
                LOG.error("Got exception while retrieving plant manager location: " + e.getMessage(), e);
                throw new NotInDatabaseException_Exception(e.getMessage());
            } else {
                return lookupPlantManager(defaultPlantIndex);
            }
        }
    }

    public boolean deletePUC(final PUCWrapper puc) {
        try {
            final IPlantManager plantManager = lookupPlantManager(puc.getPlant().getID());
            plantManager.deleteProductionUnitClass(puc.getPUC());
        } catch (UpdateException_Exception | NotInDatabaseException_Exception e) {
            LOG.error(String.format("Error while updating puc: %s\n", e.getMessage()), e);
            return false;
        }
        return true;
    }

    public boolean updatePUC(final PUCWrapper puc) {
        try {
            final IPlantManager plantManager = lookupPlantManager(puc.getPlant().getID());
            plantManager.updateProductionUnitClass(puc.getPUC());
        } catch (UpdateException_Exception | NotInDatabaseException_Exception e) {
            LOG.error(String.format("Error while updating puc: %s\n", e.getMessage()), e);
            return false;
        }
        return true;
    }

    public boolean createPUC(String name, PlantViewData plant) {
        try {
            final IPlantManager plantManager = lookupPlantManager(plant.getID());
            final ProductionUnitClassTO pucTO = new ProductionUnitClassTO();
            pucTO.setName(name);
            pucTO.setPlant(plant.getPlantTO());
            pucTO.setId(plantManager.createProductionUnitClass(pucTO));
        } catch (CreateException_Exception | NotInDatabaseException_Exception e) {
            LOG.error(String.format("Error while creating puc: %s\n", e.getMessage()), e);
            return false;
        }
        return true;
    }

    public List<PUCWrapper> queryPUCs(@NotNull PlantViewData plant) throws NotInDatabaseException_Exception {
        LOG.debug("Querying production unit classes");

        final IPlantManager plantManager = lookupPlantManager(plant.getID());
        return StreamUtil.ofNullable(plantManager.queryProductionUnitClassesByPlantID(plant.getID()))
                .map(e -> new PUCWrapper(e, plant)).collect(Collectors.toList());
    }

}
