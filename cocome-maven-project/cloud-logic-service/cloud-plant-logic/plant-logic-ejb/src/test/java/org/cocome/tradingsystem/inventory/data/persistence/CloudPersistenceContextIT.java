package org.cocome.tradingsystem.inventory.data.persistence;

import org.cocome.test.TestUtils;
import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.NorminalCustomProductParameter;
import org.cocome.tradingsystem.remote.access.connection.CSVBackendConnection;
import org.cocome.tradingsystem.remote.access.connection.IPersistenceConnection;
import org.junit.Test;

import java.util.*;

public class CloudPersistenceContextIT {

    private IPersistenceContext persistenceContext = TestUtils.injectFakeCDIObject(IPersistenceContext.class,
            createMappings());

    private Map<Class<?>, Class<?>> createMappings() {
        final Map<Class<?>, Class<?>> mapping = new HashMap<>();
        mapping.put(IPersistenceContext.class, CloudPersistenceContext.class);
        mapping.put(IPersistenceConnection.class, CSVBackendConnection.class);
        return mapping;
    }

    @Test
    public void createAndDeleteNorminalCustomProductParameter() throws Exception {
        final ICustomProduct prod = new CustomProduct();
        prod.setBarcode(new Date().getTime());
        prod.setName("Fancy Product");
        prod.setPurchasePrice(100);
        persistenceContext.createEntity(prod);

        final INorminalCustomProductParameter param = new NorminalCustomProductParameter();
        param.setCustomProduct(prod);
        param.setCustomProductId(prod.getId());
        param.setOptions(new HashSet<>(Arrays.asList("Op1", "Op2")));
        param.setCategory("SomeCategory");
        param.setName("SomeName");
        persistenceContext.createEntity(param);

        persistenceContext.deleteEntity(param);
        persistenceContext.deleteEntity(prod);
    }
}