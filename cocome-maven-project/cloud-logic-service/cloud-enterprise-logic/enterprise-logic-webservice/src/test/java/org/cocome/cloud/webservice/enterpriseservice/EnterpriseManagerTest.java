package org.cocome.cloud.webservice.enterpriseservice;

import org.cocome.cloud.logic.stub.IURIRegistryManager;
import org.cocome.cloud.logic.stub.IURIRegistryManagerService;
import org.cocome.cloud.logic.stub.RegistryEntry;
import org.cocome.tradingsystem.util.Configuration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceRef;

@RunWith(Arquillian.class)
public class EnterpriseManagerTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(IURIRegistryManager.class.getPackage())
                .addPackage(IURIRegistryManagerService.class.getPackage())
                .addPackage(Configuration.class.getPackage())
                .addPackage(QName.class.getPackage())
                .addPackage(RegistryEntry.class.getPackage())


                /*.addPackage(EnterpriseManager.class.getPackage())
                .addPackage(IApplicationHelper.class.getPackage())
                .addPackage(IPersistenceContext.class.getPackage())
                .addPackage(IPlantDataFactory.class.getPackage())
                .addPackage(IPersistenceConnection.class.getPackage())
                .addPackage(IBackendConversionHelper.class.getPackage())
                .addPackage(IUserDataFactory.class.getPackage())
                .addPackage(IStoreDataFactory.class.getPackage())
                .addPackage(IEnterpriseDataFactory.class.getPackage())
                .addPackage(ICashDeskRegistryFactory.class.getPackage())*/

                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @WebServiceRef(IURIRegistryManagerService.class)
    IURIRegistryManager registryManager;

    //@Inject
    //private IEnterpriseManager enterpriseManager;

    @Test
    public void shouldDoSomething() throws Exception {
        //System.out.println(enterpriseQuery.u);
        Assert.assertNotNull(registryManager);
    }

}