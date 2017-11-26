package org.cocome.tradingsystem.inventory.data.store;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.registry.service.Names;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * This class can be used to create JAX-WS client for the {@link IStoreManager} web services.
 *
 * @author Rudolf Biczok
 */
@Singleton
@LocalBean
public class StoreClientFactory {
    private static final Logger LOG = Logger.getLogger(StoreClientFactory.class);

    @Inject
    private long defaultStoreIndex;

    @Inject
    private IApplicationHelper applicationHelper;

    /**
     * Creates a JAX-WS client for the service endpoint that corresponds to the given store id
     *
     * @param storeID the id of the store whose service endpoint should be returned
     * @return a JAX-WS client for {@link IStoreManager}
     * @throws NotInDatabaseException if the given store id is unknown
     */
    public IStoreManager createClient(long storeID) throws NotInDatabaseException {
        IStoreManagerService storeService;
        try {
            storeService = applicationHelper.getComponent(Names.getStoreManagerRegistryName(storeID),
                    IStoreManagerService.SERVICE, IStoreManagerService.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            if (storeID == defaultStoreIndex) {
                throw new NotInDatabaseException(
                        "Exception occurred while retrieving the store service: " + e.getMessage());
            } else {
                LOG.info("Looking up default store server because there was none registered for id "
                        + storeID);
                return createClient(defaultStoreIndex);
            }
        }
        return storeService.getIStoreManagerPort();
    }
}
