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

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameterValue;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.expression.IConditionalExpression;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.INorminalPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.store.*;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;

import javax.ejb.CreateException;
import javax.ejb.Local;

/**
 * Persistence context to persist changes made into the database.
 *
 * @author Yannick Welsch
 * @author Tobias Pöppke
 * @author Rudolf Biczok
 */
@Local
public interface IPersistenceContext {
    /**
     * Creates a new product order in the database.
     *
     * @param productOrder the product order to create
     * @throws CreateException if create failed on database side
     */
    void createEntity(IProductOrder productOrder) throws CreateException;

    /**
     * Updates the given product order entry object.
     *
     * @param productOrder the product order entry object to update
     * @throws UpdateException if update failed on database side
     */
    void updateEntity(IProductOrder productOrder) throws UpdateException;

    /**
     * Creates a new stock item in the database.
     *
     * @param stockItem the stock item to create
     * @throws CreateException if create failed on database side
     */
    void createEntity(IStockItem stockItem) throws CreateException;

    /**
     * Updates a stock item with the new values given.
     *
     * @param stockItem the updated stock item
     * @throws UpdateException if update failed on database side
     */
    void updateEntity(IStockItem stockItem) throws UpdateException;

    /**
     * Deletes a stock item.
     *
     * @param stockItem the stock item to delete
     * @throws UpdateException if deletion failed on database side
     */
    void deleteEntity(IStockItem stockItem) throws UpdateException;

    void createEntity(IOnDemandItem onDemandItem) throws CreateException;

    void updateEntity(IOnDemandItem onDemandItem) throws UpdateException;

    void deleteEntity(IOnDemandItem onDemandItem) throws UpdateException;

    void createEntity(IItem item) throws CreateException;

    void updateEntity(IItem item) throws UpdateException;

    void deleteEntity(IItem item) throws UpdateException;

    /**
     * Creates a new trading enterprise in the database.
     *
     * @param enterprise the trading enterprise to create
     * @throws CreateException if create failed on database side
     */
    void createEntity(ITradingEnterprise enterprise) throws CreateException;

    /**
     * Updates a trading enterprise with the new values given.
     *
     * @param enterprise the updated trading enterprise
     * @throws UpdateException if update failed on database side
     */
    void updateEntity(ITradingEnterprise enterprise) throws UpdateException;

    /**
     * Creates a new store in the database.
     *
     * @param store the store to create
     * @throws CreateException if create failed on database side
     */
    void createEntity(IStore store) throws CreateException;

    /**
     * Updates a store with the new values given.
     *
     * @param store the updated store
     * @throws UpdateException if update failed on database side
     */
    void updateEntity(IStore store) throws UpdateException;

    /**
     * Creates a new product supplier in the database.
     *
     * @param supplier the product supplier to create
     * @throws CreateException if create failed on database side
     */
    void createEntity(IProductSupplier supplier) throws CreateException;

    /**
     * Updates a product supplier with the new values given.
     *
     * @param supplier the updated product supplier
     * @throws UpdateException if update failed on database side
     */
    void updateEntity(IProductSupplier supplier) throws UpdateException;

    /**
     * Creates a new product in the database.
     *
     * @param product the product to create
     * @throws CreateException if create failed on database side
     */
    void createEntity(IProduct product) throws CreateException;

    /**
     * Updates a product with the new values given.
     *
     * @param product the updated product
     * @throws UpdateException if update failed on database side
     */
    void updateEntity(IProduct product) throws UpdateException;

    /**
     * Deletes a product from the database
     *
     * @param product the product to delete
     * @throws UpdateException if the delete operation failed on database side
     */
    void deleteEntity(IProduct product) throws UpdateException;

    /**
     * Creates a new user in the database.
     *
     * @param user the user to create
     * @throws CreateException if create failed on database side
     */
    void createEntity(IUser user) throws CreateException;

    /**
     * Updates a user with the new values given.
     *
     * @param user the updated user
     * @throws UpdateException if update failed on database side
     */
    void updateEntity(IUser user) throws UpdateException;

    /**
     * Creates a new customer in the database.
     *
     * @param customer the customer to create
     * @throws CreateException if creation failed on database side
     */
    void createEntity(ICustomer customer) throws CreateException;

    /**
     * Updates a customer with the new values given.
     *
     * @param customer the updated customer
     * @throws UpdateException if update failed on database side
     */
    void updateEntity(ICustomer customer) throws UpdateException;

    /**
     * Deletes the enterprise from the database
     *
     * @param enterprise the enterprise to delete
     * @throws UpdateException if delete failed on database side
     */
    void deleteEntity(ITradingEnterprise enterprise) throws UpdateException;

    void deleteEntity(IPlant plant) throws UpdateException;

    void updateEntity(IPlant plant) throws UpdateException;

    void createEntity(IPlant plant) throws CreateException;

    void deleteEntity(IProductionUnitClass puc) throws UpdateException;

