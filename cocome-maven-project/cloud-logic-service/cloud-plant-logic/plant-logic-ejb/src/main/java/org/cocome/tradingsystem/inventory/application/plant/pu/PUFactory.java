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
public class PUFactory {

    @Inject
    private IPlantQuery enterpriseQuery;

    private static final long DEFAULT_EXEC_DURATION_FOR_DUMMY_INTERFACES = 1000;

    /**
     * Creates an REST client based on the given production unit
     *
     * @param unit the production unit (with its url)
     * @return the REST client of the given production unit
     */
    public IPUInterface createClient(final IProductionUnit unit) throws NotInDatabaseException {
        if (unit.isDouble()) {
            final IProductionUnitClass puc = unit.getProductionUnitClass();
            final Collection<IProductionUnitOperation> ops = enterpriseQuery
                    .queryProductionUnitOperationsByProductionUnitClassId(puc.getPlantId());
            return this.createFakeClient(ops);
        }
        return createRESTClient(unit.getInterfaceUrl());
    }

    /**
     * Creates an REST client based on the given production unit
     *
     * @param interfaceUrl the base url that points to the rest interface
     * @return the REST client of the given production unit
     */
    public IPUInterface createRESTClient(final String interfaceUrl) {
        return JAXRSClientFactory.create(interfaceUrl, IPUInterface.class,
                Collections.singletonList(new JacksonJsonProvider()));
    }

    /**
     * Creates a interface double that simulates all production unit operations
     * Note that the double interface
     *
     * @param operations the list of operations used to create the interface double
     * @return a synchronized, dummy interface that simulates all operations
     * without spawning any threads
     */
    public IPUInterface createFakeClient(final Collection<IProductionUnitOperation> operations) {
        return new PUDouble(operations, DEFAULT_EXEC_DURATION_FOR_DUMMY_INTERFACES);
    }
}
