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

package org.cocome.test;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.cocome.cloud.logic.stub.*;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.BooleanCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.NorminalCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionInfo;
import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;
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
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanParameter;

import java.util.*;

/**
 * Utilities for web service unit tests
 *
 * @author Rudolf biczok
 */
public class WSTestUtils {
    @SuppressWarnings("unchecked")
    public static <T> T createJaxWsClient(final Class<T> clientClass, final String url) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(clientClass);
        factory.setAddress(url);
        return (T) factory.create();
    }

    public static EnterpriseInfo createEnvironmentWithSimpleRecipe(final IEnterpriseManager em,
                                                                   final IPlantManager pm)
            throws CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception, RecipeException_Exception {
        final EnterpriseTO enterprise = WSTestUtils.createEnterprise(em);
        final PlantTO plant = WSTestUtils.createPlant(enterprise, em);
        final StoreWithEnterpriseTO store = WSTestUtils.createStore(enterprise, em);

        /* Environmental setup */

        final PUCImporter xppu = new PUCImporter("xPPU", XPPU.values(), plant, pm);
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
        operation1.setMarkup(new MarkupInfo(
                Arrays.asList(
                        xppu.getOperation(XPPU.Crane_ACT_Init),
                        xppu.getOperation(XPPU.Stack_ACT_Init),
                        new ConditionalExpressionInfo(
                                "Organic",
                                IBooleanParameter.TRUE_VALUE,
                                Arrays.asList(
                                        xppu.getOperation(XPPU.Crane_ACT_PutDownWP),
                                        xppu.getOperation(XPPU.Crane_ACT_PutDownWP),
                                        xppu.getOperation(XPPU.Crane_ACT_PickUpWP)),
                                Arrays.asList(
                                        xppu.getOperation(XPPU.Stack_ACT_ProvideWP),
                                        xppu.getOperation(XPPU.Stamp_ACT_Stamp),
                                        xppu.getOperation(XPPU.Stamp_ACT_Stamp))),
                        fmu.getOperation(FMU.Silo0_ACT_Init),
                        fmu.getOperation(FMU.Silo1_ACT_Init),
                        fmu.getOperation(FMU.Silo2_ACT_Init)
                )));
        operation1.setId(em.createPlantOperation(operation1));

        final BooleanPlantOperationParameterTO param = new BooleanPlantOperationParameterTO();
        param.setCategory("Ingredients");
        param.setName("Organic");
        param.setId(em.createBooleanPlantOperationParameter(param, operation1));

        final EntryPointTO op2out1 = new EntryPointTO();
        op2out1.setName("ISO 33333 Bottle");
        op2out1.setId(em.createEntryPoint(op2out1));

        final PlantOperationTO operation2 = new PlantOperationTO();
        operation2.setName("Create Package");
        operation2.setPlant(plant);
        operation2.setOutputEntryPoint(Collections.singletonList(op2out1));
        operation2.setMarkup(new MarkupInfo(Arrays.asList(
                xppu.getOperation(XPPU.Crane_ACT_Init),
                fmu.getOperation(FMU.Silo0_ACT_Init),
                fmu.getOperation(FMU.Silo2_ACT_Init)
        )));
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
        operation3.setMarkup(new MarkupInfo(Arrays.asList(
                xppu.getOperation(XPPU.Crane_ACT_Init),
                fmu.getOperation(FMU.Silo0_ACT_Init),
                fmu.getOperation(FMU.Silo2_ACT_Init)
        )));
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

        return new EnterpriseInfo(enterprise,
                Collections.singletonList(store),
                Collections.singletonList(plant),
                Collections.singletonList(recipe),
                Collections.singletonList(new CustomProductInfo(customProduct,
                        Arrays.asList(cparam1, cparam2))));
    }

    public static EnterpriseTO createEnterprise(final IEnterpriseManager em)
            throws CreateException_Exception, NotInDatabaseException_Exception {
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

    public static StoreWithEnterpriseTO createStore(final EnterpriseTO enterprise,
                                                    final IEnterpriseManager em) throws CreateException_Exception {
        final StoreWithEnterpriseTO store = new StoreWithEnterpriseTO();
        store.setName("Store1");
        store.setLocation("Test Location");
        store.setEnterpriseTO(enterprise);
        store.setId(em.createStore(store));
        return store;
    }

    public static PlantTO createPlant(final EnterpriseTO enterprise,
                                      final IEnterpriseManager em) throws CreateException_Exception,
            NotInDatabaseException_Exception {
        final PlantTO plant = new PlantTO();
        plant.setName("Plant1");
        plant.setEnterpriseTO(enterprise);
        plant.setId(em.createPlant(plant));
        return plant;
    }
}
