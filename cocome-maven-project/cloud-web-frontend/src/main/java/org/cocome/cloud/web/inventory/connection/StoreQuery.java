package org.cocome.cloud.web.inventory.connection;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.InvalidRollInRequestException_Exception;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.ProductOutOfStockException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.registry.service.Names;
import org.cocome.cloud.web.inventory.store.OrderItem;
import org.cocome.cloud.web.inventory.store.ProductWrapper;
import org.cocome.cloud.web.inventory.store.Store;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierAndStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.SaleTO;

/**
 * Implements the store query interface to retrieve store related information.
 * Uses the web service interface from CoCoMEs logic.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@RequestScoped
public class StoreQuery implements IStoreQuery {
	private static final Logger LOG = Logger.getLogger(StoreQuery.class);

	IStoreManager storeManager;

	@Inject
	long defaultStoreIndex;

	@Inject
	IApplicationHelper applicationHelper;

	private IStoreManager lookupStoreManager(long storeID) throws NotInDatabaseException_Exception {
		try {
			LOG.debug(String.format("Looking up responsible store manager for store %d", storeID));
			return applicationHelper.getComponent(Names.getStoreManagerRegistryName(storeID),
					IStoreManagerService.SERVICE, IStoreManagerService.class).getIStoreManagerPort();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
			if (storeID == defaultStoreIndex) {
				LOG.error("Got exception while retrieving store manager location: " + e.getMessage());
				e.printStackTrace();
				throw new NotInDatabaseException_Exception(e.getMessage());
			} else {
				return lookupStoreManager(defaultStoreIndex);
			}
		}
	}

	@Override
	public List<ProductWrapper> queryStockItems(Store store) throws NotInDatabaseException_Exception {
		long storeID = store.getID();
		LOG.debug("Querying stock items: Looking up store server.");
		storeManager = lookupStoreManager(storeID);
		List<ProductWrapper> stockItems = new LinkedList<ProductWrapper>();
		LOG.debug("Querying stock items: Querying stock items from store server.");
		List<ProductWithSupplierAndStockItemTO> items = storeManager.getProductsWithStockItems(storeID);
		LOG.debug("Querying stock items: Creating product wrappers.");
		for (ProductWithSupplierAndStockItemTO item : items) {
			ProductWrapper newItem = new ProductWrapper(item, item.getStockItemTO(), store);
			stockItems.add(newItem);
		}
		return stockItems;
	}

	@Override
	public ProductWrapper getStockItemByProductID(Store store, long productID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductWrapper getStockItemByBarcode(Store store, long barcode) {
		return null;
	}

	@Override
	public boolean updateStockItem(Store store, ProductWrapper stockItem) {
		long storeID = store.getID();
		try {
			storeManager = lookupStoreManager(storeID);
			storeManager.updateStockItem(storeID, stockItem.getStockItemTO());
			return true;
		} catch (NotInDatabaseException_Exception | UpdateException_Exception e) {
			LOG.error(String.format("Error while updating stock item: %s\n%s", e.getMessage(), e.getStackTrace()));
		}
		return false;
	}

	@Override
	public boolean orderProducts(@NotNull Store store, @NotNull Collection<OrderItem> items) {
		ComplexOrderTO orderTO = createComplexOrderTO(items);
		
		long storeID = store.getID();
		
		try {
			storeManager = lookupStoreManager(storeID);
			storeManager.orderProducts(storeID, orderTO);
			return true;
		} catch (NotInDatabaseException_Exception | CreateException_Exception | UpdateException_Exception e) {
			LOG.error(String.format("Error while ordering products: %s\n%s", e.getMessage(), e.getStackTrace()));
		}
		return false;
	}

	private ComplexOrderTO createComplexOrderTO(Collection<OrderItem> items) {
		ComplexOrderTO orderTO = new ComplexOrderTO();
		// Ordering and delivery date will be set by the store server
		orderTO.setDeliveryDate(null);
		orderTO.setOrderingDate(null);
		
		List<ComplexOrderEntryTO> orderEntries = new ArrayList<>(items.size());
		
		for (OrderItem item : items) {
			orderEntries.add(OrderItem.convertToOrderEntryTO(item));
		}
		orderTO.setOrderEntryTOs(orderEntries);
		return orderTO;
	}

	@Override
	public List<ComplexOrderTO> getAllOrders(Store store) {
		long storeID = store.getID();
		
		try {
			storeManager = lookupStoreManager(storeID);
			return storeManager.getOutstandingOrders(storeID);
		} catch (NotInDatabaseException_Exception e) {
			LOG.error(String.format("Error while getting orders: %s\n%s", e.getMessage(), e.getStackTrace()));
		}
		return Collections.emptyList();
	}

	@Override
	public ComplexOrderTO getOrderByID(Store store, long orderID) {
		long storeID = store.getID();
		
		try {
			storeManager = lookupStoreManager(storeID);
			return storeManager.getOrder(storeID, orderID);
		} catch (NotInDatabaseException_Exception e) {
			LOG.error(String.format("Error while getting order with id %d: %s\n%s", orderID, e.getMessage(), e.getStackTrace()));
		}
		return null;
	}

	@Override
	public boolean rollInOrder(Store store, long orderID) {
		long storeID = store.getID();
		
		try {
			storeManager = lookupStoreManager(storeID);
			storeManager.rollInReceivedOrder(storeID, orderID);
			return true;
		} catch (NotInDatabaseException_Exception | InvalidRollInRequestException_Exception | UpdateException_Exception e) {
			LOG.error(String.format("Error while rolling in order: %s\n%s", e.getMessage(), e.getStackTrace()));
		}
		return false;
	}
}
