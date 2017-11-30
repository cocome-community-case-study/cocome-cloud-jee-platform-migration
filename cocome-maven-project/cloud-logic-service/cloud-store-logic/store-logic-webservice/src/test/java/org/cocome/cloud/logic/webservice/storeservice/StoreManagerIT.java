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

package org.cocome.cloud.logic.webservice.storeservice;

import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.test.TestConfig;
import org.cocome.test.WSTestUtils;
import org.junit.Test;

public class StoreManagerIT {

    private static IEnterpriseManager em = WSTestUtils.createJaxWsClient(IEnterpriseManager.class,
            TestConfig.getEnterpriseServiceWSDL());
    private static IPlantManager pm = WSTestUtils.createJaxWsClient(IPlantManager.class,
            TestConfig.getPlantManagerWSDL());
    private static IStoreManager sm = WSTestUtils.createJaxWsClient(IStoreManager.class,
            TestConfig.getStoreManagerWSDL());

    @Test
    public void testAccountSale() throws Exception {
    }

}