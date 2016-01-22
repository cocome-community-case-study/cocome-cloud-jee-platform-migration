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

package org.cocome.tradingsystem.inventory.data.generator;

import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContextLocal;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.util.java.Lists;

/**
 * Container holding the generated database content.
 * <p>
 * <i>The class allows other package classes to access its fields directly and should not be used outside this package.</i>
 * 
 * @author Lubomir Bulej
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DatabaseContent {
	
	@Inject
	IPersistenceContextLocal pctx;

	//
	// The shared and unique stock items are held separately so that
	// we can determine to which kind of product they belong.
	//
	final List<TradingEnterprise> enterprises = Lists.newArrayList();
	final List<Store> stores = Lists.newArrayList();
	final List<Product> products = Lists.newArrayList();
	final List<ProductSupplier> suppliers = Lists.newArrayList();
	final List<StockItem> sharedStockItems = Lists.newArrayList();
	final List<StockItem> uniqueStockItems = Lists.newArrayList();
	final List<ProductOrder> productOrders = Lists.newArrayList();
	final List<OrderEntry> orderEntries = Lists.newArrayList();

	//

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	void makePersistent() throws CreateException {
		__makePersistent(enterprises);
		__makePersistent(stores);
		__makePersistent(suppliers);
		__makePersistent(products);
		__makePersistent(sharedStockItems);
		__makePersistent(uniqueStockItems);
		__makePersistent(productOrders);
		__makePersistent(orderEntries);
	}

	private <T> void __makePersistent(
			final Collection<T> entities) throws CreateException {
		for (final T entity : entities) {
			pctx.createEntity(entity);
		}
	}

}
