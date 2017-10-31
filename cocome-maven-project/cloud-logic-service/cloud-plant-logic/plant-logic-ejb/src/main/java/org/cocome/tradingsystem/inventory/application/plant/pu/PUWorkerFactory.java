package org.cocome.tradingsystem.inventory.application.plant.pu;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.PUDouble;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

/**
 * Generates the actual interface classes for access the production unit services
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PUWorkerFactory {

    @Inject
    private IPUCallback eventCoordinator;

    @Inject
    private IPlantQuery enterpriseQuery;

    private static final long DEFAULT_EXEC_DURATION_FOR_DUMMY_INTERFACES = 1000;

    /**
     * Creates a new worker instance based on the given database data in {@link IProductionUnit}
     *
     * @param unit the entity from the database.
     * @return a brand new worker that can receive atomic production operations
     * @throws NotInDatabaseException if necessary data could not be retrieved from the database
     */
    public PUWorker createWorker(final IProductionUnit unit) throws NotInDatabaseException {
        final IPUInterface iface = createClient(unit);
        return new PUWorker(unit, iface, eventCoordinator);
    }

    private IPUInterface createClient(final IProductionUnit unit) throws NotInDatabaseException {
        if (unit.isDouble()) {
            final IProductionUnitClass puc = unit.getProductionUnitClass();
            final Collection<IProductionUnitOperation> ops = enterpriseQuery
                    .queryProductionUnitOperationsByProductionUnitClassId(puc.getPlantId());
            return this.createFakeClient(ops);
        }
        return createRESTClient(unit.getInterfaceUrl());
    }

    private IPUInterface createRESTClient(final String interfaceUrl) {
        return JAXRSClientFactory.create(interfaceUrl, IPUInterface.class,
                Collections.singletonList(new JacksonJsonProvider()));
    }

    private IPUInterface createFakeClient(final Collection<IProductionUnitOperation> operations) {
        return new PUDouble(operations, DEFAULT_EXEC_DURATION_FOR_DUMMY_INTERFACES);
    }
}
