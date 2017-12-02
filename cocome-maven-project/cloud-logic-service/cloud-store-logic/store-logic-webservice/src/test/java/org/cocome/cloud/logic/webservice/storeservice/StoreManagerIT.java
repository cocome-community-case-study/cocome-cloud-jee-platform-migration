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

import org.cocome.cloud.logic.stub.*;
import org.cocome.test.EnterpriseInfo;
import org.cocome.test.TestConfig;
import org.cocome.test.WSTestUtils;
import org.cocome.tradingsystem.inventory.application.store.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

public class StoreManagerIT {

    private static IEnterpriseManager em = WSTestUtils.createJaxWsClient(IEnterpriseManager.class,
            TestConfig.getEnterpriseServiceWSDL());
    private static IPlantManager pm = WSTestUtils.createJaxWsClient(IPlantManager.class,
            TestConfig.getPlantManagerWSDL());
    private static IStoreManager sm = WSTestUtils.createJaxWsClient(IStoreManager.class,
            TestConfig.getStoreManagerWSDL());

    private static EnterpriseInfo enterpriseInfo;

    @BeforeClass
    public static void initEnvironment()
            throws CreateException_Exception,
            NotInDatabaseException_Exception,
            RecipeException_Exception,
            UpdateException_Exception {
        //enterpriseInfo = WSTestUtils.createEnvironmentWithSimpleRecipe(em, pm);
    }

    @Test
    public void testNormalAccountSale() throws Exception {
        final EnterpriseTO enterprise = WSTestUtils.createEnterprise(em);
        final StoreWithEnterpriseTO store = WSTestUtils.createStore(enterprise, em);

        final ProductTO product = new ProductTO();
        product.setBarcode(new Date().getTime());
        product.setName("Test Product");
        product.setPurchasePrice(10);
        product.setId(em.createProduct(product));

        final ProductWithStockItemTO item = new ProductWithStockItemTO();
        final StockItemTO stockItem = new StockItemTO();
        stockItem.setAmount(10);
        stockItem.setMinStock(5);
        stockItem.setMaxStock(20);
        stockItem.setSalesPrice(12);
        item.setStockItemTO(stockItem);
        item.setProductTO(product);
        item.getStockItemTO().setId(sm.createStockItem(store.getId(), item));

        SaleTO sale = new SaleTO();
        sale.setProductTOs(Collections.singletonList(item));

        sm.accountSale(store.getId(), sale);
        sm.deleteStockItem(store.getId(), item);
    }

    @Test
    public void testAccountSaleWithCustomProductAsStockItem() throws Exception {
        final ProductWithStockItemTO item = new ProductWithStockItemTO();
        final StockItemTO stockItem = new StockItemTO();
        stockItem.setAmount(10);
        stockItem.setMinStock(5);
        stockItem.setMaxStock(20);
        stockItem.setSalesPrice(12);
        item.setStockItemTO(stockItem);
        item.setProductTO(enterpriseInfo.getCustomProducts().get(0).getCustomProduct());
        item.getStockItemTO().setId(sm.createStockItem(enterpriseInfo.getStores().get(0).getId(), item));

        SaleTO sale = new SaleTO();
        sale.setProductTOs(Collections.singletonList(item));

        System.out.println(item.getStockItemTO().getId());

        sm.accountSale(enterpriseInfo.getStores().get(0).getId(), sale);
    }
}