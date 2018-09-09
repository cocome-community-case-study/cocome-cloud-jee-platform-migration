package org.cocome.cloud.cli;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.cocome.cloud.logic.stub.*;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;
import org.cocome.tradingsystem.inventory.application.plant.iface.PUCImporter;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.FMU;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.XPPU;
import org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.*;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;
import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.OnDemandItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanParameter;
import org.cocome.tradingsystem.util.JavaSEConfigLoader;

import java.util.*;

public class CoCoMEAdm {

    public static void main(final String... args)
            throws CreateException_Exception, NotInDatabaseException_Exception, RecipeException_Exception {
        final IEnterpriseManager em = createJaxWsClient(IEnterpriseManager.class,
                JavaSEConfigLoader.getEnterpriseServiceWSDL());
        final IPlantManager pm = createJaxWsClient(IPlantManager.class,
                JavaSEConfigLoader.getPlantManagerWSDL());
        final IStoreManager sm = createJaxWsClient(IStoreManager.class,
                JavaSEConfigLoader.getStoreManagerWSDL());

        final org.cocome.tradingsystem.inventory.application.store.EnterpriseTO enterprise = createEnterprise(em);
        final PlantTO plant = createPlant(enterprise, em);
        final StoreWithEnterpriseTO store = createStore(enterprise, em);

        final String xppuEndpoint;
        if (args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            xppuEndpoint = args[0];
            System.out.println("Used URL to xPPU: " + xppuEndpoint);
        } else {
            xppuEndpoint = "";
            System.out.println("Using mocked xPPU to populate CoCoME instance");
            System.out.println("Add xPPU URL as command line argument to use real xPPU instance instead");
        }

        /* Environmental setup */

        final PUCImporter xppu;
        //if (xppuEndpoint.isEmpty()) {
            xppu = new PUCImporter("xPPU", XPPU.values(), plant, pm);
        //} else {
        //    xppu = new PUCImporter("xPPU", xppuEndpoint, plant, pm);
        //}
        final PUCImporter fmu = new PUCImporter("FMU", FMU.values(), plant, pm);

        /* Production Units */

        final ProductionUnitTO xppu1 = new ProductionUnitTO();
        xppu1.setPlant(plant);
        xppu1.setProductionUnitClass(xppu.getProductionUnitClass());
        if (xppuEndpoint.isEmpty()) {
            xppu1.setDouble(true);
            xppu1.setInterfaceUrl("test.org");
        } else {
            xppu1.setDouble(false);
            xppu1.setInterfaceUrl(xppuEndpoint);
        }
        xppu1.setLocation("Some Place 1");
        xppu1.setId(pm.createProductionUnit(xppu1));

        final ProductionUnitTO fmu3 = new ProductionUnitTO();
        fmu3.setPlant(plant);
        fmu3.setProductionUnitClass(fmu.getProductionUnitClass());
        fmu3.setDouble(true);
        fmu3.setInterfaceUrl("dummy2.org");
        fmu3.setLocation("Some Place 3");
        fmu3.setId(pm.createProductionUnit(fmu3));

        /* Plant Operations */

        final org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO operation1 = new org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO();
        operation1.setName("Produce Yogurt");
        operation1.setPlant(plant);
        operation1.setMarkup(new org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo(
                Arrays.asList(
                        xppu.getOperation(XPPU.Crane_ACT_Init),
                        xppu.getOperation(XPPU.Stack_ACT_Init),
                        xppu.getOperation(XPPU.Stamp_ACT_Init),
                        xppu.getOperation(XPPU.Stack_ACT_ProvideWP),
                        xppu.getOperation(XPPU.Crane_ACT_PickUpWP),
                        xppu.getOperation(XPPU.Crane_ACT_TurnToStamp),
                        xppu.getOperation(XPPU.Crane_ACT_PutDownWP),
                        xppu.getOperation(XPPU.Stamp_ACT_Stamp),
                        xppu.getOperation(XPPU.Crane_ACT_PickUpWP),
                        xppu.getOperation(XPPU.Crane_ACT_TurnToConveyor),
                        xppu.getOperation(XPPU.Crane_ACT_PutDownWP),
                        xppu.getOperation(XPPU.ACT_ToBase),
                        new org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionInfo(
                                "Organic",
                                IBooleanParameter.TRUE_VALUE,
                                Arrays.asList(
                                        xppu.getOperation(XPPU.ACT_DriveFromBaseToRamp1)),
                                Arrays.asList(
                                        xppu.getOperation(XPPU.ACT_DriveFromBaseToRamp2))
                        ),
                        fmu.getOperation(FMU.Silo0_ACT_Init),
                        fmu.getOperation(FMU.Silo1_ACT_Init),
                        fmu.getOperation(FMU.Silo2_ACT_Init)
                )));
        operation1.setId(em.createPlantOperation(operation1));

        final EntryPointTO op1out1 = new EntryPointTO();
        op1out1.setName("ISO 12345 Cargo");
        op1out1.setOperation(operation1);
        op1out1.setDirection(EntryPointTO.DirectionTO.OUTPUT);
        op1out1.setId(em.createEntryPoint(op1out1));

        final org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO param = new org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO();
        param.setCategory("Ingredients");
        param.setName("Organic");
        param.setOperation(operation1);
        param.setId(em.createBooleanParameter(param));

        final org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO operation2 = new org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO();
        operation2.setName("Create Package");
        operation2.setPlant(plant);
        operation2.setMarkup(new org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo(Arrays.asList(
                xppu.getOperation(XPPU.Crane_ACT_Init),
                fmu.getOperation(FMU.Silo0_ACT_Init),
                fmu.getOperation(FMU.Silo2_ACT_Init)
        )));
        operation2.setId(em.createPlantOperation(operation2));

        final EntryPointTO op2out1 = new EntryPointTO();
        op2out1.setName("ISO 33333 Bottle");
        op2out1.setOperation(operation2);
        op2out1.setDirection(EntryPointTO.DirectionTO.OUTPUT);
        op2out1.setId(em.createEntryPoint(op2out1));

        final org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO opr2param = new org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO();
        opr2param.setCategory("Bottle");
        opr2param.setOptions(new HashSet<>(Arrays.asList("Glass", "Plastic")));
        opr2param.setName("Bottle");
        opr2param.setOperation(operation2);
        opr2param.setId(em.createNominalParameter(opr2param));

        final org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO operation3 = new org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO();
        operation3.setName("Package Yogurt");
        operation3.setPlant(plant);
        operation3.setMarkup(new MarkupInfo(Arrays.asList(
                xppu.getOperation(XPPU.Crane_ACT_Init),
                fmu.getOperation(FMU.Silo0_ACT_Init),
                fmu.getOperation(FMU.Silo2_ACT_Init)
        )));
        operation3.setId(em.createPlantOperation(operation3));

        final EntryPointTO op3in1 = new EntryPointTO();
        op3in1.setName("ISO 12345 Cargo");
        op3in1.setOperation(operation3);
        op3in1.setDirection(EntryPointTO.DirectionTO.INPUT);
        op3in1.setId(em.createEntryPoint(op3in1));

        final EntryPointTO op3in2 = new EntryPointTO();
        op3in2.setName("ISO 33333 Bottle");
        op3in2.setOperation(operation3);
        op3in2.setDirection(EntryPointTO.DirectionTO.INPUT);
        op3in2.setId(em.createEntryPoint(op3in2));

        final EntryPointTO op3out1 = new EntryPointTO();
        op3out1.setName("ISO 321 Package");
        op3out1.setOperation(operation3);
        op3out1.setDirection(EntryPointTO.DirectionTO.OUTPUT);
        op3out1.setId(em.createEntryPoint(op3out1));

        /* Custom Product creation */

        final CustomProductTO customProduct = new CustomProductTO();
        customProduct.setBarcode(new Date().getTime());
        customProduct.setName("Yogurt");
        customProduct.setPurchasePrice(10);
        customProduct.setId(em.createProduct(customProduct));

        /* Product creation */

        final ProductTO product = new ProductTO();
        product.setBarcode(new Date().getTime() + 1024);
        product.setName("Cola");
        product.setPurchasePrice(10);
        product.setId(em.createProduct(product));

        /* Recipe creation */

        final RecipeTO recipe = new RecipeTO();
        recipe.setName("Yogurt Recipe");
        recipe.setCustomProduct(customProduct);
        recipe.setEnterprise(enterprise);
        recipe.setId(em.createRecipe(recipe));

        final RecipeNodeTO node1 = new RecipeNodeTO();
        node1.setRecipe(recipe);
        node1.setOperation(operation1);
        node1.setId(em.createRecipeNode(node1));

        final RecipeNodeTO node2 = new RecipeNodeTO();
        node2.setRecipe(recipe);
        node2.setOperation(operation2);
        node2.setId(em.createRecipeNode(node2));

        final RecipeNodeTO node3 = new RecipeNodeTO();
        node3.setRecipe(recipe);
        node3.setOperation(operation3);
        node3.setId(em.createRecipeNode(node3));

        final org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO cparam1 = new org.cocome.tradingsystem.inventory.application.plant.parameter.BooleanParameterTO();
        cparam1.setCategory("Ingredients");
        cparam1.setName("Organic");
        cparam1.setOperation(recipe);
        cparam1.setId(em.createBooleanParameter(cparam1));

        final org.cocome.tradingsystem.inventory.application.plant.parameter.NominalParameterTO cparam2 = new NominalParameterTO();
        cparam2.setCategory("Packaging");
        cparam2.setName("Bottle");
        cparam2.setOptions(new HashSet<>(Arrays.asList("Glass", "Plastic")));
        cparam2.setOperation(recipe);
        cparam2.setId(em.createNominalParameter(cparam2));

        final EntryPointTO recipeOut1 = new EntryPointTO();
        recipeOut1.setName("ISO 321 Package");
        recipeOut1.setOperation(recipe);
        recipeOut1.setDirection(EntryPointTO.DirectionTO.OUTPUT);
        recipeOut1.setId(em.createEntryPoint(recipeOut1));

        final ParameterInteractionTO interaction1 = new ParameterInteractionTO();
        interaction1.setFrom(cparam1);
        interaction1.setTo(param);
        interaction1.setRecipe(recipe);
        interaction1.setId(em.createParameterInteraction(interaction1));

        final ParameterInteractionTO interaction2 = new ParameterInteractionTO();
        interaction2.setFrom(cparam2);
        interaction2.setTo(opr2param);
        interaction2.setRecipe(recipe);
        interaction2.setId(em.createParameterInteraction(interaction2));

        final EntryPointInteractionTO epInteraction1 = new EntryPointInteractionTO();
        epInteraction1.setFrom(op1out1);
        epInteraction1.setTo(op3in1);
        epInteraction1.setRecipe(recipe);
        epInteraction1.setId(em.createEntryPointInteraction(epInteraction1));

        final EntryPointInteractionTO epInteraction2 = new EntryPointInteractionTO();
        epInteraction2.setFrom(op2out1);
        epInteraction2.setTo(op3in2);
        epInteraction2.setRecipe(recipe);
        epInteraction2.setId(em.createEntryPointInteraction(epInteraction2));

        final EntryPointInteractionTO epInteraction3 = new EntryPointInteractionTO();
        epInteraction3.setFrom(op3out1);
        epInteraction3.setTo(recipeOut1);
        epInteraction3.setRecipe(recipe);
        epInteraction3.setId(em.createEntryPointInteraction(epInteraction3));

        em.validateRecipe(recipe);

        /* Store inventory creation */

        final ProductWithItemTO item1 = new ProductWithItemTO();
        final OnDemandItemTO onDemandItem = new OnDemandItemTO();
        onDemandItem.setSalesPrice(12);
        item1.setItem(onDemandItem);
        item1.setProduct(customProduct);
        item1.getItem().setId(sm.createItem(store.getId(), item1));

        final ProductWithItemTO item2 = new ProductWithItemTO();
        item2.setItem(new StockItemTO(10,5,20,12));
        item2.setProduct(product);
        item2.getItem().setId(sm.createItem(store.getId(), item2));
    }

