package org.cocome.cloud.webservice.store;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.IStoreInventoryLocal;
import org.cocome.tradingsystem.inventory.application.store.IStoreInventoryManagerLocal;
import org.cocome.tradingsystem.inventory.application.store.InvalidRollInRequestException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.NotImplementedException;
import org.cocome.tradingsystem.inventory.application.store.ProductMovementTO;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierAndStockItemTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SaleTO;
import org.cocome.tradingsystem.inventory.application.store.StockItemTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQueryLocal;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContextLocal;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.store.IStoreQueryLocal;
import org.cocome.tradingsystem.inventory.data.store.StockItem;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.ICashDeskRegistryFactory;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

@WebService(endpointInterface = "org.cocome.cloud.webservice.store.IStoreManager")
@Stateless
public class StoreManager implements IStoreManager {
	@Inject
	IStoreInventoryManagerLocal storeManager;
	
	@Inject
	IStoreInventoryLocal storeInventory;
	
	@Inject
	ICashDeskRegistryFactory contextFactory;
	
	@Inject
	IStoreQueryLocal storeQuery;
	
	@Inject
	IEnterpriseQueryLocal enterpriseQuery;
	
	@Inject
	IPersistenceContextLocal persistenceContext;
	
	private static final Logger LOG = Logger.getLogger(StoreManager.class);
	
	private void setContextRegistry(long storeID) throws NotInDatabaseException {
		LOG.debug("Setting store to store with id " + storeID);
		Store store = storeQuery.queryStoreById(storeID);
		long enterpriseID = store.getEnterprise().getId();
		
		IContextRegistry registry = new CashDeskRegistry("store#" + storeID);
		registry.putLong(RegistryKeys.STORE_ID, storeID);
		registry.putLong(RegistryKeys.ENTERPRISE_ID, enterpriseID);
		
		contextFactory.setStoreContext(registry);
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#accountSale(long, org.cocome.tradingsystem.inventory.application.store.SaleTO)
	 */
	@Override
	public void accountSale(long storeID, SaleTO sale) 
			throws ProductOutOfStockException, NotInDatabaseException, UpdateException {
		setContextRegistry(storeID);
		storeManager.accountSale(storeID, sale);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#getStore(long)
	 */
	@Override
	public StoreWithEnterpriseTO getStore(long storeID) throws NotInDatabaseException {
		setContextRegistry(storeID);
		return storeManager.getStore(storeID);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#getProductsWithLowStock(long)
	 */
	@Override
	public List<ProductWithStockItemTO> getProductsWithLowStock(long storeID) 
			throws NotInDatabaseException {
		setContextRegistry(storeID);
		return storeManager.getProductsWithLowStock(storeID);

	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#getAllProducts(long)
	 */
	@Override
	public List<ProductWithSupplierTO> getAllStoreProducts(long storeID) 
			throws NotInDatabaseException {
		setContextRegistry(storeID);
		return storeManager.getAllProducts(storeID);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#getProductsWithStockItems(long)
	 */
	@Override
	public List<ProductWithSupplierAndStockItemTO> getProductsWithStockItems(long storeID) 
			throws NotInDatabaseException {
		setContextRegistry(storeID);
		return storeManager.getProductsWithStockItems(storeID);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#orderProducts(long, org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO)
	 */
	@Override
	public List<ComplexOrderTO> orderProducts(long storeID, ComplexOrderTO complexOrder) 
			throws NotInDatabaseException, CreateException, UpdateException {
		setContextRegistry(storeID);
		return storeManager.orderProducts(storeID, complexOrder);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#getOrder(long, long)
	 */
	@Override
	public ComplexOrderTO getOrder(long storeID, long orderId) 
			throws NotInDatabaseException {
		setContextRegistry(storeID);
		return storeManager.getOrder(storeID, orderId);

	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#getOutstandingOrders(long)
	 */
	@Override
	public List<ComplexOrderTO> getOutstandingOrders(long storeID) 
			throws NotInDatabaseException {
		setContextRegistry(storeID);
		return storeManager.getOutstandingOrders(storeID);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#rollInReceivedOrder(long, long)
	 */
	@Override
	public void rollInReceivedOrder(long storeID, long orderId)
			throws InvalidRollInRequestException, NotInDatabaseException, UpdateException {
		setContextRegistry(storeID);
		storeManager.rollInReceivedOrder(storeID, orderId);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#changePrice(long, org.cocome.tradingsystem.inventory.application.store.StockItemTO)
	 */
	@Override
	public ProductWithStockItemTO changePrice(long storeID, StockItemTO stockItemTO) 
			throws NotInDatabaseException, UpdateException {
		LOG.debug("Changing price from stockItem " + stockItemTO.getId() + " to " + stockItemTO.getSalesPrice());
		setContextRegistry(storeID);
		return storeManager.changePrice(storeID, stockItemTO);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#markProductsUnavailableInStock(long, org.cocome.tradingsystem.inventory.application.store.ProductMovementTO)
	 */
	@Override
	public void markProductsUnavailableInStock(long storeID, ProductMovementTO movedProductAmounts)
			throws ProductOutOfStockException, NotInDatabaseException, UpdateException {
		setContextRegistry(storeID);
		storeManager.markProductsUnavailableInStock(storeID, movedProductAmounts);


	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#getStockItems(long, org.cocome.tradingsystem.inventory.application.store.ProductTO[])
	 */
	@Override
	public ComplexOrderEntryTO[] getStockItems(long storeID, ProductTO[] requiredProductTOs) 
			throws NotInDatabaseException, NotImplementedException {
		setContextRegistry(storeID);
		return storeManager.getStockItems(storeID, requiredProductTOs);

	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.webservice.store.IStoreManager#getProductWithStockItem(long, long)
	 */
	@Override
	public ProductWithStockItemTO getProductWithStockItem(long storeID, long productBarcode) 
			throws NoSuchProductException, NotInDatabaseException {
		setContextRegistry(storeID);
		return storeInventory.getProductWithStockItem(storeID, productBarcode);
	}

	@Override
	public void updateStockItem(long storeID, StockItemTO stockItemTO) throws NotInDatabaseException, UpdateException {
		setContextRegistry(storeID);
		storeManager.updateStockItem(storeID, stockItemTO);
	}

	@Override
	public void createStockItem(long storeID, ProductWithStockItemTO stockItemTO)
			throws NotInDatabaseException, CreateException {
		setContextRegistry(storeID);
		
		Store store = storeQuery.queryStoreById(storeID);
		
		StockItem item = new StockItem();
		item.setAmount(stockItemTO.getStockItemTO().getAmount());
		item.setIncomingAmount(stockItemTO.getStockItemTO().getIncomingAmount());
		item.setMaxStock(stockItemTO.getStockItemTO().getMaxStock());
		item.setMinStock(stockItemTO.getStockItemTO().getMinStock());
		item.setSalesPrice(stockItemTO.getStockItemTO().getSalesPrice());
		
		Product product;
		if (stockItemTO.getId() != 0) {
			product = enterpriseQuery.queryProductByID(stockItemTO.getId());
		} else {
			product = enterpriseQuery.queryProductByBarcode(stockItemTO.getBarcode());
		}
		
		item.setProduct(product);
		item.setProductBarcode(product.getBarcode());
		item.setStore(store);
		item.setStoreLocation(store.getLocation());
		item.setStoreName(store.getName());
		
		persistenceContext.createEntity(item);
	}

}
