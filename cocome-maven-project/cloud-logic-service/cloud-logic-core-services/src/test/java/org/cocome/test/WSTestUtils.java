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
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;

import java.util.UUID;

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

    public static EnterpriseTO getOrCreateEnterprise(final IEnterpriseManager em)
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

    public static StoreWithEnterpriseTO getOrCreateStore(final EnterpriseTO enterprise,
                                                         final IEnterpriseManager em) throws CreateException_Exception {
        final StoreWithEnterpriseTO store = new StoreWithEnterpriseTO();
        store.setName("Store1");
        store.setLocation("Test Location");
        store.setEnterpriseTO(enterprise);
        store.setId(em.createStore(store));
        return store;
    }

    public static PlantTO getOrCreatePlant(final EnterpriseTO enterprise,
                                           final IEnterpriseManager em) throws CreateException_Exception,
            NotInDatabaseException_Exception {
        final PlantTO plant = new PlantTO();
        plant.setName("Plant1");
        plant.setEnterpriseTO(enterprise);
        plant.setId(em.createPlant(plant));
        return plant;
    }
}
