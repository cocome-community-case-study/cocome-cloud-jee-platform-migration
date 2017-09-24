package plantservice;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class PlantManagerIT {

    private static IEnterpriseManager em;
    private static IPlantManager pm;

    @BeforeClass
    public static void createClient() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(IEnterpriseManager.class);
        factory.setAddress("http://127.0.0.1:40797/EnterpriseService/IEnterpriseManager");
        em = (IEnterpriseManager) factory.create();

        factory.setServiceClass(IPlantManager.class);
        factory.setAddress("http://127.0.0.1:41897/PlantService/IPlantManager");
        pm = (IPlantManager) factory.create();
    }

    /*
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
    */

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
        pm.createProductionUnitOperation(operation1);

        final ProductionUnitOperationTO operation2 = new ProductionUnitOperationTO();
        operation2.setOperationId("__OP2");
        operation2.setProductionUnitClass(puc);
        pm.createProductionUnitOperation(operation2);

        final List<ProductionUnitOperationTO> operations =
                pm.queryProductionUnitOperationsByEnterpriseID(enterprise.getId());
        Assert.assertNotNull(operations);
        Assert.assertFalse(operations.isEmpty());
        //Assert.assertEquals(2, operations.size());

        final ProductionUnitOperationTO singleInstance =
                pm.queryProductionUnitOperationByID(operations.get(1).getId());
        Assert.assertNotNull(singleInstance);
        Assert.assertEquals(operations.get(1).getId(), singleInstance.getId());
        Assert.assertEquals(operations.get(1).getOperationId(), singleInstance.getOperationId());
        for (final ProductionUnitOperationTO instance : operations) {
            pm.deleteProductionUnitOperation(instance);
        }
        Assert.assertTrue(pm.queryProductionUnitOperationsByEnterpriseID(enterprise.getId()).isEmpty());
        em.deleteProductionUnitClass(puc);
        em.deleteEnterprise(enterprise);
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
}