package org.cocome.tradingsystem.inventory.data.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.IPlantManagerService;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.registry.service.Names;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * This class can be used to create JAX-WS client for the {@link IPlantManager} web services.
 *
 * @author Rudolf Biczok
 */
@Singleton
@LocalBean
public class PlantClientFactory {
    private static final Logger LOG = Logger.getLogger(PlantClientFactory.class);

    @Inject
    private long defaultPlantIndex;

    @Inject
    private IApplicationHelper applicationHelper;

    /**
     * Creates a JAX-WS client for the service endpoint that corresponds to the given plant id
     *
     * @param plantID the id of the plant whose service endpoint should be returned
     * @return a JAX-WS client for {@link IPlantManager}
     * @throws NotInDatabaseException if the given plant id is unknown
     */
    public IPlantManager createClient(long plantID) throws NotInDatabaseException {
        IPlantManagerService plantService;
        try {
            plantService = applicationHelper.getComponent(Names.getPlantManagerRegistryName(plantID),
                    IPlantManagerService.SERVICE, IPlantManagerService.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            if (plantID == defaultPlantIndex) {
                throw new NotInDatabaseException(
                        "Exception occurred while retrieving the plant service: " + e.getMessage());
            } else {
                LOG.info("Looking up default plant server because there was none registered for id "
                        + plantID);
                return createClient(defaultPlantIndex);
            }
        }
        return plantService.getIPlantManagerPort();
    }
}
