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

package org.cocome.tradingsystem.inventory.application.store;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.productdispatcher.IProductDispatcherLocal;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContextLocal;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.store.IStoreQueryLocal;
import org.cocome.tradingsystem.inventory.data.store.OrderEntry;
import org.cocome.tradingsystem.inventory.data.store.ProductOrder;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.java.Lists;
import org.cocome.tradingsystem.util.java.Maps;
import org.cocome.tradingsystem.util.qualifier.StoreRequired;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

/**
 * Implements the server part of the store application.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 */
@Stateless
public class StoreServer implements Serializable, IStoreInventoryManagerLocal, IStoreInventoryLocal {

	private static final long serialVersionUID = -529765757261183369L;

	private static final Logger __log__ = Logger.getLogger(StoreServer.class);

	//

	@EJB
	private IStoreQueryLocal __storeQuery;

	@EJB
	private IPersistenceContextLocal pctx;
	//

	/** Contains the identifier of the corresponding store entity. */
	@Inject @StoreRequired
	private IContextRegistry context;
	
	@Inject
	Provider<OrderEntry> entryProvider;
	
	@Inject
	Provider<ProductOrder> orderProvider;

	/** Remote reference to the product dispatcher. */
	@EJB
	private IProductDispatcherLocal __dispatcher;

	//private long __storeId;

	@PostConstruct
	private void __setUpStore() {
		__log__.debug("Setting up store with ID " + context.getLong(RegistryKeys.STORE_ID));
	}
	
