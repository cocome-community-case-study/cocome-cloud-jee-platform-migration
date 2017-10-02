package org.cocome.cloud.webservice.enterpriseservice;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public void testCRUDForCustomProduct() throws CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception {
        final CustomProductTO customProductTO = new CustomProductTO();
        customProductTO.setName("Awsome Product");
        customProductTO.setBarcode(new Date().getTime());
        customProductTO.setPurchasePrice(10);
        customProductTO.setId(em.createCustomProduct(customProductTO));

        final CustomProductTO customProductTO2 = new CustomProductTO();
        customProductTO2.setName("Awsome Product");
        customProductTO2.setBarcode(new Date().getTime());
        customProductTO2.setPurchasePrice(10);
        customProductTO2.setId(em.createCustomProduct(customProductTO2));

        final Collection<CustomProductTO> products = em.getAllCustomProducts();
        Assert.assertNotNull(products);
        Assert.assertTrue(products.size() >= 2);

        Assert.assertEquals(customProductTO2.getBarcode(),
                em.queryCustomProductByBarcode(customProductTO2.getBarcode()).getBarcode());

        final CustomProductTO queriedInstance = em.queryCustomProductByID(customProductTO.getId());
        Assert.assertEquals(customProductTO.getName(), queriedInstance.getName());
        Assert.assertEquals(customProductTO.getPurchasePrice(), queriedInstance.getPurchasePrice(), 0.001);

        em.deleteCustomProduct(customProductTO);
        em.deleteCustomProduct(customProductTO2);
        try {
            em.queryCustomProductByID(customProductTO.getId());
            Assert.fail("Expect ");
        } catch (final NotInDatabaseException_Exception ex) {
            //no-op
        }
    }

    @Test
    public void testCRUDForEntryPoint() throws CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception {
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