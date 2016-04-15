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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Provider;

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.IOrderEntry;
import org.cocome.tradingsystem.inventory.data.store.IProductOrder;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.java.Iterables;
import org.cocome.tradingsystem.util.java.Lists;
import org.cocome.tradingsystem.util.java.Sets;

/**
 * Generates database contents. This class is responsible for creating
 * associations between primitive trading system entities produced by {@link EntityGenerator}.
 * 
 * @author Lubomir Bulej
 */
@Stateless
public class DatabaseGenerator {

	private static final long __BARCODE_BASE__ = 777;

	//

	private DatabaseConfiguration __config;

	private IEntityConfiguration __entityConfig;

	private EntityGenerator __entityGenerator;
	
	@EJB
	DatabaseContent databaseContent;
	
	@Inject
	static Provider<DatabaseGenerator> generatorProvider;

	//

//	private DatabaseGenerator(
//			final DatabaseConfiguration config,
//			final IEntityConfiguration entityConfig) {
//		__config = config;
//		__entityConfig = entityConfig;
//		__entityGenerator = new EntityGenerator(entityConfig);
//	}

	public static DatabaseContent generate(
			final DatabaseConfiguration databaseConfig,
			final IEntityConfiguration entityConfig
			) throws NotInDatabaseException {
		
		return generatorProvider.get().__generate(databaseConfig, entityConfig);
	}

	//

	private DatabaseContent __generate(final DatabaseConfiguration databaseConfig,
			final IEntityConfiguration entityConfig) throws NotInDatabaseException {
		__config = databaseConfig;
		__entityConfig = entityConfig;
		__entityGenerator = new EntityGenerator(entityConfig);
		
		//
		// Create stores to carry the products.
		//
		final List<IStore> stores = __entityGenerator.createStores(__config.getStoreCount());

		//
		// Create shared products and their suppliers and assign stock items for
		// all shared products to every store, so that each store carries the
		// shared products.
		//
		final List<IProduct> sharedProducts = __entityGenerator.createProducts(
				"Shared ", __BARCODE_BASE__,
				__config.getSharedProductCount()
				);

		final List<IProductSupplier> sharedSuppliers = __entityGenerator.createSuppliers(
				"Shared ", __config.getSharedSupplierCount()
				);
		__distributeProductsAmongSuppliers(sharedProducts, sharedSuppliers);

		final List<IStockItem> sharedStockItems = __assignProductsToStores(sharedProducts, stores);

		//
		// Create unique products and their suppliers and distribute stock items
		// for the products evenly among all stores, so that no two stores carry
		// the same product.
		//
		final List<IProduct> uniqueProducts = __entityGenerator.createProducts(
				"Unique ", __BARCODE_BASE__ + __config.getSharedProductCount(),
				__config.getUniqueProductCount()
				);

		final List<IProductSupplier> uniqueSuppliers = __entityGenerator.createSuppliers(
				"Unique ", __config.getUniqueSupplierCount()
				);
		__distributeProductsAmongSuppliers(uniqueProducts, uniqueSuppliers);

		final List<IStockItem> uniqueStockItems = __distributeProductsAmongStores(uniqueProducts, stores);

		//
		// Create enterprises and assign the stores to the enterprises. Then
		// collect suppliers from individual stores in each enterprise and
		// assign the suppliers to the respective enterprise.
		//
		final List<ITradingEnterprise> enterprises = __entityGenerator.createEnterprises(
				__config.getEnterpriseCount()
				);
		__assignStoresToEnterprises(stores, enterprises);
		__assignSuppliersToEnterprises(enterprises);

		//
		// Create product orders and distribute them among stores.
		// Create order entries and distribute them among product stores.
		//
		final List<IProductOrder> productOrders = __entityGenerator.createProductOrders(
				__config.getProductOrderCount()
				);
		__distributeProductOrdersAmongStores(productOrders, stores);

		final List<IOrderEntry> orderEntries = __entityGenerator.createOrderEntries(
				__config.getOrderEntryCount()
				);
		__distributeOrderEntriesAmongProductOrders(orderEntries, productOrders);
		__assignProductsToOrderEntries(stores);

		//
		// Create the result.
		//
		
		databaseContent.enterprises.addAll(enterprises);
		databaseContent.stores.addAll(stores);
		databaseContent.products.addAll(sharedProducts);
		databaseContent.products.addAll(uniqueProducts);
		databaseContent.suppliers.addAll(sharedSuppliers);
		databaseContent.suppliers.addAll(uniqueSuppliers);
		databaseContent.sharedStockItems.addAll(sharedStockItems);
		databaseContent.uniqueStockItems.addAll(uniqueStockItems);
		databaseContent.productOrders.addAll(productOrders);
		databaseContent.orderEntries.addAll(orderEntries);

		return databaseContent;
	}