	//
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ProductWithStockItemTO changePrice(long storeID, final StockItemTO stockItemTO) 
			throws NotInDatabaseException, UpdateException {
		final StockItem si = __storeQuery.queryStockItemById(
				stockItemTO.getId());

		si.setSalesPrice(stockItemTO.getSalesPrice());
		pctx.updateEntity(si);

		return FillTransferObjects.fillProductWithStockItemTO(si);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductWithSupplierTO> getAllProducts(long storeID) throws NotInDatabaseException {
		final Collection<Product> products = __storeQuery.queryProducts(
				storeID);

		final List<ProductWithSupplierTO> result = Lists.newArrayList();
		for (final Product product : products) {
			result.add(FillTransferObjects.fillProductWithSupplierTO(product));
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductWithSupplierAndStockItemTO> getProductsWithStockItems(long storeID) throws NotInDatabaseException {
		final Collection<StockItem> stockItems = __storeQuery
				.queryAllStockItems(storeID);

		final List<ProductWithSupplierAndStockItemTO> result = Lists
				.newArrayList();
		for (final StockItem stockItem : stockItems) {
			result.add(FillTransferObjects
					.fillProductWithSupplierAndStockItemTO(stockItem));
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComplexOrderTO getOrder(long storeID, final long orderId) throws NotInDatabaseException {
		return FillTransferObjects.fillComplexOrderTO(__storeQuery
				.queryOrderById(orderId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ComplexOrderTO> getOutstandingOrders(long storeID) throws NotInDatabaseException {
		final Collection<ProductOrder> orders = __storeQuery
				.queryOutstandingOrders(storeID);

		final List<ComplexOrderTO> result = Lists.newArrayList();
		for (final ProductOrder order : orders) {
			result.add(FillTransferObjects.fillComplexOrderTO(order));
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductWithStockItemTO> getProductsWithLowStock(long storeID) {
		final Collection<StockItem> stockItems = __storeQuery
				.queryLowStockItems(storeID);

		final List<ProductWithStockItemTO> result = Lists.newArrayList();
		for (final StockItem si : stockItems) {
			result.add(FillTransferObjects.fillProductWithStockItemTO(si));
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StoreWithEnterpriseTO getStore(long storeID) throws NotInDatabaseException {
		return FillTransferObjects.fillStoreWithEnterpriseTO(__storeQuery
				.queryStoreById(storeID));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ComplexOrderTO> orderProducts(long storeID, final ComplexOrderTO complexOrder) 
			throws NotInDatabaseException, CreateException, UpdateException {
		final IStoreQueryLocal sq = __storeQuery;

		final HashMap<Long, List<OrderEntry>> ordersBySupplier = Maps
				.newHashMap();
		
		final LinkedList<StockItem> updatedStockItems = new LinkedList<StockItem>();
		
		for (final ComplexOrderEntryTO coeto : complexOrder.getOrderEntryTOs()) {
			final Product product = sq.queryProductByBarcode(coeto.getProductTO()
					.getBarcode());

			__debug("Found product %d", coeto.getProductTO().getBarcode());

			OrderEntry oe = entryProvider.get();
			oe.setProduct(product);
			oe.setAmount(coeto.getAmount());
			oe.setProductBarcode(product.getBarcode());

			// this is probably not necessary to persist because it gets 
			// persisted with the ProductOrder later on
			// pctx.createEntity(oe);

			//

			ProductSupplier supplier = product.getSupplier();
			long supplierId = -1;
			
			// It is possible that there is no supplier entry for a product...
			// Perhaps throw an exception in this case
			if (supplier != null) {
				supplierId = product.getSupplier().getId();
			}
			
			List<OrderEntry> entries = ordersBySupplier.get(supplierId);
			if (entries == null) {
				entries = Lists.newArrayList();
				ordersBySupplier.put(supplierId, entries);
			}
			entries.add(oe);
			
			StockItem item = sq.queryStockItem(storeID, oe.getProductBarcode());
			item.setIncomingAmount(item.getIncomingAmount() + oe.getAmount());
			updatedStockItems.add(item);
		}

		//

		System.out.println(ordersBySupplier);
		final Store store = sq.queryStoreById(storeID);
		final List<ProductOrder> orders = Lists.newArrayList();
		for (final List<OrderEntry> orderEntries : ordersBySupplier.values()) {
			ProductOrder po = orderProvider.get();
			po.setOrderEntries(orderEntries);
			po.setStore(store);
			// set OrderingDate to NOW
			po.setOrderingDate(new Date());
			po.setDeliveryDate(null);

			pctx.createEntity(po);

			orders.add(po);
		}
		
		for (StockItem item : updatedStockItems) {
			pctx.updateEntity(item);
		}

		//

		final List<ComplexOrderTO> result = Lists.newArrayList();
		for (final ProductOrder order : orders) {
			result.add(FillTransferObjects.fillComplexOrderTO(order));
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void rollInReceivedOrder(long storeID, final long orderId)
			throws InvalidRollInRequestException, NotInDatabaseException, UpdateException {
		final ProductOrder order = __storeQuery.queryOrderById(orderId);

		//
		// Ignore the roll in if the order has been already rolled in.
		//
		if (order.getDeliveryDate() != null && order.getDeliveryDate().after(order.getOrderingDate())) {
			final String message = String.format(
					"Product order %d already rolled in.", order.getId());

			__warn(message);
			throw new InvalidRollInRequestException(message);
		}

		//
		// Ignore the roll in if the order is for different store.
		//
		if (order.getStore().getId() != storeID) {
			final String message = String.format(
					"Order in store %d cannot be rolled-in by store %d", order
							.getStore().getId(), storeID);

			__error(message);
			throw new InvalidRollInRequestException(message);
		}

		// set DeliveryDate to NOW
		order.setDeliveryDate(new Date());

		for (final OrderEntry oe : order.getOrderEntries()) {
			final StockItem si = __storeQuery.queryStockItem(storeID, oe
					.getProduct().getBarcode());

			//
			// Create a new stock item for completely new products.
			//
			if (si == null) {
				// TODO Create a new stock item if it does not exist
			}

			final Product product = si.getProduct();
			final long oldAmount = si.getAmount();
			final long newAmount = oldAmount + oe.getAmount();
			final long newIncoming = si.getIncomingAmount() - oe.getAmount();
			
			si.setAmount(newAmount);
			if (newIncoming >= 0) {
				si.setIncomingAmount(newIncoming);
			} else {
				si.setIncomingAmount(0);
				__warn("New incoming amount of %s (%d) was negative (%d).", product.getName(), 
						product.getBarcode(), newIncoming);	
			}
			
			pctx.updateEntity(si);

			__debug("%s (%d) stock increased from %d to %d.",
					product.getName(), product.getBarcode(), oldAmount,
					newAmount);
		}
		pctx.updateEntity(order);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductWithStockItemTO getProductWithStockItem(long storeID, 
			final long productBarCode) throws NoSuchProductException {
		final StockItem stockItem = __storeQuery.queryStockItem(storeID,
				productBarCode);

		if (stockItem == null) {
			throw new NoSuchProductException(
					"There is no stock item for product with barcode "
							+ productBarCode);
		}

		return FillTransferObjects.fillProductWithStockItemTO(stockItem);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accountSale(long storeID, final SaleTO sale) 
			throws ProductOutOfStockException, NotInDatabaseException, UpdateException {
		__bookSale(storeID, sale);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void __bookSale(long storeID, final SaleTO saleTO) 
			throws ProductOutOfStockException, NotInDatabaseException, UpdateException {
		for (final ProductWithStockItemTO pwsto : saleTO.getProductTOs()) {
			final StockItem si = __storeQuery.queryStockItemById(pwsto
					.getStockItemTO().getId());

			long amount = si.getAmount();
			
			if (amount == 0) {
				// Normally this should not happen...
				throw new ProductOutOfStockException(
								"The requested product is not in stock anymore!");
			}
			
			si.setAmount(si.getAmount() - 1);
			pctx.updateEntity(si);
		}
		//
		// Check for items running low on stock. Required for UC 8.
		// Alternative (and probably better) design would be to check
		// once in a while from separate thread, not on every sale.
		//
		try {
			__checkForLowRunningGoods(storeID);

		} catch (final Exception e) {
			__warn("Failed UC8! Could not transport low-stock items from other stores: %s",
					e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComplexOrderEntryTO[] getStockItems(long storeID, 
			final ProductTO[] requiredProductTOs) throws NotImplementedException {
		// TODO Auto-generated method stub
		throw new NotImplementedException("TODO: SDQ implement");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void markProductsUnavailableInStock(long storeID, 
			final ProductMovementTO movedProducts)
			throws ProductOutOfStockException, UpdateException {
		for (final ProductAmountTO movedProduct : movedProducts
				.getProductAmounts()) {
			final ProductTO productTO = movedProduct.getProduct();
			final long barcode = productTO.getBarcode();
			final StockItem stockItem = __storeQuery.queryStockItem(storeID,
					barcode);

			if (stockItem == null) {
				throw new ProductOutOfStockException(String.format(
						"Store %d has no product with barcode %d", storeID,
						barcode));
			}

			//

			final long availableAmount = stockItem.getAmount();
			final long movedAmount = movedProduct.getAmount();

			if (availableAmount < movedAmount) {
				throw new ProductOutOfStockException(
						String.format(
								"Store %d only has %d product(s) with barcode %d, but %d required",
								storeID, availableAmount, barcode,
								movedAmount));
			}

			// set new remaining stock amount
			stockItem.setAmount(availableAmount - movedAmount);
			pctx.updateEntity(stockItem);

			//
			// TODO: virtual printout is missing
			// A list of all products that need to be delivered should be
			// printed out.
			//
			final StoreTO originStore = movedProducts.getOriginStore();
			final StoreTO destinationStore = movedProducts
					.getDestinationStore();
			System.out.printf(
					"[%s at %s] Ship %s, barcode %d to %s at %s, amount %d\n",
					originStore.getName(), originStore.getLocation(),
					productTO.getName(), barcode, destinationStore.getName(),
					destinationStore.getLocation(), movedAmount);
		}
	}

	/**
	 * Checks for goods that run low. If there are goods running low they
	 * transported from nearby stores in the enterprise.
	 * <p>
	 * Technically, the operation is performed by the product dispatcher. The
	 * store only needs to provide it with products that are low on stock. If
	 * there is any problem communicating with the product dispatcher, the
	 * operation will not be performed. In case of transient errors, the
	 * operation may succeed during next check for low-stock products.
	 * <p>
	 * Required for UC 8
	 */
	private void __checkForLowRunningGoods(long storeID) throws Exception {
		//
		// Determine the products and amounts of items that are
		// actually required, i.e. items that are really low on
		// stock, including their current incoming amount.
		//
		final Collection<ProductAmountTO> requiredProducts = __findRequiredProducts(storeID);

		if (requiredProducts.size() < 1) {
			return;
		}

		//
		// Order required products from stores determined by the
		// product dispatcher.
		//
		final ProductAmountTO[] incomingProducts = __orderRequiredProducts(storeID, 
				requiredProducts);

		if (incomingProducts.length < 1) {
			return;
		}

		//
		// Mark the products coming from other stores as incoming.
		//
		__registerIncomingProducts(storeID, incomingProducts);
	}

	private Collection<ProductAmountTO> __findRequiredProducts(long storeID) {
		//
		// Query the store inventory for apparently low stock items,
		// without consider items coming from other stores.
		//
		final Collection<StockItem> lowStockItems = __storeQuery
				.queryLowStockItems(storeID);
		if (lowStockItems.size() < 1) {
			return Collections.emptyList();
		}

		//
		// Filter the low-stock items to determine items that are really
		// low on stock and should be transported from other stores.
		//
		final Collection<StockItem> itemsToOrder = __selectItemsToOrder(storeID, lowStockItems);
		if (itemsToOrder.size() < 1) {
			return Collections.emptyList();
		}

		//
		// Finally determine the product amounts that need ot be transported
		// from nearby stores.
		//
		return __calculateRequiredAmounts(storeID, itemsToOrder);
	}

	/**
	 * Selects and returns stock items that are really low on stock and will be
	 * ordered from other stores. Many items can be low on stock, but have more
	 * stock incoming that along with the current stock satisfies the minimal
	 * stock condition. Such items are filtered out and only those really low on
	 * stock are left.
	 */
	private Collection<StockItem> __selectItemsToOrder(long storeID,
			final Collection<StockItem> stockItems) {
		final Collection<StockItem> result = new LinkedList<StockItem>();
		SCAN: for (final StockItem stockItem : stockItems) {
			final Product product = stockItem.getProduct();
			__debug("\t%s, barcode %d, amount %d, incoming %d, min stock %d",
					product.getName(), product.getBarcode(),
					stockItem.getAmount(), stockItem.getIncomingAmount(),
					stockItem.getMinStock());

			final long virtualAmount = stockItem.getAmount()
					+ stockItem.getIncomingAmount();
			if (virtualAmount >= stockItem.getMinStock()) {
				__debug("\t\tvirtual stock %d => not low stock", virtualAmount);
				continue SCAN;
			}

			result.add(stockItem);
		}

		__debug("%d really low-stock items in store %d", result.size(),
				storeID);
		return result;
	}

	/**
	 * Orders by default the minimum stock items for each low running
	 * product/good.
	 * <p>
	 * Required for UC 8
	 * 
	 * @param stockItems
	 *            collection of product stock items that run low
	 * @return Collection of Product/Amount tuples for each product, which
	 *         represents the required amount of each product.
	 */
	private Collection<ProductAmountTO> __calculateRequiredAmounts(long storeID,
			final Collection<StockItem> stockItems) {
		final Collection<ProductAmountTO> result = Lists.newArrayList();

		//
		// Order at least minimum stock for each item, but do not exceed stock
		// limits. The stock of each item in the collections is guaranteed lower
		// than the minimum (including the incoming amount), so we will never
		// exceed the maximum level.
		//
		for (final StockItem stockItem : stockItems) {
			long orderAmount = stockItem.getMinStock();
			if (2 * stockItem.getMinStock() >= stockItem.getMaxStock()) {
				orderAmount = stockItem.getMaxStock() - stockItem.getMinStock();
			}

			final ProductAmountTO pa = new ProductAmountTO();
			pa.setProduct(FillTransferObjects.fillProductTO(stockItem
					.getProduct()));
			pa.setAmount(orderAmount);

			result.add(pa);
		}

		__debug("%d products to be ordered by store %d", result.size(),
				storeID);
		return result;
	}

	/**
	 * Requests the product dispatcher to determine the stores to transfer goods
	 * from and to issue the product movement orders. Returns the amounts of
	 * items incoming from other stores.
	 * @throws NotInDatabaseException 
	 * 
	 * @throws ComponentNotAvailableException
	 *             if the product dispatcher cannot be found
	 * @throws RemoteException
	 *             if there is a problem communicating with the dispatcher
	 */
	private ProductAmountTO[] __orderRequiredProducts(
			long storeID,
			final Collection<ProductAmountTO> requiredProducts) throws NotInDatabaseException {
		//
		// Connect to the product dispatcher and order the required products
		// from other stores in the enterprise. Do nothing if the connection
		// cannot be established.
		final Store store = __storeQuery.queryStoreById(storeID);

		final ProductAmountTO[] result = __dispatcher
				.dispatchProductsFromOtherStores(store.getId(),
						requiredProducts);

		__debug("%d products incoming to store %d", result.length, storeID);
		return result;
	}

	/**
	 * Registers the products coming from other stores by increasing the
	 * incoming amount of stock items corresponding to the incoming products.
	 * @throws UpdateException 
	 */
	private void __registerIncomingProducts(
			long storeID,
			final ProductAmountTO[] incomingProducts) throws UpdateException {
		for (final ProductAmountTO incomingProductTO : incomingProducts) {
			final ProductTO incomingProduct = incomingProductTO.getProduct();
			final StockItem stockItem = __storeQuery.queryStockItem(storeID,
					incomingProduct.getBarcode());

			final long incomingAmount = incomingProductTO.getAmount();
			stockItem.setIncomingAmount(stockItem.getIncomingAmount()
					+ incomingAmount);
			pctx.updateEntity(stockItem);

			__debug("\t%s, barcode %d, incoming amount %d",
					incomingProduct.getName(), incomingProduct.getBarcode(),
					incomingAmount);
		}
	}

	private static void __debug(final String format, final Object... args) {
		__log(Level.DEBUG, format, args);
	}

	private static void __warn(final String format, final Object... args) {
		__log(Level.WARN, format, args);
	}

	private static void __error(final String format, final Object... args) {
		__log(Level.ERROR, format, args);
	}

	private static void __log(final Level level, final String format,
			final Object... args) {
		if (__log__.isEnabledFor(level)) {
			__log__.log(level, String.format(format, args));
		}
	}

	@Override
	public ProductWithStockItemTO updateStockItem(long storeID,
			StockItemTO stockItemTO) throws NotInDatabaseException,
			UpdateException {
		final StockItem si = __storeQuery.queryStockItemById(
				stockItemTO.getId());

		si.setSalesPrice(stockItemTO.getSalesPrice());
		si.setAmount(stockItemTO.getAmount());
		si.setMaxStock(stockItemTO.getMaxStock());
		si.setMinStock(stockItemTO.getMinStock());
		
		pctx.updateEntity(si);

		return FillTransferObjects.fillProductWithStockItemTO(si);
	}

}