    void updateEntity(IProductionUnitClass puc) throws UpdateException;

    void createEntity(IProductionUnitClass puc) throws CreateException;

    void createEntity(IProductionUnitOperation operation) throws CreateException;

    void updateEntity(IProductionUnitOperation operation) throws UpdateException;

    void deleteEntity(IProductionUnitOperation operation) throws UpdateException;

    void createEntity(IProductionUnit unit) throws CreateException;

    void updateEntity(IProductionUnit unti) throws UpdateException;

    void deleteEntity(IProductionUnit unit) throws UpdateException;

    void createEntity(IEntryPoint ep) throws CreateException;

    void updateEntity(IEntryPoint operation) throws UpdateException;

    void deleteEntity(IEntryPoint operation) throws UpdateException;

    void createEntity(IBooleanCustomProductParameter param) throws CreateException;

    void updateEntity(IBooleanCustomProductParameter param) throws UpdateException;

    void deleteEntity(IBooleanCustomProductParameter param) throws UpdateException;

    void createEntity(INorminalCustomProductParameter param) throws CreateException;

    void updateEntity(INorminalCustomProductParameter param) throws UpdateException;

    void deleteEntity(INorminalCustomProductParameter param) throws UpdateException;

    void createEntity(IPlantOperation operation) throws CreateException;

    void updateEntity(IPlantOperation operation) throws UpdateException;

    void deleteEntity(IPlantOperation operation) throws UpdateException;

    void createEntity(IConditionalExpression expression) throws CreateException;

    void updateEntity(IConditionalExpression expression) throws UpdateException;

    void deleteEntity(IConditionalExpression expression) throws UpdateException;

    void createEntity(IBooleanPlantOperationParameter param, IPlantOperation operation) throws CreateException;

    void updateEntity(IBooleanPlantOperationParameter param, IPlantOperation operation) throws UpdateException;

    void deleteEntity(IBooleanPlantOperationParameter param, IPlantOperation operation) throws UpdateException;

    void createEntity(INorminalPlantOperationParameter param, IPlantOperation operation) throws CreateException;

    void updateEntity(INorminalPlantOperationParameter param, IPlantOperation operation) throws UpdateException;

    void deleteEntity(INorminalPlantOperationParameter param, IPlantOperation operation) throws UpdateException;

    void createEntity(IEntryPointInteraction interaction) throws CreateException;

    void updateEntity(IEntryPointInteraction interaction) throws UpdateException;

    void deleteEntity(IEntryPointInteraction interaction) throws UpdateException;

    void createEntity(IParameterInteraction interaction) throws CreateException;

    void updateEntity(IParameterInteraction interaction) throws UpdateException;

    void deleteEntity(IParameterInteraction interaction) throws UpdateException;

    void createEntity(IRecipe recipe) throws CreateException;

    void updateEntity(IRecipe recipe) throws UpdateException;

    void deleteEntity(IRecipe recipe) throws UpdateException;

    void createEntity(IPlantOperationOrder order) throws CreateException;

    void updateEntity(IPlantOperationOrder order) throws UpdateException;

    void deleteEntity(IPlantOperationOrder order) throws UpdateException;

    void createEntity(IPlantOperationOrderEntry orderEntry,
                      IPlantOperationOrder order)
            throws CreateException;

    void updateEntity(IPlantOperationOrderEntry orderEntry,
                      IPlantOperationOrder order)
            throws UpdateException;

    void deleteEntity(IPlantOperationOrderEntry orderEntry,
                      IPlantOperationOrder order)
            throws UpdateException;

    void createEntity(IPlantOperationParameterValue recipe, IPlantOperationOrderEntry orderEntry)
            throws CreateException;

    void updateEntity(IPlantOperationParameterValue recipe, IPlantOperationOrderEntry orderEntry)
            throws UpdateException;

    void deleteEntity(IPlantOperationParameterValue recipe, IPlantOperationOrderEntry orderEntry)
            throws UpdateException;

    void createEntity(IProductionOrder order) throws CreateException;

    void updateEntity(IProductionOrder order) throws UpdateException;

    void deleteEntity(IProductionOrder order) throws UpdateException;

    void createEntity(IProductionOrderEntry orderEntry,
                      IProductionOrder order)
            throws CreateException;

    void updateEntity(IProductionOrderEntry orderEntry,
                      IProductionOrder order)
            throws UpdateException;

    void deleteEntity(IProductionOrderEntry orderEntry,
                      IProductionOrder order)
            throws UpdateException;

    void createEntity(ICustomProductParameterValue value, IProductionOrderEntry orderEntry)
            throws CreateException;

    void updateEntity(ICustomProductParameterValue value, IProductionOrderEntry orderEntry)
            throws UpdateException;

    void deleteEntity(ICustomProductParameterValue value, IProductionOrderEntry orderEntry)
            throws UpdateException;
}
