package org.cocome.cloud.webservice.enterpriseservice;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class EnterpriseManagerIT {

    private static IEnterpriseManager em;

    @BeforeClass
    public static void createClient() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(IEnterpriseManager.class);
        factory.setAddress("http://127.0.0.1:40797/EnterpriseService/IEnterpriseManager");
        em = (IEnterpriseManager) factory.create();
    }

    @Test
    public void retrieveAndDeleteEnterprises() throws Exception {
        if (em.getEnterprises() == null) {
            em.createEnterprise("TestEnterprise");
        }
        for (final EnterpriseTO e : em.getEnterprises()) {
            em.deleteEnterprise(e);
        }
        Assert.assertNotNull(em.getEnterprises());
        Assert.assertTrue(em.getEnterprises().isEmpty());
    }

    @Test
    public void testCRUDForPlant() throws Exception {
        final EnterpriseTO enterprise = getOrCreateEnterprise();
        PlantTO plant = new PlantTO();
        plant.setName("Plant1");
        plant.setLocation("Some Location");
        plant.setEnterpriseTO(enterprise);
        em.createPlant(plant);

        final List<PlantTO> plants = em.queryPlantsByEnterpriseID(enterprise.getId());
        Assert.assertNotNull(plants);
        Assert.assertFalse(plants.isEmpty());

        final PlantTO singleInstance = em.queryPlantByID(plants.get(0).getId());
        Assert.assertNotNull(singleInstance);
        Assert.assertEquals(plants.get(0).getId(), singleInstance.getId());
        Assert.assertEquals(plants.get(0).getName(), singleInstance.getName());
        Assert.assertEquals(plants.get(0).getLocation(), singleInstance.getLocation());
        for (final PlantTO plantTO : plants) {
            em.deletePlant(plantTO);
        }
        em.deleteEnterprise(enterprise);
    }

    @Test
    public void testCRUDForProductionUnitClass() throws Exception {
        final EnterpriseTO enterprise = getOrCreateEnterprise();
        final ProductionUnitClassTO puc = new ProductionUnitClassTO();
        puc.setName("PUC1");
        puc.setEnterprise(enterprise);
        em.createProductionUnitClass(puc);

        final List<ProductionUnitClassTO> pucs = em.queryProductionUnitClassesByEnterpriseID(enterprise.getId());
        Assert.assertNotNull(pucs);
        Assert.assertFalse(pucs.isEmpty());

        final ProductionUnitClassTO singleInstance = em.queryProductionUnitClassByID(pucs.get(0).getId());
        Assert.assertNotNull(singleInstance);
        Assert.assertEquals(pucs.get(0).getId(), singleInstance.getId());
        Assert.assertEquals(pucs.get(0).getName(), singleInstance.getName());
        for (final ProductionUnitClassTO instance : pucs) {
            em.deleteProductionUnitClass(instance);
        }
        em.deleteEnterprise(enterprise);
    }

    @Test
    public void testCRUDForProductionUnitOperation() throws Exception {
        final EnterpriseTO enterprise = getOrCreateEnterprise();
        final ProductionUnitClassTO createPuc = new ProductionUnitClassTO();
        createPuc.setName("PUC1");
        createPuc.setEnterprise(enterprise);
        em.createProductionUnitClass(createPuc);
        final ProductionUnitClassTO puc = em.queryProductionUnitClassesByEnterpriseID(enterprise.getId()).get(0);

        final ProductionUnitOperationTO operation1 = new ProductionUnitOperationTO();
        operation1.setOperationId("__OP1");
        operation1.setProductionUnitClass(puc);
        em.createProductionUnitOperation(operation1);

        final ProductionUnitOperationTO operation2 = new ProductionUnitOperationTO();
        operation2.setOperationId("__OP2");
        operation2.setProductionUnitClass(puc);
        em.createProductionUnitOperation(operation2);

        final List<ProductionUnitOperationTO> operations =
                em.queryProductionUnitOperationsByEnterpriseID(enterprise.getId());
        Assert.assertNotNull(operations);
        Assert.assertFalse(operations.isEmpty());
        //Assert.assertEquals(2, operations.size());

        final ProductionUnitOperationTO singleInstance =
                em.queryProductionUnitOperationByID(operations.get(1).getId());
        Assert.assertNotNull(singleInstance);
        Assert.assertEquals(operations.get(1).getId(), singleInstance.getId());
        Assert.assertEquals(operations.get(1).getOperationId(), singleInstance.getOperationId());
        for (final ProductionUnitOperationTO instance : operations) {
            em.deleteProductionUnitOperation(instance);
        }
        Assert.assertTrue(em.queryProductionUnitOperationsByEnterpriseID(enterprise.getId()).isEmpty());
        em.deleteProductionUnitClass(puc);
        em.deleteEnterprise(enterprise);
    }

    private EnterpriseTO getOrCreateEnterprise() throws CreateException_Exception, NotInDatabaseException_Exception {
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
}