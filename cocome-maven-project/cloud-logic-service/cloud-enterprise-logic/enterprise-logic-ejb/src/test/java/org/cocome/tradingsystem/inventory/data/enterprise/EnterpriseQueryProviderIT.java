package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.test.TestUtils;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.CredentialFactory;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredentialFactory;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.*;
import org.cocome.tradingsystem.inventory.data.persistence.CloudPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.plant.IPlantDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.PlantDatatypesFactory;
import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.NorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.EnterpriseStoreQueryProvider;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.inventory.data.store.StoreDatatypesFactory;
import org.cocome.tradingsystem.inventory.data.usermanager.IUserDataFactory;
import org.cocome.tradingsystem.inventory.data.usermanager.UsermanagerDatatypesFactory;
import org.cocome.tradingsystem.remote.access.connection.CSVBackendConnection;
import org.cocome.tradingsystem.remote.access.connection.GetXMLFromBackend;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.connection.IPersistenceConnection;
import org.cocome.tradingsystem.remote.access.parsing.CSVHelper;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Test unit for {@link EnterpriseQueryProvider}
 *
 * @author Rudolf Biczok
 */
public class EnterpriseQueryProviderIT {

    private IPersistenceContext persistenceContext = TestUtils.injectFakeCDIObject(IPersistenceContext.class,
            createMappings());

    private IEnterpriseQuery enterpriseQuery = TestUtils.injectFakeCDIObject(IEnterpriseQuery.class,
            createMappings());

    private Map<Class<?>, Class<?>> createMappings() {
        final Map<Class<?>, Class<?>> mapping = new HashMap<>();
        mapping.put(IPlantOperation.class, PlantOperation.class);
        mapping.put(IPersistenceContext.class, CloudPersistenceContext.class);
        mapping.put(IPersistenceConnection.class, CSVBackendConnection.class);
        mapping.put(IEntryPointInteraction.class, EntryPointInteraction.class);
        mapping.put(IParameterInteraction.class, ParameterInteraction.class);
        mapping.put(IPlantOperationOrder.class, PlantOperationOrder.class);
        mapping.put(IPlantOperationOrderEntry.class, PlantOperationOrderEntry.class);
        mapping.put(IPlantOperationParameterValue.class, PlantOperationParameterValue.class);
        mapping.put(IRecipe.class, Recipe.class);
        mapping.put(ICustomProduct.class, CustomProduct.class);
        mapping.put(IBooleanPlantOperationParameter.class, BooleanPlantOperationParameter.class);
        mapping.put(INorminalPlantOperationParameter.class, NorminalPlantOperationParameter.class);
        mapping.put(IBooleanCustomProductParameter.class, BooleanCustomProductParameter.class);
        mapping.put(INorminalCustomProductParameter.class, NorminalCustomProductParameter.class);
        mapping.put(ICredentialFactory.class, CredentialFactory.class);
        mapping.put(IUserDataFactory.class, UsermanagerDatatypesFactory.class);
        mapping.put(IPlantDataFactory.class, PlantDatatypesFactory.class);
        mapping.put(IStoreDataFactory.class, StoreDatatypesFactory.class);
        mapping.put(IEnterpriseDataFactory.class, EnterpriseDatatypesFactory.class);
        mapping.put(IBackendConversionHelper.class, CSVHelper.class);
        mapping.put(IStoreQuery.class, EnterpriseStoreQueryProvider.class);
        mapping.put(IBackendQuery.class, GetXMLFromBackend.class);
        mapping.put(IEnterpriseQuery.class, EnterpriseQueryProvider.class);
        return mapping;
    }

    @Test
    public void queryParametersByCustomProductID() throws Exception {
        final ICustomProduct prod = new CustomProduct();
        prod.setBarcode(new Date().getTime());
        prod.setName("Fancy Product");
        prod.setPurchasePrice(100);
        persistenceContext.createEntity(prod);

        final IBooleanCustomProductParameter param = new BooleanCustomProductParameter();
        param.setCustomProduct(prod);
        param.setCustomProductId(prod.getId());
        param.setCategory("SomeCategory");
        param.setName("SomeName2");
        persistenceContext.createEntity(param);

        final INorminalCustomProductParameter param2 = new NorminalCustomProductParameter();
        param2.setCustomProduct(prod);
        param2.setCustomProductId(prod.getId());
        param2.setOptions(new HashSet<>(Arrays.asList("Op1", "Op2")));
        param2.setCategory("SomeCategory");
        param2.setName("SomeName");
        persistenceContext.createEntity(param2);

        final List<ICustomProductParameter> params =
                new ArrayList<>(enterpriseQuery.queryParametersByCustomProductID(prod.getId()));
        Assert.assertEquals(2, params.size());
        Assert.assertEquals(params.get(0).getClass(), BooleanCustomProductParameter.class);
        Assert.assertEquals(params.get(1).getClass(), NorminalCustomProductParameter.class);

        persistenceContext.deleteEntity(param);
        persistenceContext.deleteEntity(param2);
        persistenceContext.deleteEntity(prod);
    }
}