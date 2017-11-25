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

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.cocome.cloud.logic.stub.*;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.BooleanCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.CustomProductParameterValueTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.NorminalCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionTO;
import org.cocome.tradingsystem.inventory.application.plant.iface.PUCImporter;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.FMU;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.XPPU;
import org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NorminalPlantOperationParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.*;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanParameter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class EnterpriseManagerIT {

    private static IEnterpriseManager em;
    private static IPlantManager pm;

    @BeforeClass
    public static void createClient() {
        em = createJaxWsClient(IEnterpriseManager.class,
                "http://127.0.0.1:40797/EnterpriseService/IEnterpriseManager");

        pm = createJaxWsClient(IPlantManager.class,
                "http://127.0.0.1:41897/PlantService/IPlantManager");
    }

    @Test
    public void testCRUDForPlant() throws Exception {
        final EnterpriseTO enterprise = getOrCreateEnterprise();
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
        Assert.assertEquals(products.size(), 3);

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

    private EnterpriseTO getOrCreateEnterprise() throws CreateException_Exception, NotInDatabaseException_Exception {
        final String enterpriseName = String.format("Enterprise-%s", UUID.randomUUID().toString());
        final EnterpriseTO enterprise;
        try {
            enterprise = em.queryEnterpriseByName(enterpriseName);
        } catch (NotInDatabaseException_Exception e) {
            em.createEnterprise(enterpriseName);
            return em.queryEnterpriseByName(enterpriseName);
        }
        return enterprise;
    }

    @Test
    public void testOrderShit() throws Exception {
        final EnterpriseTO enterprise = getOrCreateEnterprise();
        final PlantTO plant = getOrCreatePlant(enterprise);

        /* Environmental setup */

        final PUCImporter xppu = new PUCImporter("Default xPPU", XPPU.values(), plant, pm);
        final PUCImporter fmu = new PUCImporter("FMU", FMU.values(), plant, pm);

        /* Production Units */

        final ProductionUnitTO xppu1 = new ProductionUnitTO();
        xppu1.setPlant(plant);
        xppu1.setProductionUnitClass(xppu.getProductionUnitClass());
        xppu1.setDouble(true);
        xppu1.setInterfaceUrl("dummy1.org");
        xppu1.setLocation("Some Place 1");
        xppu1.setId(pm.createProductionUnit(xppu1));

        final ProductionUnitTO xppu2 = new ProductionUnitTO();
        xppu2.setPlant(plant);
        xppu2.setProductionUnitClass(xppu.getProductionUnitClass());
        xppu2.setDouble(true);
        xppu2.setInterfaceUrl("dummy2.org");
        xppu2.setLocation("Some Place 2");
        xppu2.setId(pm.createProductionUnit(xppu2));

        final ProductionUnitTO fmu3 = new ProductionUnitTO();
        fmu3.setPlant(plant);
        fmu3.setProductionUnitClass(fmu.getProductionUnitClass());
        fmu3.setDouble(true);
        fmu3.setInterfaceUrl("dummy2.org");
        fmu3.setLocation("Some Place 3");
        fmu3.setId(pm.createProductionUnit(fmu3));

        /* Plant Operations */

        final EntryPointTO op1out1 = new EntryPointTO();
        op1out1.setName("ISO 12345 Cargo");
        op1out1.setId(em.createEntryPoint(op1out1));

        final PlantOperationTO operation1 = new PlantOperationTO();
        operation1.setName("Produce Yogurt");
        operation1.setPlant(plant);
        operation1.setOutputEntryPoint(Collections.singletonList(op1out1));
        operation1.setId(em.createPlantOperation(operation1));

        final BooleanPlantOperationParameterTO param = new BooleanPlantOperationParameterTO();
        param.setCategory("Ingredients");
        param.setName("Organic");
        param.setId(em.createBooleanPlantOperationParameter(param, operation1));

        final ConditionalExpressionTO conditionalExpression = new ConditionalExpressionTO();
        conditionalExpression.setParameter(param);
        conditionalExpression.setParameterValue(IBooleanParameter.TRUE_VALUE);
        conditionalExpression.setOnTrueExpressions(Arrays.asList(
                xppu.getOperation(XPPU.Crane_ACT_PutDownWP),
                xppu.getOperation(XPPU.Crane_ACT_PutDownWP),
                xppu.getOperation(XPPU.Crane_ACT_PickUpWP)));
        conditionalExpression.setOnFalseExpressions(Arrays.asList(
                xppu.getOperation(XPPU.Stack_ACT_ProvideWP),
                xppu.getOperation(XPPU.Stamp_ACT_Stamp),
                xppu.getOperation(XPPU.Stamp_ACT_Stamp)));
        conditionalExpression.setId(pm.createConditionalExpression(conditionalExpression));

        operation1.setExpressions(Arrays.asList(
                xppu.getOperation(XPPU.Crane_ACT_Init),
                xppu.getOperation(XPPU.Stack_ACT_Init),
                conditionalExpression,
                fmu.getOperation(FMU.Silo0_ACT_Init),
                fmu.getOperation(FMU.Silo1_ACT_Init),
                fmu.getOperation(FMU.Silo2_ACT_Init)
        ));
        em.updatePlantOperation(operation1);

        final EntryPointTO op2out1 = new EntryPointTO();
        op2out1.setName("ISO 33333 Bottle");
        op2out1.setId(em.createEntryPoint(op2out1));

        final PlantOperationTO operation2 = new PlantOperationTO();
        operation2.setName("Fill Yogurt");
        operation2.setPlant(plant);
        operation2.setOutputEntryPoint(Collections.singletonList(op2out1));
        operation2.setExpressions(Arrays.asList(
                xppu.getOperation(XPPU.Crane_ACT_Init),
                fmu.getOperation(FMU.Silo0_ACT_Init),
                fmu.getOperation(FMU.Silo2_ACT_Init)
        ));
        operation2.setId(em.createPlantOperation(operation2));

        final NorminalPlantOperationParameterTO opr2param = new NorminalPlantOperationParameterTO();
        opr2param.setCategory("Bottle");
        opr2param.setOptions(new HashSet<>(Arrays.asList("Glass", "Plastic")));
        opr2param.setName("Bottle");
        opr2param.setId(em.createNorminalPlantOperationParameter(opr2param, operation2));

        final EntryPointTO op3in1 = new EntryPointTO();
        op3in1.setName("ISO 12345 Cargo");
        op3in1.setId(em.createEntryPoint(op3in1));

        final EntryPointTO op3in2 = new EntryPointTO();
        op3in2.setName("ISO 33333 Bottle");
        op3in2.setId(em.createEntryPoint(op3in2));

        final EntryPointTO op3out1 = new EntryPointTO();
        op3out1.setName("ISO 321 Package");
        op3out1.setId(em.createEntryPoint(op3out1));

        final PlantOperationTO operation3 = new PlantOperationTO();
        operation3.setName("Package Yogurt");
        operation3.setPlant(plant);
        operation3.setInputEntryPoint(Arrays.asList(op3in1, op3in2));
        operation3.setOutputEntryPoint(Collections.singletonList(op3out1));
        operation3.setExpressions(Arrays.asList(
                xppu.getOperation(XPPU.Crane_ACT_Init),
                fmu.getOperation(FMU.Silo0_ACT_Init),
                fmu.getOperation(FMU.Silo2_ACT_Init)
        ));
        operation3.setId(em.createPlantOperation(operation3));

        /* Custom Product creation */

        final CustomProductTO customProduct = new CustomProductTO();
        customProduct.setBarcode(new Date().getTime());
        customProduct.setName("Yogurt");
        customProduct.setPurchasePrice(10);
        customProduct.setId(em.createProduct(customProduct));

        final BooleanCustomProductParameterTO cparam1 = new BooleanCustomProductParameterTO();
        cparam1.setCategory("Ingredients");
        cparam1.setName("Organic");
        cparam1.setCustomProduct(customProduct);
        cparam1.setId(em.createBooleanCustomProductParameter(cparam1));

        final NorminalCustomProductParameterTO cparam2 = new NorminalCustomProductParameterTO();
        cparam2.setCategory("Packaging");
        cparam2.setName("Bottle");
        cparam2.setOptions(new HashSet<>(Arrays.asList("Glass", "Plastic")));
        cparam2.setCustomProduct(customProduct);
        cparam2.setId(em.createNorminalCustomProductParameter(cparam2));

        /* Recipe creation */

        final EntryPointTO recipeOut1 = new EntryPointTO();
        recipeOut1.setName("ISO 321 Package");
        recipeOut1.setId(em.createEntryPoint(recipeOut1));

        final ParameterInteractionTO interaction1 = new ParameterInteractionTO();
        interaction1.setFrom(cparam1);
        interaction1.setTo(param);
        interaction1.setId(em.createParameterInteraction(interaction1));

        final ParameterInteractionTO interaction2 = new ParameterInteractionTO();
        interaction2.setFrom(cparam2);
        interaction2.setTo(opr2param);
        interaction2.setId(em.createParameterInteraction(interaction2));

        final EntryPointInteractionTO epInteraction1 = new EntryPointInteractionTO();
        epInteraction1.setFrom(op1out1);
        epInteraction1.setTo(op3in1);
        epInteraction1.setId(em.createEntryPointInteraction(epInteraction1));

        final EntryPointInteractionTO epInteraction2 = new EntryPointInteractionTO();
        epInteraction2.setFrom(op2out1);
        epInteraction2.setTo(op3in2);
        epInteraction2.setId(em.createEntryPointInteraction(epInteraction2));

        final EntryPointInteractionTO epInteraction3 = new EntryPointInteractionTO();
        epInteraction3.setFrom(op3out1);
        epInteraction3.setTo(recipeOut1);
        epInteraction3.setId(em.createEntryPointInteraction(epInteraction3));

        final RecipeTO recipe = new RecipeTO();
        recipe.setName("Yogurt Recipe");
        recipe.setCustomProduct(customProduct);
        recipe.setOutputEntryPoint(Collections.singletonList(recipeOut1));
        recipe.setEntryPointInteractions(Arrays.asList(epInteraction1, epInteraction2, epInteraction3));
        recipe.setParameterInteractions(Arrays.asList(interaction1, interaction2));
        recipe.setOperations(Arrays.asList(operation1, operation2, operation3));
        recipe.setId(em.createRecipe(recipe));

        em.validateRecipe(recipe);

        /* Order creation */

        final ProductionOrderTO operationOrder = new ProductionOrderTO();
        operationOrder.setEnterprise(enterprise);

        final CustomProductParameterValueTO cparam1Value = new CustomProductParameterValueTO();
        cparam1Value.setParameter(cparam1);
        cparam1Value.setValue(IBooleanParameter.FALSE_VALUE);

        final CustomProductParameterValueTO cparam2Value = new CustomProductParameterValueTO();
        cparam2Value.setParameter(cparam1);
        cparam2Value.setValue(IBooleanParameter.FALSE_VALUE);

        final ProductionOrderEntryTO entry = new ProductionOrderEntryTO();
        entry.setRecipe(recipe);
        entry.setAmount(2);
        entry.setParameterValues(Arrays.asList(cparam1Value, cparam2Value));

        operationOrder.setOrderEntries(Collections.singletonList(entry));
        em.submitProductionOrder(operationOrder);
    }

    private PlantTO getOrCreatePlant(final EnterpriseTO enterprise) throws CreateException_Exception,
            NotInDatabaseException_Exception {
        final PlantTO plant = new PlantTO();
        plant.setName("Plant1");
        plant.setEnterpriseTO(enterprise);
        em.createPlant(plant);
        final Collection<PlantTO> plants = em.queryPlantsByEnterpriseID(enterprise.getId());
        return plants.iterator().next();
    }

    @SuppressWarnings("unchecked")
    private static <T> T createJaxWsClient(final Class<T> clientClass, final String url) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(clientClass);
        factory.setAddress(url);
        return (T) factory.create();
    }
}