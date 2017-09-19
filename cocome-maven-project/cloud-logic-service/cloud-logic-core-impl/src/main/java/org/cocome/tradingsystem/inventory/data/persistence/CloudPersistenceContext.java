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

package org.cocome.tradingsystem.inventory.data.persistence;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.remote.access.connection.IPersistenceConnection;

import javax.ejb.*;
import javax.inject.Inject;
import java.io.IOException;

@Stateless
@Local(IPersistenceContext.class)
public class CloudPersistenceContext implements IPersistenceContext {
    // TODO make these calls asynchronous by pushing them into a JMS queue
    // and implement bean that asynchronously tries to persist the changes

    // TODO create query class to hold information about the queries

    private static final Logger LOG = Logger.getLogger(CloudPersistenceContext.class);

    @Inject
    private IPersistenceConnection postData;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateEntity(IProductOrder productOrder) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getProductOrderContent(productOrder);

        try {
            postData.sendUpdateQuery("ProductOrder", ServiceAdapterHeaders.PRODUCTORDER_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not update entity!", e);
        }

        String response = postData.getResponse();

        if (response.contains("FAIL") || !response.contains("SUCCESS")) {
            throw new UpdateException("Could not update entity!");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createEntity(IStockItem stockItem) throws CreateException {
        String content = ServiceAdapterEntityConverter.getStockItemContent(stockItem);
        try {
            postData.sendCreateQuery("StockItem", ServiceAdapterHeaders.STOCKITEM_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateEntity(IStockItem stockItem) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getStockItemContent(stockItem);

        try {
            postData.sendUpdateQuery("StockItem", ServiceAdapterHeaders.STOCKITEM_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not update entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not update entity!");
        }
    }

    @Override
    public void createEntity(ITradingEnterprise enterprise) throws CreateException {
        String content = ServiceAdapterEntityConverter.getCreateEnterpriseContent(enterprise);
        try {
            postData.sendCreateQuery("TradingEnterprise", ServiceAdapterHeaders.ENTERPRISE_CREATE_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity! " + e.getMessage());
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    @Override
    public void createEntity(IProductOrder productOrder) throws CreateException {
        String content = ServiceAdapterEntityConverter.getProductOrderContent(productOrder);
        try {
            postData.sendCreateQuery("ProductOrder", ServiceAdapterHeaders.PRODUCTORDER_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (postData.getResponse().contains("FAIL") || !postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    @Override
    public void updateEntity(ITradingEnterprise enterprise) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getUpdateEnterpriseContent(enterprise);

        try {
            postData.sendUpdateQuery("TradingEnterprise", ServiceAdapterHeaders.ENTERPRISE_UPDATE_HEADER, content);
        } catch (IOException e) {
            // TODO perhaps throw this exception to caller?
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not update entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not update entity!");
        }
    }

    @Override
    public void createEntity(IStore store) throws CreateException {
        String content = ServiceAdapterEntityConverter.getCreateStoreContent(store);
        try {
            postData.sendCreateQuery("Store", ServiceAdapterHeaders.STORE_CREATE_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    @Override
    public void updateEntity(IStore store) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getUpdateStoreContent(store);

        try {
            postData.sendUpdateQuery("Store", ServiceAdapterHeaders.STORE_UPDATE_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not create entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not update entity!");
        }
    }

    @Override
    public void createEntity(IProductSupplier supplier) throws CreateException {
        String content = ServiceAdapterEntityConverter.getCreateSupplierContent(supplier);
        try {
            postData.sendCreateQuery("ProductSupplier", ServiceAdapterHeaders.PRODUCTSUPPLIER_CREATE_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    @Override
    public void updateEntity(IProductSupplier supplier) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getUpdateSupplierContent(supplier);

        try {
            postData.sendUpdateQuery("ProductSupplier", ServiceAdapterHeaders.PRODUCTSUPPLIER_UPDATE_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not update entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not update entity!");
        }
    }

    @Override
    public void createEntity(IProduct product) throws CreateException {
        String content = ServiceAdapterEntityConverter.getProductContent(product);
        try {
            postData.sendCreateQuery("Product", ServiceAdapterHeaders.PRODUCT_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    @Override
    public void updateEntity(IProduct product) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getProductContent(product);

        try {
            postData.sendUpdateQuery("Product", ServiceAdapterHeaders.PRODUCT_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not update entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not update entity!");
        }
    }

    @Override
    public void createEntity(IUser user) throws CreateException {
        String content = ServiceAdapterEntityConverter.getUserContent(user);
        try {
            postData.sendCreateQuery("LoginUser", ServiceAdapterHeaders.USER_CREATE_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    @Override
    public void updateEntity(IUser user) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getUserContent(user);
        try {
            postData.sendUpdateQuery("LoginUser", ServiceAdapterHeaders.USER_UPDATE_HEADER, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not connect to the database!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not create entity!");
        }
    }

    @Override
    public void createEntity(ICustomer customer) throws CreateException {
        String content = ServiceAdapterEntityConverter.getCustomerContent(customer);

        // TODO Transactions would be good here
        createEntity(customer.getUser());

        try {
            postData.sendCreateQuery("Customer",
                    customer.getPreferredStore() == null ?
                            ServiceAdapterHeaders.CUSTOMER_CREATE_HEADER
                            : ServiceAdapterHeaders.CUSTOMER_CREATE_HEADER_WITH_STORE,
                    content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    @Override
    public void updateEntity(ICustomer customer) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getUpdateCustomerContent(customer);

        try {
            postData.sendUpdateQuery("Customer",
                    customer.getPreferredStore() == null ?
                            ServiceAdapterHeaders.CUSTOMER_UPDATE_HEADER
                            : ServiceAdapterHeaders.CUSTOMER_UPDATE_HEADER_WITH_STORE,
                    content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not connect to the database!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not create entity!");
        }
    }

    @Override
    public void deleteEntity(ITradingEnterprise enterprise) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getUpdateEnterpriseContent(enterprise);

        try {
            postData.sendDeleteQuery("TradingEnterprise", ServiceAdapterHeaders.ENTERPRISE_UPDATE_HEADER, content);
        } catch (IOException e) {
            // TODO perhaps throw this exception to caller?
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not delete entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not delete entity!");
        }
    }

    @Override
    public void deleteEntity(IPlant plant) throws UpdateException {
        deleteEntity("Plant",
                ServiceAdapterEntityConverter.getUpdatePlantContent(plant),
                ServiceAdapterHeaders.PLANT_UPDATE_HEADER);
    }

    @Override
    public void updateEntity(IPlant plant) throws UpdateException {
        updateEntity("Plant",
                ServiceAdapterEntityConverter.getUpdatePlantContent(plant),
                ServiceAdapterHeaders.PLANT_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IPlant plant) throws CreateException {
        createEntity("Plant",
                ServiceAdapterEntityConverter.getCreatePlantContent(plant),
                ServiceAdapterHeaders.PLANT_CREATE_HEADER);
    }

    @Override
    public void deleteEntity(IProductionUnitClass puc) throws UpdateException {
        deleteEntity("ProductionUnitClass",
                ServiceAdapterEntityConverter.getUpdateProductionUnitClassContent(puc),
                ServiceAdapterHeaders.PRODUCTIONUNITCLASS_UPDATE_HEADER);
    }

    @Override
    public void updateEntity(IProductionUnitClass puc) throws UpdateException {
        updateEntity("ProductionUnitClass",
                ServiceAdapterEntityConverter.getUpdateProductionUnitClassContent(puc),
                ServiceAdapterHeaders.PRODUCTIONUNITCLASS_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IProductionUnitClass puc) throws CreateException {
        createEntity("ProductionUnitClass",
                ServiceAdapterEntityConverter.getCreateProductionUnitClassContent(puc),
                ServiceAdapterHeaders.PRODUCTIONUNITCLASS_CREATE_HEADER);
    }

    @Override
    public void createEntity(ICustomProduct customProduct) throws CreateException {
        createEntity("CustomProduct",
                ServiceAdapterEntityConverter.getCreateCustomProductContent(customProduct),
                ServiceAdapterHeaders.CUSTOMPRODUCT_CREATE_HEADER);
    }

    @Override
    public void updateEntity(ICustomProduct customProduct) throws UpdateException {
        updateEntity("CustomProduct",
                ServiceAdapterEntityConverter.getUpdateCustomProductContent(customProduct),
                ServiceAdapterHeaders.CUSTOMPRODUCT_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(ICustomProduct customProduct) throws UpdateException {
        deleteEntity("CustomProduct",
                ServiceAdapterEntityConverter.getUpdateCustomProductContent(customProduct),
                ServiceAdapterHeaders.CUSTOMPRODUCT_UPDATE_HEADER);
    }

    private void createEntity(String entityTypeName,
                              String content,
                              String header) throws CreateException {
        try {
            postData.sendCreateQuery(entityTypeName, header, content);
        } catch (IOException e) {
            // TODO perhaps throw this exception to caller?
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new CreateException("Could not create entity!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
    }

    private void updateEntity(String entityTypeName,
                              String content,
                              String header) throws UpdateException {
        try {
            postData.sendUpdateQuery(entityTypeName, header, content);
        } catch (IOException e) {
            // TODO perhaps throw this exception to caller?
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not update entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not update entity!");
        }
    }

    private void deleteEntity(String entityTypeName,
                              String content,
                              String header) throws UpdateException {
        try {
            postData.sendDeleteQuery(entityTypeName, header, content);
        } catch (IOException e) {
            // TODO perhaps throw this exception to caller?
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not delete entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not delete entity!");
        }
    }
}
