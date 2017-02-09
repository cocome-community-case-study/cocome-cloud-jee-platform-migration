package org.cocome.cloud.web.connector.enterpriseconnector;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.xml.ws.WebServiceRef;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IEnterpriseManagerService;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.registry.service.Names;
import org.cocome.cloud.web.data.enterprisedata.EnterpriseViewData;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.StoreViewData;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;

/**
 * Implements the enterprise query interface to retrieve information from the
 * backend about available enterprises and stores.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Named
@ApplicationScoped
public class EnterpriseQuery implements IEnterpriseQuery {
	private static final Logger LOG = Logger.getLogger(EnterpriseQuery.class);

	private Map<Long, EnterpriseViewData> enterprises;
	private Map<Long, Collection<StoreViewData>> storeCollections;
	private Map<Long, StoreViewData> stores;

	IEnterpriseManager enterpriseManager;
	
	@Inject
	long defaultEnterpriseIndex;
	
	@Inject
	IApplicationHelper applicationHelper;
	
	private IEnterpriseManager lookupEnterpriseManager(long enterpriseID) throws NotInDatabaseException_Exception {
		try {
			return applicationHelper.getComponent(
					Names.getEnterpriseManagerRegistryName(enterpriseID), 
					IEnterpriseManagerService.SERVICE, 
					IEnterpriseManagerService.class).getIEnterpriseManagerPort();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
			if (enterpriseID == defaultEnterpriseIndex) {
			LOG.error("Got exception while retrieving enterprise manager location: " + e.getMessage());
			e.printStackTrace();
			throw new NotInDatabaseException_Exception(e.getMessage());
			} else {
				return lookupEnterpriseManager(defaultEnterpriseIndex);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cocome.cloud.shop.inventory.connection.IEnterpriseQuery#
	 * getEnterprises()
	 */
	@Override
	public Collection<EnterpriseViewData> getEnterprises() throws NotInDatabaseException_Exception {
		if (enterprises != null) {
			return enterprises.values();
		}

		updateEnterpriseInformation();
		return enterprises.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cocome.cloud.shop.inventory.connection.IEnterpriseQuery#getStores(
	 * long)
	 */
	@Override
	public Collection<StoreViewData> getStores(long enterpriseID) throws NotInDatabaseException_Exception {
		if (storeCollections == null) {
			updateStoreInformation();
		}

		Collection<StoreViewData> storeCollection = storeCollections.get(enterpriseID);
		return storeCollection != null ? storeCollection : new LinkedList<StoreViewData>();
	}

	@Override
	public void updateEnterpriseInformation() throws NotInDatabaseException_Exception {
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		this.enterprises = new HashMap<Long, EnterpriseViewData>();

		// TODO only update enterprises that are not already present
		for (EnterpriseTO enterprise : enterpriseManager.getEnterprises()) {
			enterprises.put(enterprise.getId(), new EnterpriseViewData(enterprise.getId(), enterprise.getName()));
		}
	}

	@Override
	public void updateStoreInformation() throws NotInDatabaseException_Exception {
		checkEnterprisesAvailable();

		// This collection has a fixed size depending on the number of
		// enterprises,
		// so we can set the initial capacity to this to avoid overhead
		storeCollections = new HashMap<Long, Collection<StoreViewData>>((int) (enterprises.size() / 0.75) + 1);

		this.stores = new HashMap<Long, StoreViewData>();
        
		for (EnterpriseViewData ent : enterprises.values()) {
			LinkedList<StoreViewData> stores = new LinkedList<StoreViewData>();
			enterpriseManager = lookupEnterpriseManager(ent.getId());
			for (StoreWithEnterpriseTO store : enterpriseManager.queryStoresByEnterpriseID(ent.getId())) {
				StoreViewData tempStore = new StoreViewData(store.getId(), store.getEnterpriseTO(), store.getLocation(),
						store.getName());
				this.stores.put(tempStore.getID(), tempStore);
				stores.add(tempStore);
			}
			this.storeCollections.put(ent.getId(), stores);
		}
	}

	@Override
	public EnterpriseViewData getEnterpriseByID(long enterpriseID) throws NotInDatabaseException_Exception {
		checkEnterprisesAvailable();

		return enterprises.get(enterpriseID);
	}

	@Override
	public StoreViewData getStoreByID(long storeID) throws NotInDatabaseException_Exception {
		LOG.debug("Retrieving store with id " + storeID);

		if (stores == null) {
			updateStoreInformation();
		}
		StoreViewData store = stores.get(storeID);

		if (store == null) {
			updateStoreInformation();
			store = stores.get(storeID);
		}
		
		if (store == null) {
			throw new NotInDatabaseException_Exception("Store not found!");
		}

		LOG.debug("Got store " + store.getName());
		return store;
	}

	public List<ProductWrapper> getAllProducts() throws NotInDatabaseException_Exception {
		// TODO should definitely be cached somehow, especially when there are
		// lots of products
		List<ProductWrapper> products = new LinkedList<ProductWrapper>();
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		for (ProductTO product : enterpriseManager.getAllProducts()) {
			ProductWrapper wrapper = new ProductWrapper(product);
			products.add(wrapper);
		}
		return products;
	}

	@Override
	public ProductWrapper getProductByID(long productID) throws NotInDatabaseException_Exception {
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		ProductWrapper product = new ProductWrapper(enterpriseManager.getProductByID(productID));
		return product;
	}

	@Override
	public ProductWrapper getProductByBarcode(long barcode) throws NotInDatabaseException_Exception {
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		ProductWrapper product = new ProductWrapper(enterpriseManager.getProductByBarcode(barcode));
		return product;
	}

	@Override
	public boolean updateStore(@NotNull StoreViewData store) throws NotInDatabaseException_Exception {
		StoreWithEnterpriseTO storeTO = new StoreWithEnterpriseTO();
		storeTO.setId(store.getID());
		storeTO.setLocation(store.getLocation());
		storeTO.setName(store.getName());
		storeTO.setEnterpriseTO(store.getEnterprise());
		enterpriseManager = lookupEnterpriseManager(store.getEnterprise().getId());
		try {
			enterpriseManager.updateStore(storeTO);
		} catch (NotInDatabaseException_Exception | UpdateException_Exception e) {
			LOG.error(String.format("Exception while updating store: %s\n%s", e.getMessage(), e.getStackTrace()));
			return false;
		}
		
		if (!checkStoresAvailable()) {
			// Error occured while retrieving the stores
			return false;
		}
		
		stores.put(store.getID(), store);
		return true;
	}

	@Override
	public boolean createEnterprise(@NotNull String name) throws NotInDatabaseException_Exception {
		checkEnterprisesAvailable();
		
		EnterpriseTO enterpriseTO;
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);

		try {
			enterpriseManager.createEnterprise(name);
			enterpriseTO = enterpriseManager.queryEnterpriseByName(name);
		} catch (CreateException_Exception | NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while creating enterprise: %s\n%s", e.getMessage(), e.getStackTrace()));
			return false;
		}
		
		enterprises.put(enterpriseTO.getId(), new EnterpriseViewData(enterpriseTO.getId(), enterpriseTO.getName()));

		return true;
	}

	@Override
	public boolean createProduct(@NotNull String name, long barcode, double purchasePrice) throws NotInDatabaseException_Exception {
		ProductTO product = new ProductTO();
		product.setBarcode(barcode);
		product.setName(name);
		product.setPurchasePrice(purchasePrice);
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		try {
			enterpriseManager.createProduct(product);
		} catch (CreateException_Exception e) {
			LOG.error(String.format("Exception while creating product: %s\n%s", e.getMessage(), e.getStackTrace()));
			return false;
		}

		return true;
	}

	@Override
	public boolean createStore(long enterpriseID, String name, String location) throws NotInDatabaseException_Exception {
		StoreWithEnterpriseTO storeTO = new StoreWithEnterpriseTO();
		storeTO.setEnterpriseTO(EnterpriseViewData.createEnterpriseTO(enterprises.get(enterpriseID)));
		storeTO.setLocation(location);
		storeTO.setName(name);

		Collection<StoreWithEnterpriseTO> storeTOs;
		enterpriseManager = lookupEnterpriseManager(enterpriseID);
		try {
			enterpriseManager.createStore(storeTO);
			storeTOs = enterpriseManager.queryStoreByName(enterpriseID, name);
		} catch (CreateException_Exception | NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while creating store: %s\n%s", e.getMessage(), e.getStackTrace()));
			return false;
		}

		if (!checkStoresAvailable()) {
			// Error occured while retrieving the stores
			return false;
		}
		
		for (StoreWithEnterpriseTO recStoreTO : storeTOs) {
			if (storeTO.getLocation().equals(location)) {
				StoreViewData recStore = new StoreViewData(recStoreTO.getId(), recStoreTO.getEnterpriseTO(), location, name);
				stores.put(recStoreTO.getId(),recStore);
				storeCollections.get(enterpriseID).add(recStore);
			}
		}

		return true;
	}

	private boolean checkStoresAvailable() {
		if (stores == null) {
			try {
				updateStoreInformation();
			} catch (NotInDatabaseException_Exception e) {
				return false;
			}
		}
		return true;
	}
	
	private void checkEnterprisesAvailable() throws NotInDatabaseException_Exception {
		if (enterprises == null) {
			updateEnterpriseInformation();
		}
	}
}
