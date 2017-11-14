package org.cocome.tradingsystem.inventory.data.enterprise;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IEnterpriseManagerService;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.registry.service.Names;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * This class can be used to create JAX-WS client for the {@link IEnterpriseManager} web services.
 *
 * @author Rudolf Biczok
 */
@Singleton
@LocalBean
public class EnterpriseClientFactory {
    private static final Logger LOG = Logger.getLogger(EnterpriseClientFactory.class);

    @Inject
    private long defaultEnterpriseIndex;

    @Inject
    private IApplicationHelper applicationHelper;

    /**
     * Creates a JAX-WS client for the service endpoint that corresponds to the given enterprise id
     *
     * @param enterpriseID the id of the enterprise whose service endpoint should be returned
     * @return a JAX-WS client for {@link IEnterpriseManager}
     * @throws NotInDatabaseException if the given enterprise id is unknown
     */
    public IEnterpriseManager createClient(long enterpriseID) throws NotInDatabaseException {
        IEnterpriseManagerService enterpriseService;
        try {
            enterpriseService = applicationHelper.getComponent(Names.getEnterpriseManagerRegistryName(enterpriseID),
                    IEnterpriseManagerService.SERVICE, IEnterpriseManagerService.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            if (enterpriseID == defaultEnterpriseIndex) {
                throw new NotInDatabaseException(
                        "Exception occurred while retrieving the enterprise service: " + e.getMessage());
            } else {
                LOG.info("Looking up default enterprise server because there was none registered for id "
                        + enterpriseID);
                return createClient(defaultEnterpriseIndex);
            }
        }
        return enterpriseService.getIEnterpriseManagerPort();
    }

    /**
     * Creates a JAX-WS client for the service endpoint that corresponds to the given enterprise name
     *
     * @param enterpriseName the name of the enterprise whose service endpoint should be returned
     * @return a JAX-WS client for {@link IEnterpriseManager}
     * @throws NotInDatabaseException if the given enterprise id is unknown
     */
    public IEnterpriseManager createClient(String enterpriseName) throws NotInDatabaseException {
        IEnterpriseManagerService enterpriseService;
        try {
            enterpriseService = applicationHelper.getComponent(Names.getEnterpriseManagerRegistryName(enterpriseName),
                    IEnterpriseManagerService.SERVICE, IEnterpriseManagerService.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
            LOG.info("Looking up default enterprise server because there was none registered for name "
                    + enterpriseName);
            return createClient(defaultEnterpriseIndex);
        }
        return enterpriseService.getIEnterpriseManagerPort();
    }

}
