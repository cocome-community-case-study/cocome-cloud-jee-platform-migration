/***************************************************************************
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
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.data.persistence;

import javax.ejb.CreateException;
import javax.ejb.Local;

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;

/**
 * Persistence context to persist changes made into the database.
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */
@Local
public interface IPersistenceContextLocal {
	/**
	 * Creates a new product order in the database.
	 * 
	 * @param productOrder
	 * 			the product order to create
	 * @throws CreateException 
	 */
	public void createEntity(IProductOrder productOrder) throws CreateException;
	
	/**
	 * Updates the given product order entry object.
	 * 
	 * @param productOrder
	 * 			the product order entry object to update
	 * @throws UpdateException 
	 */
	public void updateEntity(IProductOrder productOrder) throws UpdateException;
	
	/**
	 * Creates a new stock item in the database.
	 * 
	 * @param stockItem
	 * 			the stock item to create
	 * @throws CreateException 
	 */
	public void createEntity(IStockItem stockItem) throws CreateException;
	
	/**
	 * Updates a stock item with the new values given.
	 * 
	 * @param stockItem
	 * 			the updated stock item
	 * @throws UpdateException 
	 */
	public void updateEntity(IStockItem stockItem) throws UpdateException;
	
	/**
	 * Creates a new trading enterprise in the database.
	 * 
	 * @param enterprise
	 * 			the trading enterprise to create
	 * @throws CreateException 
	 */
	public void createEntity(ITradingEnterprise enterprise) throws CreateException;
	
	/**
	 * Updates a trading enterprise with the new values given.
	 * 
	 * @param enterprise
	 * 			the updated trading enterprise
	 * @throws UpdateException 
	 */
	public void updateEntity(ITradingEnterprise enterprise) throws UpdateException;
	
	/**
	 * Creates a new store in the database.
	 * 
	 * @param store
	 * 			the store to create
	 * @throws CreateException 
	 */
	public void createEntity(IStore store) throws CreateException;
	
	/**
	 * Updates a store with the new values given.
	 * 
	 * @param store
	 * 			the updated store
	 * @throws UpdateException 
	 */
	public void updateEntity(IStore store) throws UpdateException;
	
	/**
	 * Creates a new product supplier in the database.
	 * 
	 * @param supplier
	 * 			the product supplier to create
	 * @throws CreateException 
	 */
	public void createEntity(IProductSupplier supplier) throws CreateException;
	
	/**
	 * Updates a product supplier with the new values given.
	 * 
	 * @param supplier
	 * 			the updated product supplier
	 * @throws UpdateException 
	 */
	public void updateEntity(IProductSupplier supplier) throws UpdateException;
	
	/**
	 * Creates a new product in the database.
	 * 
	 * @param product
	 * 			the product to create
	 * @throws CreateException 
	 */
	public void createEntity(IProduct product) throws CreateException;
	
	/**
	 * Updates a product with the new values given.
	 * 
	 * @param product
	 * 			the updated product
	 * @throws UpdateException 
	 */
	public void updateEntity(IProduct product) throws UpdateException;
	
	/**
	 * Creates a new user in the database.
	 * 
	 * @param user
	 * 			the user to create
	 * @throws CreateException 
	 */
	public void createEntity(IUser user) throws CreateException;
	
	/**
	 * Updates a user with the new values given.
	 * 
	 * @param user
	 * 			the updated user
	 * @throws UpdateException 
	 */
	public void updateEntity(IUser user) throws UpdateException;
	
	/**
	 * Creates a new customer in the database.
	 * 
	 * @param customer
	 * 			the customer to create
	 * @throws CreateException 
	 */
	public void createEntity(ICustomer customer) throws CreateException;
	
	/**
	 * Updates a customer with the new values given.
	 * 
	 * @param customer
	 * 			the updated customer
	 * @throws UpdateException 
	 */
	public void updateEntity(ICustomer customer) throws UpdateException;

//	/**
//	 * Creates a new order entry in the database.
//	 * 
//	 * @param oe
//	 * 			the order entry to create 
//	 * @throws CreateException 
//	 */
//	public void createEntity(OrderEntry oe) throws CreateException;
	
	/**
	 * Creates a new entity in the database.
	 * 
	 * @param entity
	 * 			the entity to create
	 * @throws CreateException 
	 */
	public <T> void createEntity(T entity) throws CreateException;
	
	/**
	 * Updates an entity with the new values given.
	 * 
	 * @param entity
	 * 			the updated entity
	 * @throws UpdateException 
	 */
	public <T> void updateEntity(T entity) throws UpdateException;

}