	private void __assignProductsToOrderEntries(final List<IStore> stores) {
		//
		// For each store, get product orders and populate their order entries
		// with random products.
		//
		for (final IStore store : stores) {
			final Collection<IStockItem> items = store.getStockItems();

			//
			// For each product order, fill the order entries with randomly
			// selected products. There should be no duplicate products in
			// one product order.
			//
			for (final IProductOrder order : store.getProductOrders()) {
				final Iterator<IStockItem> randomItems = Iterables.randomIterator(items);

				for (final IOrderEntry entry : order.getOrderEntries()) {
					final IStockItem item = randomItems.next();
					entry.setProduct(item.getProduct());

					long amount = __entityConfig.getOrderEntryAmount(
							item.getAmount()
							);

					entry.setAmount(amount);

					//
					// Keep track of incoming items. This will be used to
					// adjust product stock amount and max stock attributes.
					//
					item.setIncomingAmount(item.getIncomingAmount() + amount);
				}
			}

			//
			// Ensure that the sum of amounts in order entries does not exceed
			// the amount of items available in the store. Clear the incoming
			// amount afterwards.
			//
			for (final IStockItem item : items) {
				item.setAmount(Math.max(item.getIncomingAmount(), item.getAmount()));
				item.setMaxStock(Math.max(item.getAmount(), item.getMaxStock()));
				item.setIncomingAmount(0);
			}
		}
	}

	private void __distributeOrderEntriesAmongProductOrders(
			final List<IOrderEntry> entries, final List<IProductOrder> orders
			) {
		final Iterator<IProductOrder> randomOrders = Iterables.samplingIterator(orders);
		for (final IOrderEntry entry : entries) {
			final IProductOrder order = randomOrders.next();
//			entry.setOrder(order);
			order.getOrderEntries().add(entry);
		}
	}

	private void __distributeProductOrdersAmongStores(
			final List<IProductOrder> orders, final List<IStore> stores
			) {
		final Iterator<IStore> randomStores = Iterables.samplingIterator(stores);
		for (final IProductOrder order : orders) {
			final IStore store = randomStores.next();
			order.setStore(store);
			store.getProductOrders().add(order);
		}
	}

	private static void __assignStoresToEnterprises(
			final List<IStore> stores, final List<ITradingEnterprise> enterprises
			) {
		final Iterator<ITradingEnterprise> randomEnterprises = Iterables.samplingIterator(enterprises);

		for (final IStore store : stores) {
			final ITradingEnterprise enterprise = randomEnterprises.next();
			store.setEnterprise(enterprise);
			enterprise.getStores().add(store);
		}
	}

	private static void __assignSuppliersToEnterprises(
			final List<ITradingEnterprise> enterprises
			) throws NotInDatabaseException {
		for (final ITradingEnterprise enterprise : enterprises) {
			final Set<IProductSupplier> suppliers = __collectEntepriseSuppliers(enterprise);
			enterprise.setSuppliers(suppliers);
		}
	}

	private static Set<IProductSupplier> __collectEntepriseSuppliers(
			final ITradingEnterprise enterprise
			) throws NotInDatabaseException {
		final Set<IProductSupplier> suppliers = Sets.newHashSet();
		for (final IStore store : enterprise.getStores()) {
			for (final IStockItem stockItem : store.getStockItems()) {
				suppliers.add(stockItem.getProduct().getSupplier());
			}
		}
		return suppliers;
	}

	private static void __distributeProductsAmongSuppliers(
			final List<IProduct> products, final List<IProductSupplier> suppliers
			) {
		//
		// Pick a random supplier for each product. This ensures that each
		// product has only a single supplier, while each supplier can provide
		// multiple products.
		//
		final Iterator<IProductSupplier> randomSuppliers = Iterables.samplingIterator(suppliers);

		for (final IProduct product : products) {
			product.setSupplier(randomSuppliers.next());
		}
	}

	private List<IStockItem> __assignProductsToStores(
			final List<IProduct> sharedProducts, final List<IStore> stores
			) {
		//
		// For each store, generate stock items for the given products
		// and assign the stock items to the store.
		//
		final List<IStockItem> result = Lists.newArrayList();
		for (final IStore store : stores) {
			final List<IStockItem> stockItems = __createStockItems(sharedProducts);
			for (final IStockItem stockItem : stockItems) {
				__assignStockItemToStore(stockItem, store);
			}

			result.addAll(stockItems);
		}

		return result;
	}

	private List<IStockItem> __distributeProductsAmongStores(
			final List<IProduct> uniqueProducts, final List<IStore> stores
			) {
		//
		// Generate stock items for the given products and distribute them
		// randomly among the stores.
		//
		final List<IStockItem> result = __createStockItems(uniqueProducts);
		final Iterator<IStore> randomStores = Iterables.samplingIterator(stores);

		for (final IStockItem stockItem : result) {
			final IStore store = randomStores.next();
			__assignStockItemToStore(stockItem, store);
		}

		return result;
	}

	private static void __assignStockItemToStore(
			final IStockItem stockItem, final IStore store
			) {
		stockItem.setStore(store);
		store.getStockItems().add(stockItem);
	}

	//

	private List<IStockItem> __createStockItems(
			final List<IProduct> products
			) {
		// create stock items
		final int count = products.size();
		final List<IStockItem> result = __entityGenerator.createStockItems(count);

		// map stock items to products
		for (int index = 0; index < count; index++) {
			final IProduct product = products.get(index);
			final IStockItem stockItem = result.get(index);

			stockItem.setProduct(product);
			stockItem.setSalesPrice(
					__entityConfig.getStockItemSalesPrice(product.getPurchasePrice())
					);
		}

		return result;
	}

}
