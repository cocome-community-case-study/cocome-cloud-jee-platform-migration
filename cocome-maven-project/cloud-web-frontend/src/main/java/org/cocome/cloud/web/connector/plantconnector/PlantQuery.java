package org.cocome.cloud.web.connector.plantconnector;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.IPlantManagerService;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.registry.service.Names;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

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

}
