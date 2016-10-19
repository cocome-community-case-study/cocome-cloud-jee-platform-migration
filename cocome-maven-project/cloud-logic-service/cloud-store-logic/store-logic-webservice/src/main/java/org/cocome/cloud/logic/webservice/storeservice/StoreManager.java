package org.cocome.cloud.logic.webservice.storeservice;

import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.registry.service.Names;
import org.cocome.logic.webservice.storeservice.IStoreManager;
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
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.inventory.data.store.IStockItem;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.ICashDeskRegistryFactory;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

@WebService(serviceName = "IStoreManagerService", 
			name = "IStoreManager", 
			endpointInterface = "org.cocome.logic.webservice.storeservice.IStoreManager",
			targetNamespace = "http://store.webservice.logic.cocome.org/")
@Stateless
public class StoreManager implements IStoreManager {
	@Inject
	IStoreInventoryManagerLocal storeManager;
	
	@Inject
	IStoreInventoryLocal storeInventory;
	
	@Inject
	ICashDeskRegistryFactory contextFactory;
	
	@Inject
	IStoreQuery storeQuery;
	
	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	@Inject
	IPersistenceContext persistenceContext;
	
	@Inject
	IApplicationHelper applicationHelper;
	
	@Inject
	IStoreDataFactory storeFactory;
	
	@Inject
	String storeManagerWSDL;
	
	@Inject
	long defaultStoreIndex;
	
	private static final Logger LOG = Logger.getLogger(StoreManager.class);
	
	private void setContextRegistry(long storeID) throws NotInDatabaseException {
		LOG.debug("Setting store to store with id " + storeID);
		IStore store = storeQuery.queryStoreById(storeID);
		long enterpriseID = store.getEnterprise().getId();
		
		IContextRegistry registry = new CashDeskRegistry("store#" + storeID);
		registry.putLong(RegistryKeys.STORE_ID, storeID);
		registry.putLong(RegistryKeys.ENTERPRISE_ID, enterpriseID);
		
		contextFactory.setStoreContext(registry);
		
		try {
			applicationHelper.registerComponent(Names.getStoreManagerRegistryName(defaultStoreIndex), storeManagerWSDL, false);
			applicationHelper.registerComponent(Names.getStoreManagerRegistryName(storeID), storeManagerWSDL, false);
		} catch (URISyntaxException e) {
			LOG.error("Error registering component: " + e.getMessage());
		}
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
		storeManager.createStockItem(storeID, stockItemTO);
	}

}
