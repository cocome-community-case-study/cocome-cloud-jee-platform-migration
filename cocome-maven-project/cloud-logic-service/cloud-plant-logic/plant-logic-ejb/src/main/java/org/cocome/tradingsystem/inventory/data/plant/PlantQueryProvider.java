package org.cocome.tradingsystem.inventory.data.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IEnterpriseManagerService;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.registry.service.Names;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * The objects returned will only have their basic datatype attributes filled.
 *
 * @author Rudolf Biczok
 */
@Stateless
@Local(IPlantQuery.class)
public class PlantQueryProvider implements IPlantQuery {

    private static final Logger LOG = Logger.getLogger(PlantQueryProvider.class);

    @Inject
    long defaultEnterpriseIndex;

    @Inject
    IApplicationHelper applicationHelper;

    @Inject
    IEnterpriseDataFactory enterpriseFactory;

    @Inject
    IPlantDataFactory plantFactory;

    private IEnterpriseManager lookupEnterpriseManager(long enterpriseID) throws NotInDatabaseException {
        IEnterpriseManagerService enterpriseService;
        try {
            enterpriseService = applicationHelper.getComponent(Names.getEnterpriseManagerRegistryName(enterpriseID),
                    IEnterpriseManagerService.SERVICE, IEnterpriseManagerService.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            if (enterpriseID == defaultEnterpriseIndex) {
                throw new NotInDatabaseException(
                        "Exception occured while retrieving the enterprise service: " + e.getMessage());
            } else {
                LOG.info("Looking up default enterprise server because there was none registered for id "
                        + enterpriseID);
                return lookupEnterpriseManager(defaultEnterpriseIndex);
            }
        }
        return enterpriseService.getIEnterpriseManagerPort();
    }

    private IEnterpriseManager lookupEnterpriseManager(String enterpriseName) throws NotInDatabaseException {
        IEnterpriseManagerService enterpriseService;
        try {
            enterpriseService = applicationHelper.getComponent(Names.getEnterpriseManagerRegistryName(enterpriseName),
                    IEnterpriseManagerService.SERVICE, IEnterpriseManagerService.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            LOG.info("Looking up default enterprise server because there was none registered for name "
                    + enterpriseName);
            return lookupEnterpriseManager(defaultEnterpriseIndex);
        }
        return enterpriseService.getIEnterpriseManagerPort();
    }

    @Override
    public Collection<IPlant> queryPlantsByEnterpriseId(long enterpriseID) {
        IEnterpriseManager enterpriseManager;
        List<PlantTO> plantTOList;
        try {
            enterpriseManager = lookupEnterpriseManager(enterpriseID);
            plantTOList = enterpriseManager.queryPlantsByEnterpriseID(enterpriseID);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
            return Collections.emptyList();
        }

        List<IPlant> plantList = new ArrayList<>(plantTOList.size());

        for (PlantTO plantTO : plantTOList) {
            plantList.add(plantFactory.convertToPlant(plantTO));
        }
        return plantList;
    }

    @Override
    public IPlant queryPlantByEnterprise(long enterpriseID, long plantID) throws NotInDatabaseException {
        IEnterpriseManager enterpriseManager = lookupEnterpriseManager(enterpriseID);
        try {
            return plantFactory.convertToPlant(enterpriseManager.queryPlantByEnterpriseID(enterpriseID, plantID));
        } catch (NotInDatabaseException_Exception e) {
            throw new NotInDatabaseException(e.getFaultInfo().getMessage());
        }
    }

    @Override
    public Collection<IPlant> queryPlantByName(long enterpriseID, String plantName) {
        IEnterpriseManager enterpriseManager;
        List<PlantTO> plantTOList;

        try {
            enterpriseManager = lookupEnterpriseManager(enterpriseID);
            plantTOList = enterpriseManager.queryPlantByName(enterpriseID, plantName);
        } catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
            LOG.error("Got error while looking up plants by name: " + e.getMessage());
            return Collections.emptyList();
        }

        List<IPlant> plantList = new ArrayList<>(plantTOList.size());

        for (PlantTO plantTO : plantTOList) {
            plantList.add(plantFactory.convertToPlant(plantTO));
        }
        return plantList;
    }

}
