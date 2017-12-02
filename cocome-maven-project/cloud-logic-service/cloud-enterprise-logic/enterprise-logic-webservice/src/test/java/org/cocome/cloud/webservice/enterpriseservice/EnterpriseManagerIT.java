/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package org.cocome.cloud.webservice.enterpriseservice;

import org.cocome.cloud.logic.stub.*;
import org.cocome.test.EnterpriseInfo;
import org.cocome.test.TestConfig;
import org.cocome.test.WSTestUtils;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.BooleanCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.CustomProductParameterValueTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.NorminalCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ProductionOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.ProductionOrderTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanParameter;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class EnterpriseManagerIT {

    private static IEnterpriseManager em = WSTestUtils.createJaxWsClient(IEnterpriseManager.class,
            TestConfig.getEnterpriseServiceWSDL());
    private static IPlantManager pm = WSTestUtils.createJaxWsClient(IPlantManager.class,
            TestConfig.getPlantManagerWSDL());

    @Test
    public void testCRUDForPlant() throws Exception {
        final EnterpriseTO enterprise = WSTestUtils.createEnterprise(em);
        PlantTO plant = new PlantTO();
        plant.setName("Plant1");
        plant.setLocation("Some Location");
        plant.setEnterpriseTO(enterprise);
        plant.setId(em.createPlant(plant));

        final List<PlantTO> plants = em.queryPlantsByEnterpriseID(enterprise.getId());
        Assert.assertNotNull(plants);
        Assert.assertFalse(plants.isEmpty());

        final PlantTO singleInstance = em.queryPlantByID(plant.getId());
        Assert.assertNotNull(singleInstance);
        Assert.assertEquals(plant.getId(), singleInstance.getId());
        Assert.assertEquals(plant.getName(), singleInstance.getName());
        Assert.assertEquals(plant.getLocation(), singleInstance.getLocation());
        for (final PlantTO plantTO : plants) {
            em.deletePlant(plantTO);
        }
        em.deleteEnterprise(enterprise);
    }

    @Test
    public void testCRUDForProducts()
            throws CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception {
        final CustomProductTO customProductTO = new CustomProductTO();
        customProductTO.setName("Awesome Product 1");
        customProductTO.setBarcode(new Date().getTime());
        customProductTO.setPurchasePrice(10);
        customProductTO.setId(em.createProduct(customProductTO));

        final CustomProductTO customProductTO2 = new CustomProductTO();
        customProductTO2.setName("Awesome Product 1");
        customProductTO2.setBarcode(new Date().getTime());
        customProductTO2.setPurchasePrice(10);
        customProductTO2.setId(em.createProduct(customProductTO2));

        final ProductTO productTO = new ProductTO();
        productTO.setName("Boring Product");
        productTO.setBarcode(new Date().getTime());
        productTO.setPurchasePrice(10);
        productTO.setId(em.createProduct(productTO));

        final List<ProductTO> products = em.getAllProducts();
        Assert.assertNotNull(products);
        Assert.assertTrue(products.size() >= 3);

        final Collection<CustomProductTO> customProducts = em.getAllCustomProducts();
        Assert.assertNotNull(customProducts);
        Assert.assertTrue(customProducts.size() >= 2);

        Assert.assertEquals(customProductTO2.getBarcode(),
                em.queryCustomProductByBarcode(customProductTO2.getBarcode()).getBarcode());

        final CustomProductTO queriedInstance = em.queryCustomProductByID(customProductTO.getId());
        Assert.assertEquals(customProductTO.getName(), queriedInstance.getName());
        Assert.assertEquals(customProductTO.getPurchasePrice(), queriedInstance.getPurchasePrice(), 0.001);

        em.deleteProduct(productTO);
        em.deleteProduct(customProductTO);
        em.deleteProduct(customProductTO2);
        try {
            em.queryCustomProductByID(customProductTO.getId());
            Assert.fail("Expecting product to be deleted from the database: " + customProductTO.getId());
        } catch (final NotInDatabaseException_Exception ex) {
            //no-op
        }
    }

    @Test
    public void testCRUDForEntryPoint()
            throws CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception {
        final EntryPointTO entryPointTO = new EntryPointTO();
        entryPointTO.setName("ISO 12345 Slot");
        entryPointTO.setId(em.createEntryPoint(entryPointTO));
        final EntryPointTO singleInstance = em.queryEntryPointById(entryPointTO.getId());
        Assert.assertEquals(entryPointTO.getName(), singleInstance.getName());
        em.deleteEntryPoint(entryPointTO);
        try {
            em.queryEntryPointById(entryPointTO.getId());
            Assert.fail("Entity is supposed to be deleted");
        } catch (final NotInDatabaseException_Exception ex) {
            //no-op
        }
    }

    @Test
    public void testCRUDForBooleanCustomProductParameter()
            throws CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception {
        final CustomProductTO customProductTO = new CustomProductTO();
        customProductTO.setName("Awsome Product");
        customProductTO.setBarcode(new Date().getTime());
        customProductTO.setPurchasePrice(10);
        customProductTO.setId(em.createProduct(customProductTO));

        final BooleanCustomProductParameterTO paramTO = new BooleanCustomProductParameterTO();
        paramTO.setName("A Parameter");
        paramTO.setCategory("Amazing Stuff");
        paramTO.setCustomProduct(customProductTO);
        paramTO.setId(em.createBooleanCustomProductParameter(paramTO));
        final BooleanCustomProductParameterTO singleInstance
                = em.queryBooleanCustomProductParameterById(paramTO.getId());
        Assert.assertEquals(paramTO.getName(), singleInstance.getName());
        Assert.assertEquals(paramTO.getCategory(), singleInstance.getCategory());
        Assert.assertEquals(paramTO.getCustomProduct().getBarcode(),
                singleInstance.getCustomProduct().getBarcode());
        em.deleteBooleanCustomProductParameter(paramTO);
        try {
            em.queryBooleanCustomProductParameterById(paramTO.getId());
            Assert.fail("Entity is supposed to be deleted");
        } catch (final NotInDatabaseException_Exception ex) {
            //no-op
        }
    }

    @Test
    public void testCRUDForNorminalCustomProductParameter()
            throws CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception {
        final CustomProductTO customProductTO = new CustomProductTO();
        customProductTO.setName("Awsome Product");
        customProductTO.setBarcode(new Date().getTime());
        customProductTO.setPurchasePrice(10);
        customProductTO.setId(em.createProduct(customProductTO));

        final NorminalCustomProductParameterTO paramTO = new NorminalCustomProductParameterTO();
        paramTO.setName("A Parameter");
        paramTO.setCategory("Amazing Stuff");
        paramTO.setCustomProduct(customProductTO);
        paramTO.setOptions(new HashSet<>(Arrays.asList("Apple", "Nuts")));
        paramTO.setId(em.createNorminalCustomProductParameter(paramTO));
        final NorminalCustomProductParameterTO singleInstance
                = em.queryNorminalCustomProductParameterById(paramTO.getId());
        Assert.assertEquals(paramTO.getId(), singleInstance.getId());
        Assert.assertEquals(paramTO.getName(), singleInstance.getName());
        Assert.assertEquals(paramTO.getCategory(), singleInstance.getCategory());
        Assert.assertEquals(paramTO.getOptions(), singleInstance.getOptions());
        Assert.assertEquals(paramTO.getCustomProduct().getBarcode(),
                singleInstance.getCustomProduct().getBarcode());
        em.deleteNorminalCustomProductParameter(paramTO);
        try {
            em.queryNorminalCustomProductParameterById(paramTO.getId());
            Assert.fail("Entity is supposed to be deleted");
        } catch (final NotInDatabaseException_Exception ex) {
            //no-op
        }
    }

    @Test
    public void testSubmitProductionOrder() throws Exception {
        final EnterpriseInfo enterpriseInfo = WSTestUtils.createEnvironmentWithSimpleRecipe(em, pm);

        /* Order creation */

        final ProductionOrderTO operationOrder = new ProductionOrderTO();
        operationOrder.setStore(enterpriseInfo.getStores().get(0));

        final CustomProductParameterValueTO cparam1Value = new CustomProductParameterValueTO();
        cparam1Value.setParameter(enterpriseInfo.getCustomProducts().get(0).getParameters().get(0));
        cparam1Value.setValue(IBooleanParameter.FALSE_VALUE);

        final CustomProductParameterValueTO cparam2Value = new CustomProductParameterValueTO();
        cparam2Value.setParameter(enterpriseInfo.getCustomProducts().get(0).getParameters().get(1));
        cparam2Value.setValue("Glass");

        final ProductionOrderEntryTO entry = new ProductionOrderEntryTO();
        entry.setRecipe(enterpriseInfo.getRecipes().get(0));
        entry.setAmount(2);
        entry.setParameterValues(Arrays.asList(cparam1Value, cparam2Value));

        operationOrder.setOrderEntries(Collections.singletonList(entry));
        em.submitProductionOrder(operationOrder);
    }
}