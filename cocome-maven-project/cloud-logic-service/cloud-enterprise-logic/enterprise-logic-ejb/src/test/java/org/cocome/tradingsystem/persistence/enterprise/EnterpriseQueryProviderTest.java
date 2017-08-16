package org.cocome.tradingsystem.persistence.enterprise;

import org.cocome.tradingsystem.persistence.plant.IPlantDataFactory;
import org.cocome.tradingsystem.persistence.store.IStoreQuery;
import org.cocome.tradingsystem.persistence.usermanager.IUserDataFactory;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.cocome.tradingsystem.util.Configuration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

@RunWith(Arquillian.class)
public class EnterpriseQueryProviderTest {

    @Deployment
    public static Archive<?> createDeployment() {
        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve("org.cocome:service-adapter-ejb:ejb:1.1",
                        "org.cocome:service-adapter-rest:war:1.1",
                        "de.kit.ipd:java.utils:1.1")
                .withTransitivity()
                .asFile();

        WebArchive adapterWar = null;
        JavaArchive adapterEjbJar = null;
        JavaArchive javaUtilsJar = null;
        for (File f : files) {
            if (f.getName().contains("service-adapter-rest")) {
                adapterWar = ShrinkWrap.create(ZipImporter.class, "service-adapter-rest.war")
                        .importFrom(f)
                        .as(WebArchive.class);
                continue;
            }
            if (f.getName().contains("service-adapter-ejb")) {
                adapterEjbJar = ShrinkWrap.create(ZipImporter.class, "service-adapter-ejb.jar")
                        .importFrom(f)
                        .as(JavaArchive.class);
                continue;
            }
            if (f.getName().contains("java.utils")) {
                javaUtilsJar = ShrinkWrap.create(ZipImporter.class, "java.utils.jar")
                        .importFrom(f)
                        .as(JavaArchive.class);
            }
        }

        // Embedding war package which contains the test class is needed
        // So that Arquillian can invoke test class through its servlet test runner
        final WebArchive testWar = Testable.archiveToTest(ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(EnterpriseQueryProviderTest.class)
                .addPackage(EnterpriseQueryProvider.class.getPackage())
                .addPackage(IBackendConversionHelper.class.getPackage())
                .addPackage(IBackendQuery.class.getPackage())
                .addPackage(IStoreQuery.class.getPackage())
                .addPackage(IPlantDataFactory.class.getPackage())
                .addPackage(IUserDataFactory.class.getPackage())
                .addPackage(Configuration.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"));
        return ShrinkWrap.create(EnterpriseArchive.class)
                .setApplicationXML("application.xml")
                .addAsModule(adapterWar)
                .addAsModule(adapterEjbJar)
                .addAsModule(testWar)
                .addAsLibrary(javaUtilsJar);
    }

    @Inject
    private IEnterpriseQuery enterpriseQuery;

    @Test
    public void shouldDoSomething() throws Exception {
        enterpriseQuery.queryEnterpriseById(1);
        Assert.assertNotNull(enterpriseQuery);
    }

}