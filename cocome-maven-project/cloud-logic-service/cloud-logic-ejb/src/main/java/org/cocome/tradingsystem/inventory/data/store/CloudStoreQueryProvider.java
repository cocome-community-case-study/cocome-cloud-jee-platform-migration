package org.cocome.tradingsystem.inventory.data.store;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.remote.access.connection.GetXMLFromBackend;
import org.cocome.tradingsystem.remote.access.parsing.CSVHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;


/**
 * The objects returned will only have their basic datatype attributes filled.
 * 
 * @author Tobias Pöppke
 *
 */
@Stateless
public class CloudStoreQueryProvider implements IStoreQueryLocal {
	
	// TODO either cache the retrieved objects or provide faster queries which
	// return objects with only the simple attribute types set and other queries which
	// query all attributes of the objects
	
	private static final Logger LOG = Logger.getLogger(CloudStoreQueryProvider.class);
	
	@Inject
	GetXMLFromBackend backendConnection;
	
	@Inject
	CSVHelper csvHelper;
	
	@Override
	public Store queryStore(String name, String location) {
		String locationQuery = "*";
		
		if (!location.equals("")) {
			locationQuery = location;
		}
		
		List<Store> stores = (List<Store>) csvHelper.getStoresFromCSV(
				backendConnection.getStores("name=LIKE'" + name + "';Store.location=LIKE'" + locationQuery + "'"));
		
		if (stores.size() > 1) {
			LOG.warn("More than one store with name " + name + 
					" and location " + location + " was found!");
		}

		try {
			return stores.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public Store queryStoreById(long storeId) throws NotInDatabaseException {
		try {
			Store store = csvHelper.getStoresFromCSV(
					backendConnection.getStores("id==" + storeId)).iterator().next();			
			return store;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException(
					"Store with ID " + storeId + " could not be found!");
		}
	}

	// TODO don't call this method, because there is no way to retrieve the stock item id
	@Override
	public StockItem queryStockItemById(long stockItemId) throws NotInDatabaseException {
		try {
			StockItem item = csvHelper.getStockItemsFromCSV(
					backendConnection.getStockItems("id==" + stockItemId)).iterator().next();
			return item;
		} catch  (NoSuchElementException e) {
			throw new NotInDatabaseException("StockItem with ID " 
					+ stockItemId + " could not be found!");
		}
	}

	@Override
	public Product queryProductById(long productId) throws NotInDatabaseException {
		Product product = null;
		try {
			product = csvHelper.getProductsFromCSV(
					backendConnection.getProducts("id==" + productId)).iterator().next();
			product.setId(productId);
			return product;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("Product with ID " + productId + " could not be found!");
		}
	}

	@Override
	public Product queryProductByBarcode(long barcode) {
		Product product = null;
		try {
			product = csvHelper.getProductsFromCSV(
					backendConnection.getProducts("barcode==" + barcode)).iterator().next();
		} catch (NoSuchElementException e) {
			// Do nothing, no product found
		}
		return product;
	}

	@Override
	public ProductOrder queryOrderById(long orderId) throws NotInDatabaseException {
		ProductOrder productOrder;
		try {
			productOrder = csvHelper.getProductOrdersFromCSV(
					backendConnection.getProductOrder("id==" + orderId)).iterator().next();
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("Order with ID " + orderId + " could not be found!");
		}
		return productOrder;
	}

	@Override
	public Collection<Product> queryProducts(long storeId) {
		Collection<Product> products = new LinkedList<Product>();
		for (StockItem item : queryAllStockItems(storeId)) {
			products = csvHelper.getProductsFromCSV(
					backendConnection.getProducts("barcode==" + item.getProductBarcode()));
		}
		return products;
	}

	@Override
	public Collection<ProductOrder> queryOutstandingOrders(long storeId) {
		Collection<ProductOrder> productOrders = csvHelper.getProductOrdersFromCSV(
				backendConnection.getProductOrder("store.id==" + storeId + ";ProductOrder.deliveryDate=<e.orderingDate"));
		return productOrders;
	}

	@Override
	public Collection<StockItem> queryAllStockItems(long storeId) {
		Collection<StockItem> stockItems = csvHelper.getStockItemsFromCSV(
				backendConnection.getStockItems("store.id==" + storeId));
		return stockItems;
	}

	@Override
	public Collection<StockItem> queryLowStockItems(long storeId) {
		// Hacky way to get the result. We have to use e.minStock as comparison because
		// using StockItem.minStock will not be parsed and the query will return an error
		Collection<StockItem> stockItems = csvHelper.getStockItemsFromCSV(
				backendConnection.getStockItems("store.id==" + storeId + ";StockItem.amount=<e.minStock"));
		return stockItems;
	}

	@Override
	public StockItem queryStockItem(long storeId, long productBarcode) {
		StockItem item = null;
		try {
			item = csvHelper.getStockItemsFromCSV(
					backendConnection.getStockItems("product.barcode==" + productBarcode 
					+ ";StockItem.store.id==" + storeId)).iterator().next();
		} catch (NoSuchElementException e) {
			// Do nothing, just return null and don't crash
		}
		return item;
	}

	@Override
	public Collection<StockItem> queryStockItemsByProductId(long storeId,
			long[] productIds) {
		List<StockItem> items = new LinkedList<StockItem>();
		for (long productId : productIds) {
			Collection<StockItem> stockItems = csvHelper.getStockItemsFromCSV(
					backendConnection.getStockItems("store.id==" + storeId + ";product.id==" + productId));
			items.addAll(stockItems);
		}
		return items;
	}

	@Override
	public ProductOrder queryProductOrder(long storeId,
			long productBarcode, long amount) throws NotInDatabaseException {
		Collection<ProductOrder> productOrders = csvHelper.getProductOrdersFromCSV(
				backendConnection.getProductOrder("amount==" + amount
						+ "store.id==" + storeId 
						+ ";product.barcode==" + productBarcode));
		try {
			return productOrders.iterator().next();
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("The product order for product barcode " + productBarcode 
					+ " with amount " + amount + " could not be found in store " + storeId);
		}
	}

	@Override
	public Collection<ProductOrder> queryAllOrders(long storeId) {
		Collection<ProductOrder> productOrders = csvHelper.getProductOrdersFromCSV(
				backendConnection.getProductOrder("store.id==" + storeId));
		return productOrders;
	}
}
