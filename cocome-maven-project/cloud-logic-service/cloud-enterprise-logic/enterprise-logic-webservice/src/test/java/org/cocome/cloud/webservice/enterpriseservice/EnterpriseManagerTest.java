package org.cocome.cloud.webservice.enterpriseservice;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.cocome.logic.webservice.enterpriseservice.IEnterpriseManager;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.junit.Assert;
import org.junit.Test;

public class EnterpriseManagerTest {

    @Test
    public void shouldDoSomething() throws Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(IEnterpriseManager.class);
        factory.setAddress("http://127.0.0.1:40797/EnterpriseService/IEnterpriseManager");
        IEnterpriseManager em = (IEnterpriseManager) factory.create();


        if (em.getEnterprises() == null) {
            em.createEnterprise("TestEnterprise");
        }
        for (final EnterpriseTO e : em.getEnterprises()) {
            em.deleteEnterprise(e);
        }
        Assert.assertNull(em.getEnterprises());
    }

}