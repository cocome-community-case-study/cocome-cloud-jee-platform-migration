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
import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.remote.access.connection.IPersistenceConnection;

import javax.ejb.*;
import javax.inject.Inject;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@Local(IPersistenceContext.class)
public class CloudPersistenceContext implements IPersistenceContext {
    // TODO make these calls asynchronous by pushing them into a JMS queue
    // and implement bean that asynchronously tries to persist the changes

    // TODO create query class to hold information about the queries

    private static final Logger LOG = Logger.getLogger(CloudPersistenceContext.class);

    @Inject
    private IPersistenceConnection postData;

    private static final Pattern INSERT_ID_PATTERN = Pattern.compile("\\[id=(\\d+)]");

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

        stockItem.setId(fetchDatabaseID(postData.getResponse()));
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

        enterprise.setId(fetchDatabaseID(postData.getResponse()));
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

        productOrder.setId(fetchDatabaseID(postData.getResponse()));
    }

    @Override
    public void updateEntity(ITradingEnterprise enterprise) throws UpdateException {
        String content = ServiceAdapterEntityConverter.getUpdateEnterpriseContent(enterprise);

        try {
            postData.sendUpdateQuery("TradingEnterprise", ServiceAdapterHeaders.ENTERPRISE_UPDATE_HEADER, content);
        } catch (IOException e) {
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

        store.setId(fetchDatabaseID(postData.getResponse()));
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

        supplier.setId(fetchDatabaseID(postData.getResponse()));
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

        product.setId(fetchDatabaseID(postData.getResponse()));
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

        customer.setId(fetchDatabaseID(postData.getResponse()));
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
            LOG.error("Could not execute post because of an IOException: " + e.getMessage());
            throw new UpdateException("Could not delete entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not delete entity!");
        }
    }

    @Override
    public void createEntity(IPlant plant) throws CreateException {
        createEntity(plant,
                "Plant",
                ServiceAdapterEntityConverter.getCreatePlantContent(plant),
                ServiceAdapterHeaders.PLANT_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IPlant plant) throws UpdateException {
        updateEntity("Plant",
                ServiceAdapterEntityConverter.getUpdatePlantContent(plant),
                ServiceAdapterHeaders.PLANT_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IPlant plant) throws UpdateException {
        deleteEntity("Plant",
                ServiceAdapterEntityConverter.getUpdatePlantContent(plant),
                ServiceAdapterHeaders.PLANT_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IProductionUnitClass puc) throws CreateException {
        createEntity(puc,
                "ProductionUnitClass",
                ServiceAdapterEntityConverter.getCreateProductionUnitClassContent(puc),
                ServiceAdapterHeaders.PRODUCTIONUNITCLASS_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IProductionUnitClass puc) throws UpdateException {
        updateEntity("ProductionUnitClass",
                ServiceAdapterEntityConverter.getUpdateProductionUnitClassContent(puc),
                ServiceAdapterHeaders.PRODUCTIONUNITCLASS_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IProductionUnitClass puc) throws UpdateException {
        deleteEntity("ProductionUnitClass",
                ServiceAdapterEntityConverter.getUpdateProductionUnitClassContent(puc),
                ServiceAdapterHeaders.PRODUCTIONUNITCLASS_UPDATE_HEADER);
    }

    @Override
    public void createEntity(ICustomProduct customProduct) throws CreateException {
        createEntity(customProduct,
                "CustomProduct",
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

    @Override
    public void createEntity(IProductionUnitOperation operation) throws CreateException {
        createEntity(operation,
                "ProductionUnitOperation",
                ServiceAdapterEntityConverter.getCreateProductionUnitOperationContent(operation),
                ServiceAdapterHeaders.PRODUCTIONUNITOPERATION_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IProductionUnitOperation operation) throws UpdateException {
        updateEntity("ProductionUnitOperation",
                ServiceAdapterEntityConverter.getUpdateProductionUnitOperationContent(operation),
                ServiceAdapterHeaders.PRODUCTIONUNITOPERATION_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IProductionUnitOperation operation) throws UpdateException {
        deleteEntity("ProductionUnitOperation",
                ServiceAdapterEntityConverter.getUpdateProductionUnitOperationContent(operation),
                ServiceAdapterHeaders.PRODUCTIONUNITOPERATION_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IEntryPoint entryPoint) throws CreateException {
        createEntity(entryPoint,
                "EntryPoint",
                ServiceAdapterEntityConverter.getCreateEntryPointContent(entryPoint),
                ServiceAdapterHeaders.ENTRYPOINT_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IEntryPoint entryPoint) throws UpdateException {
        updateEntity("EntryPoint",
                ServiceAdapterEntityConverter.getUpdateEntryPointContent(entryPoint),
                ServiceAdapterHeaders.ENTRYPOINT_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IEntryPoint entryPoint) throws UpdateException {
        deleteEntity("EntryPoint",
                ServiceAdapterEntityConverter.getUpdateEntryPointContent(entryPoint),
                ServiceAdapterHeaders.ENTRYPOINT_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IBooleanCustomProductParameter param) throws CreateException {
        createEntity(param,
                "BooleanCustomProductParameter",
                ServiceAdapterEntityConverter.getCreateBooleanCustomProductParameterContent(param),
                ServiceAdapterHeaders.BOOLEAN_CUSTOM_PRODUCT_PARAM_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IBooleanCustomProductParameter param) throws UpdateException {
        updateEntity("BooleanCustomProductParameter",
                ServiceAdapterEntityConverter.getUpdateBooleanCustomProductParameterContent(param),
                ServiceAdapterHeaders.BOOLEAN_CUSTOM_PRODUCT_PARAM_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IBooleanCustomProductParameter param) throws UpdateException {
        deleteEntity("BooleanCustomProductParameter",
                ServiceAdapterEntityConverter.getUpdateBooleanCustomProductParameterContent(param),
                ServiceAdapterHeaders.BOOLEAN_CUSTOM_PRODUCT_PARAM_UPDATE_HEADER);
    }

    @Override
    public void createEntity(INorminalCustomProductParameter param) throws CreateException {
        createEntity(param,
                "NorminalCustomProductParameter",
                ServiceAdapterEntityConverter.getCreateNorminalCustomProductParameterContent(param),
                ServiceAdapterHeaders.NORMINAL_CUSTOM_PRODUCT_PARAM_CREATE_HEADER);
    }

    @Override
    public void updateEntity(INorminalCustomProductParameter param) throws UpdateException {
        updateEntity("NorminalCustomProductParameter",
                ServiceAdapterEntityConverter.getUpdateNorminalCustomProductParameterContent(param),
                ServiceAdapterHeaders.NORMINAL_CUSTOM_PRODUCT_PARAM_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(INorminalCustomProductParameter param) throws UpdateException {
        deleteEntity("NorminalCustomProductParameter",
                ServiceAdapterEntityConverter.getUpdateNorminalCustomProductParameterContent(param),
                ServiceAdapterHeaders.NORMINAL_CUSTOM_PRODUCT_PARAM_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IPlantOperation operation) throws CreateException {
        createEntity(operation,
                "PlantOperation",
                ServiceAdapterEntityConverter.getCreatePlantOperationContent(operation),
                ServiceAdapterHeaders.PLANTOPERATION_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IPlantOperation operation) throws UpdateException {
        updateEntity("PlantOperation",
                ServiceAdapterEntityConverter.getUpdatePlantOperationContent(operation),
                ServiceAdapterHeaders.PLANTOPERATION_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IPlantOperation operation) throws UpdateException {
        deleteEntity("PlantOperation",
                ServiceAdapterEntityConverter.getUpdatePlantOperationContent(operation),
                ServiceAdapterHeaders.PLANTOPERATION_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IBooleanPlantOperationParameter param,
                             IPlantOperation operation) throws CreateException {
        createEntity(param,
                "BooleanPlantOperationParameter",
                ServiceAdapterEntityConverter.getCreateBooleanPlantOperationParameterContent(param, operation),
                ServiceAdapterHeaders.BOOLEAN_PLANT_OPERATION_PARAM_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IBooleanPlantOperationParameter param,
                             IPlantOperation operation) throws UpdateException {
        updateEntity("BooleanPlantOperationParameter",
                ServiceAdapterEntityConverter.getUpdateBooleanPlantOperationParameterContent(param, operation),
                ServiceAdapterHeaders.BOOLEAN_PLANT_OPERATION_PARAM_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IBooleanPlantOperationParameter param,
                             IPlantOperation operation) throws UpdateException {
        deleteEntity("BooleanPlantOperationParameter",
                ServiceAdapterEntityConverter.getUpdateBooleanPlantOperationParameterContent(param, operation),
                ServiceAdapterHeaders.BOOLEAN_PLANT_OPERATION_PARAM_UPDATE_HEADER);
    }

    @Override
    public void createEntity(INorminalPlantOperationParameter param,
                             IPlantOperation operation) throws CreateException {
        createEntity(param,
                "NorminalPlantOperationParameter",
                ServiceAdapterEntityConverter.getCreateNorminalPlantOperationParameterContent(param, operation),
                ServiceAdapterHeaders.NORMINAL_PLANT_OPERATION_PARAM_CREATE_HEADER);
    }

    @Override
    public void updateEntity(INorminalPlantOperationParameter param,
                             IPlantOperation operation) throws UpdateException {
        updateEntity("NorminalPlantOperationParameter",
                ServiceAdapterEntityConverter.getUpdateNorminalPlantOperationParameterContent(param, operation),
                ServiceAdapterHeaders.NORMINAL_PLANT_OPERATION_PARAM_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(INorminalPlantOperationParameter param,
                             IPlantOperation operation) throws UpdateException {
        deleteEntity("NorminalPlantOperationParameter",
                ServiceAdapterEntityConverter.getUpdateNorminalPlantOperationParameterContent(param, operation),
                ServiceAdapterHeaders.NORMINAL_PLANT_OPERATION_PARAM_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IEntryPointInteraction interaction) throws CreateException {
        createEntity(interaction,
                "EntryPointInteraction",
                ServiceAdapterEntityConverter.getCreateInteractionContent(interaction),
                ServiceAdapterHeaders.ENTRYPOINTINTERACTION_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IEntryPointInteraction interaction) throws UpdateException {
        updateEntity("EntryPointInteraction",
                ServiceAdapterEntityConverter.getUpdateInteractionContent(interaction),
                ServiceAdapterHeaders.ENTRYPOINTINTERACTION_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IEntryPointInteraction interaction) throws UpdateException {
        deleteEntity("EntryPointInteraction",
                ServiceAdapterEntityConverter.getUpdateInteractionContent(interaction),
                ServiceAdapterHeaders.ENTRYPOINTINTERACTION_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IParameterInteraction interaction) throws CreateException {
        createEntity(interaction,
                "ParameterInteraction",
                ServiceAdapterEntityConverter.getCreateInteractionContent(interaction),
                ServiceAdapterHeaders.PARAMETERINTERACTION_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IParameterInteraction interaction) throws UpdateException {
        updateEntity("ParameterInteraction",
                ServiceAdapterEntityConverter.getUpdateInteractionContent(interaction),
                ServiceAdapterHeaders.PARAMETERINTERACTION_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IParameterInteraction interaction) throws UpdateException {
        deleteEntity("ParameterInteraction",
                ServiceAdapterEntityConverter.getUpdateInteractionContent(interaction),
                ServiceAdapterHeaders.PARAMETERINTERACTION_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IRecipe recipe) throws CreateException {
        createEntity(recipe,
                "Recipe",
                ServiceAdapterEntityConverter.getCreateRecipeContent(recipe),
                ServiceAdapterHeaders.RECIPE_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IRecipe recipe) throws UpdateException {
        updateEntity("Recipe",
                ServiceAdapterEntityConverter.getUpdateRecipeContent(recipe),
                ServiceAdapterHeaders.RECIPE_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IRecipe recipe) throws UpdateException {
        deleteEntity("Recipe",
                ServiceAdapterEntityConverter.getUpdateRecipeContent(recipe),
                ServiceAdapterHeaders.RECIPE_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IPlantOperationOrder order) throws CreateException {
        createEntity(order,
                "PlantOperationOrder",
                ServiceAdapterEntityConverter.getCreatePlantOperationOrderContent(order),
                ServiceAdapterHeaders.PLANTOPERATIONORDER_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IPlantOperationOrder order) throws UpdateException {
        updateEntity("PlantOperationOrder",
                ServiceAdapterEntityConverter.getUpdatePlantOperationOrderContent(order),
                ServiceAdapterHeaders.PLANTOPERATIONORDER_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IPlantOperationOrder order) throws UpdateException {
        updateEntity("PlantOperationOrder",
                ServiceAdapterEntityConverter.getUpdatePlantOperationOrderContent(order),
                ServiceAdapterHeaders.PLANTOPERATIONORDER_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IPlantOperationOrderEntry orderEntry,
                             IPlantOperation operation,
                             IPlantOperationOrder order) throws CreateException {
        createEntity(orderEntry,
                "PlantOperationOrderEntry",
                ServiceAdapterEntityConverter.getCreatePlantOperationOrderEntryContent(orderEntry, operation, order),
                ServiceAdapterHeaders.PLANTOPERATIONENTRY_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IPlantOperationOrderEntry orderEntry,
                             IPlantOperation operation,
                             IPlantOperationOrder order) throws UpdateException {
        updateEntity("PlantOperationOrderEntry",
                ServiceAdapterEntityConverter.getUpdatePlantOperationOrderEntryContent(orderEntry, operation, order),
                ServiceAdapterHeaders.PLANTOPERATIONENTRY_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IPlantOperationOrderEntry orderEntry,
                             IPlantOperation operation,
                             IPlantOperationOrder order) throws UpdateException {
        updateEntity("PlantOperationOrderEntry",
                ServiceAdapterEntityConverter.getUpdatePlantOperationOrderEntryContent(orderEntry, operation, order),
                ServiceAdapterHeaders.PLANTOPERATIONENTRY_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IPlantOperationParameterValue value, IPlantOperationOrderEntry orderEntry) throws CreateException {
        createEntity(value,
                "PlantOperationParameterValue",
                ServiceAdapterEntityConverter.getCreatePlantOperationParameterValueContent(value, orderEntry),
                ServiceAdapterHeaders.PARAMETERVALUECONTENT_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IPlantOperationParameterValue value, IPlantOperationOrderEntry orderEntry) throws UpdateException {
        updateEntity("PlantOperationParameterValue",
                ServiceAdapterEntityConverter.getUpdatePlantOperationParameterValueContent(value, orderEntry),
                ServiceAdapterHeaders.PARAMETERVALUECONTENT_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IPlantOperationParameterValue value, IPlantOperationOrderEntry orderEntry) throws UpdateException {
        updateEntity("PlantOperationParameterValue",
                ServiceAdapterEntityConverter.getUpdatePlantOperationParameterValueContent(value, orderEntry),
                ServiceAdapterHeaders.PARAMETERVALUECONTENT_UPDATE_HEADER);
    }

    @Override
    public void createEntity(IConditionalExpression expression) throws CreateException {
        createEntity(expression,
                "ConditionalExpression",
                ServiceAdapterEntityConverter.getCreateConditionalExpressionContent(expression),
                ServiceAdapterHeaders.CONDITIONALEXPRESSION_CREATE_HEADER);
    }

    @Override
    public void updateEntity(IConditionalExpression expression) throws UpdateException {
        updateEntity("ConditionalExpression",
                ServiceAdapterEntityConverter.getUpdateConditionalExpressionContent(expression),
                ServiceAdapterHeaders.CONDITIONALEXPRESSION_UPDATE_HEADER);
    }

    @Override
    public void deleteEntity(IConditionalExpression expression) throws UpdateException {
        deleteEntity("ConditionalExpression",
                ServiceAdapterEntityConverter.getUpdateConditionalExpressionContent(expression),
                ServiceAdapterHeaders.CONDITIONALEXPRESSION_UPDATE_HEADER);
    }

    private void createEntity(IIdentifiable entity,
                              String entityTypeName,
                              String content,
                              String header) throws CreateException {
        try {
            postData.sendCreateQuery(entityTypeName, header, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage(), e);
            throw new CreateException("Could not create entity!");
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new CreateException("Could not create entity!");
        }
        entity.setId(fetchDatabaseID(postData.getResponse()));
    }

    private void updateEntity(String entityTypeName,
                              String content,
                              String header) throws UpdateException {
        try {
            postData.sendUpdateQuery(entityTypeName, header, content);
        } catch (IOException e) {
            LOG.error("Could not execute post because of an IOException: " + e.getMessage(), e);
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
            LOG.error("Could not execute post because of an IOException: " + e.getMessage(), e);
            throw new UpdateException("Could not delete entity!", e);
        }

        if (!postData.getResponse().contains("SUCCESS")) {
            throw new UpdateException("Could not delete entity!");
        }
    }

    private long fetchDatabaseID(String responseString) throws CreateException {
        Matcher matcher = INSERT_ID_PATTERN.matcher(responseString);
        if (!matcher.find()) {
            throw new CreateException("Unable to fetch database id");
        }

        return Long.valueOf(matcher.group(1));
    }
}
