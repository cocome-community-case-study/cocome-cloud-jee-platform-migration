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

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.registry.service.Names;
import org.cocome.logic.webservice.storeservice.IStoreManager;
import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.ICashDeskRegistryFactory;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import java.net.URISyntaxException;
import java.util.List;

@WebService(serviceName = "IStoreManagerService",
        name = "IStoreManager",
        endpointInterface = "org.cocome.logic.webservice.storeservice.IStoreManager",
        targetNamespace = "http://store.webservice.logic.cocome.org/")
@Stateless
public class StoreManager implements IStoreManager {

    @Inject
    private IStoreInventoryManagerLocal storeManager;

    @Inject
    private IStoreInventoryLocal storeInventory;

    @Inject
    private ICashDeskRegistryFactory contextFactory;

    @Inject
    private IStoreQuery storeQuery;

    @Inject
    private IEnterpriseQuery enterpriseQuery;

    @Inject
    private IPersistenceContext persistenceContext;

    @Inject
    private IApplicationHelper applicationHelper;

    @Inject
    private IStoreDataFactory storeFactory;

    @Inject
    private String storeManagerWSDL;

    @Inject
    private long defaultStoreIndex;

    private static final Logger LOG = Logger.getLogger(StoreManager.class);

    private void setContextRegistry(long storeID) throws NotInDatabaseException {
        LOG.debug("Setting store to store with id " + storeID);
        IStore store = storeQuery.queryStoreById(storeID);
        long enterpriseID = store.getEnterprise().getId();

        IContextRegistry registry = new CashDeskRegistry("store#" + storeID);
        registry.putLong(RegistryKeys.STORE_ID, storeID);
        registry.putLong(RegistryKeys.ENTERPRISE_ID, enterpriseID);

        contextFactory.setStoreContext(registry);

        try {
            applicationHelper.registerComponent(Names.getStoreManagerRegistryName(defaultStoreIndex), storeManagerWSDL, false);
            applicationHelper.registerComponent(Names.getStoreManagerRegistryName(storeID), storeManagerWSDL, false);
        } catch (URISyntaxException e) {
            LOG.error("Error registering component: " + e.getMessage());
        }
    }

    @Override
    public void accountSale(long storeID, SaleTO sale)
            throws ProductOutOfStockException, NotInDatabaseException, UpdateException {
        setContextRegistry(storeID);
        storeManager.accountSale(storeID, sale);
    }

    @Override
    public StoreWithEnterpriseTO getStore(long storeID) throws NotInDatabaseException {
        setContextRegistry(storeID);
        return storeManager.getStore(storeID);
    }

    @Override
    public List<ProductWithStockItemTO> getProductsWithLowStock(long storeID)
            throws NotInDatabaseException {
        setContextRegistry(storeID);
        return storeManager.getProductsWithLowStock(storeID);
    }

    @Override
    public List<ProductWithSupplierTO> getAllStoreProducts(long storeID)
            throws NotInDatabaseException {
        setContextRegistry(storeID);
        return storeManager.getAllProducts(storeID);
    }

    @Override
    public List<ProductWithSupplierAndStockItemTO> getProductsWithStockItems(long storeID)
            throws NotInDatabaseException {
        setContextRegistry(storeID);
        return storeManager.getProductsWithStockItems(storeID);
    }

    @Override
    public List<ComplexOrderTO> orderProducts(long storeID, ComplexOrderTO complexOrder)
            throws NotInDatabaseException, CreateException, UpdateException {
        setContextRegistry(storeID);
        return storeManager.orderProducts(storeID, complexOrder);
    }

    @Override
    public ComplexOrderTO getOrder(long storeID, long orderId)
            throws NotInDatabaseException {
        setContextRegistry(storeID);
        return storeManager.getOrder(storeID, orderId);
    }

    @Override
    public List<ComplexOrderTO> getOutstandingOrders(long storeID)
            throws NotInDatabaseException {
        setContextRegistry(storeID);
        return storeManager.getOutstandingOrders(storeID);
    }

    @Override
    public void rollInReceivedOrder(long storeID, long orderId)
            throws InvalidRollInRequestException, NotInDatabaseException, UpdateException {
        setContextRegistry(storeID);
        storeManager.rollInReceivedOrder(storeID, orderId);
    }

    @Override
    public ProductWithStockItemTO changePrice(long storeID, StockItemTO stockItemTO)
            throws NotInDatabaseException, UpdateException {
        LOG.debug("Changing price from stockItem " + stockItemTO.getId() + " to " + stockItemTO.getSalesPrice());
        setContextRegistry(storeID);
        return storeManager.changePrice(storeID, stockItemTO);
    }

    @Override
    public void markProductsUnavailableInStock(long storeID, ProductMovementTO movedProductAmounts)
            throws ProductOutOfStockException, NotInDatabaseException, UpdateException {
        setContextRegistry(storeID);
        storeManager.markProductsUnavailableInStock(storeID, movedProductAmounts);
    }

    @Override
    public ComplexOrderEntryTO[] getStockItems(long storeID, ProductTO[] requiredProductTOs)
            throws NotInDatabaseException, NotImplementedException {
        setContextRegistry(storeID);
        return storeManager.getStockItems(storeID, requiredProductTOs);
    }

    @Override
    public ProductWithStockItemTO getProductWithStockItem(long storeID, long productBarcode)
            throws NoSuchProductException, NotInDatabaseException {
        setContextRegistry(storeID);
        return storeInventory.getProductWithStockItem(storeID, productBarcode);
    }

    @Override
    public void updateStockItem(long storeID, StockItemTO stockItemTO) throws NotInDatabaseException, UpdateException {
        setContextRegistry(storeID);
        storeManager.updateStockItem(storeID, stockItemTO);
    }

    @Override
    public void createStockItem(long storeID, ProductWithStockItemTO stockItemTO)
            throws NotInDatabaseException, CreateException {
        setContextRegistry(storeID);
        storeManager.createStockItem(storeID, stockItemTO);
    }

    @Override
    public void onProductionFinish(long plantOperationOrderEntryId) {
        LOG.info("Finished production for order entry: " + plantOperationOrderEntryId);
    }

    @Override
    public void onProductionOrderEntryFinish(long productionOrderEntryId) {
        LOG.info("Finished production order entry: " + productionOrderEntryId);
    }

    @Override
    public void onProductionOrderFinish(long productionOrderId) {
        LOG.info("Finished production order " + productionOrderId);
    }
}
