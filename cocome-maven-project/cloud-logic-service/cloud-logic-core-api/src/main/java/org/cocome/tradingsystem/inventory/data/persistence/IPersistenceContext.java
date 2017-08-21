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
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;

import javax.ejb.CreateException;
import javax.ejb.Local;

/**
 * Persistence context to persist changes made into the database.
 *
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */
@Local
public interface IPersistenceContext {
    /**
     * Creates a new product order in the database.
     *
     * @param productOrder the product order to create
     * @throws CreateException
     */
    void createEntity(IProductOrder productOrder) throws CreateException;

    /**
     * Updates the given product order entry object.
     *
     * @param productOrder the product order entry object to update
     * @throws UpdateException
     */
    void updateEntity(IProductOrder productOrder) throws UpdateException;

    /**
     * Creates a new stock item in the database.
     *
     * @param stockItem the stock item to create
     * @throws CreateException
     */
    void createEntity(IStockItem stockItem) throws CreateException;

    /**
     * Updates a stock item with the new values given.
     *
     * @param stockItem the updated stock item
     * @throws UpdateException
     */
    void updateEntity(IStockItem stockItem) throws UpdateException;

    /**
     * Creates a new trading enterprise in the database.
     *
     * @param enterprise the trading enterprise to create
     * @throws CreateException
     */
    void createEntity(ITradingEnterprise enterprise) throws CreateException;

    /**
     * Updates a trading enterprise with the new values given.
     *
     * @param enterprise the updated trading enterprise
     * @throws UpdateException
     */
    void updateEntity(ITradingEnterprise enterprise) throws UpdateException;

    /**
     * Creates a new store in the database.
     *
     * @param store the store to create
     * @throws CreateException
     */
    void createEntity(IStore store) throws CreateException;

    /**
     * Updates a store with the new values given.
     *
     * @param store the updated store
     * @throws UpdateException
     */
    void updateEntity(IStore store) throws UpdateException;

    /**
     * Creates a new product supplier in the database.
     *
     * @param supplier the product supplier to create
     * @throws CreateException
     */
    void createEntity(IProductSupplier supplier) throws CreateException;

    /**
     * Updates a product supplier with the new values given.
     *
     * @param supplier the updated product supplier
     * @throws UpdateException
     */
    void updateEntity(IProductSupplier supplier) throws UpdateException;

    /**
     * Creates a new product in the database.
     *
     * @param product the product to create
     * @throws CreateException
     */
    void createEntity(IProduct product) throws CreateException;

    /**
     * Updates a product with the new values given.
     *
     * @param product the updated product
     * @throws UpdateException
     */
    void updateEntity(IProduct product) throws UpdateException;

    /**
     * Creates a new user in the database.
     *
     * @param user the user to create
     * @throws CreateException
     */
    void createEntity(IUser user) throws CreateException;

    /**
     * Updates a user with the new values given.
     *
     * @param user the updated user
     * @throws UpdateException
     */
    void updateEntity(IUser user) throws UpdateException;

    /**
     * Creates a new customer in the database.
     *
     * @param customer the customer to create
     * @throws CreateException
     */
    void createEntity(ICustomer customer) throws CreateException;

    /**
     * Updates a customer with the new values given.
     *
     * @param customer the updated customer
     * @throws UpdateException
     */
    void updateEntity(ICustomer customer) throws UpdateException;

    /**
     * Deletes the enterprise from the database
     * @param enterprise the enterprise to delete
     * @throws UpdateException if deletion failed
     */
    void deleteEntity(ITradingEnterprise enterprise) throws UpdateException;
}