    @SuppressWarnings("unchecked")
    private static <T> T createJaxWsClient(final Class<T> clientClass, final String url) {
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(clientClass);
        factory.setAddress(url);
        return (T) factory.create();
    }

    private static StoreWithEnterpriseTO createStore(final EnterpriseTO enterprise,
                                                     final IEnterpriseManager em) throws CreateException_Exception {
        final StoreWithEnterpriseTO store = new StoreWithEnterpriseTO();
        store.setName("Store1");
        store.setLocation("Test Location");
        store.setEnterpriseTO(enterprise);
        store.setId(em.createStore(store));
        return store;
    }

    private static EnterpriseTO createEnterprise(final IEnterpriseManager em)
            throws CreateException_Exception, NotInDatabaseException_Exception {
        final String enterpriseName = "TestEnterprise";
        final EnterpriseTO enterprise;
        try {
            enterprise = em.queryEnterpriseByName(enterpriseName);
        } catch (NotInDatabaseException_Exception e) {
            em.createEnterprise(enterpriseName);
            return em.queryEnterpriseByName(enterpriseName);
        }
        return enterprise;
    }

    private static PlantTO createPlant(final EnterpriseTO enterprise,
                                       final IEnterpriseManager em) throws CreateException_Exception {
        final PlantTO plant = new PlantTO();
        plant.setName("Plant1");
        plant.setLocation("Test Location");
        plant.setEnterpriseTO(enterprise);
        plant.setId(em.createPlant(plant));
        return plant;
    }
}
