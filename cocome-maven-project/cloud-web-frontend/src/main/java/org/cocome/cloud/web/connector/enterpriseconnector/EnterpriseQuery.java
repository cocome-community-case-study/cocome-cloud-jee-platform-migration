package org.cocome.cloud.web.connector.enterpriseconnector;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.xml.ws.WebServiceRef;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IEnterpriseManagerService;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.stub.UpdateException_Exception;
import org.cocome.cloud.web.data.enterprisedata.Enterprise;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.data.storedata.Store;
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

	private Map<Long, Enterprise> enterprises;
	private Map<Long, Collection<Store>> storeCollections;
	private Map<Long, Store> stores;

	@WebServiceRef(IEnterpriseManagerService.class)
	IEnterpriseManager enterpriseManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cocome.cloud.shop.inventory.connection.IEnterpriseQuery#
	 * getEnterprises()
	 */
	@Override
	public Collection<Enterprise> getEnterprises() {
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
	public Collection<Store> getStores(long enterpriseID) throws NotInDatabaseException_Exception {
		if (storeCollections == null) {
			updateStoreInformation();
		}

		Collection<Store> storeCollection = storeCollections.get(enterpriseID);
		return storeCollection != null ? storeCollection : new LinkedList<Store>();
	}

	@Override
	public void updateEnterpriseInformation() {
		this.enterprises = new HashMap<Long, Enterprise>();

		// TODO only update enterprises that are not already present
		for (EnterpriseTO enterprise : enterpriseManager.getEnterprises()) {
			enterprises.put(enterprise.getId(), new Enterprise(enterprise.getId(), enterprise.getName()));
		}
	}

	@Override
	public void updateStoreInformation() throws NotInDatabaseException_Exception {
		if (this.enterprises == null) {
			updateEnterpriseInformation();
		}

		// This collection has a fixed size depending on the number of
		// enterprises,
		// so we can set the initial capacity to this to avoid overhead
		storeCollections = new HashMap<Long, Collection<Store>>((int) (enterprises.size() / 0.75) + 1);

		this.stores = new HashMap<Long, Store>();

		for (Enterprise ent : enterprises.values()) {
			LinkedList<Store> stores = new LinkedList<Store>();

			for (StoreWithEnterpriseTO store : enterpriseManager.queryStoresByEnterpriseID(ent.getId())) {
				Store tempStore = new Store(store.getId(), store.getEnterpriseTO(), store.getLocation(),
						store.getName());
				this.stores.put(tempStore.getID(), tempStore);
				stores.add(tempStore);
			}
			this.storeCollections.put(ent.getId(), stores);
		}
	}

	@Override
	public Enterprise getEnterpriseByID(long enterpriseID) {
		if (enterprises == null) {
			updateEnterpriseInformation();
		}

		return enterprises.get(enterpriseID);
	}

	@Override
	public Store getStoreByID(long storeID) throws NotInDatabaseException_Exception {
		LOG.debug("Retrieving store with id " + storeID);

		if (stores == null) {
			updateStoreInformation();
		}
		Store store = stores.get(storeID);

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

	public List<ProductWrapper> getAllProducts() {
		// TODO should definitely be cached somehow, especially when there are
		// lots of products
		List<ProductWrapper> products = new LinkedList<ProductWrapper>();
		for (ProductTO product : enterpriseManager.getAllProducts()) {
			ProductWrapper wrapper = new ProductWrapper(product);
			products.add(wrapper);
		}
		return products;
	}

	@Override
	public ProductWrapper getProductByID(long productID) throws NotInDatabaseException_Exception {
		ProductWrapper product = new ProductWrapper(enterpriseManager.getProductByID(productID));
		return product;
	}

	@Override
	public ProductWrapper getProductByBarcode(long barcode) throws NotInDatabaseException_Exception {
		ProductWrapper product = new ProductWrapper(enterpriseManager.getProductByBarcode(barcode));
		return product;
	}

	@Override
	public boolean updateStore(@NotNull Store store) {
		StoreWithEnterpriseTO storeTO = new StoreWithEnterpriseTO();
		storeTO.setId(store.getID());
		storeTO.setLocation(store.getLocation());
		storeTO.setName(store.getName());
		storeTO.setEnterpriseTO(store.getEnterprise());

		try {
			enterpriseManager.updateStore(storeTO);
		} catch (NotInDatabaseException_Exception | UpdateException_Exception e) {
			LOG.error(String.format("Exception while updating store: %s\n%s", e.getMessage(), e.getStackTrace()));
			return false;
		}

		stores.put(store.getID(), store);
		return true;
	}

	@Override
	public boolean createEnterprise(@NotNull String name) {
		EnterpriseTO enterpriseTO;

		try {
			enterpriseManager.createEnterprise(name);
			enterpriseTO = enterpriseManager.queryEnterpriseByName(name);
		} catch (CreateException_Exception | NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while creating enterprise: %s\n%s", e.getMessage(), e.getStackTrace()));
			return false;
		}
		enterprises.put(enterpriseTO.getId(), new Enterprise(enterpriseTO.getId(), enterpriseTO.getName()));

		return true;
	}

	@Override
	public boolean createProduct(@NotNull String name, long barcode, double purchasePrice) {
		ProductTO product = new ProductTO();
		product.setBarcode(barcode);
		product.setName(name);
		product.setPurchasePrice(purchasePrice);

		try {
			enterpriseManager.createProduct(product);
		} catch (CreateException_Exception e) {
			LOG.error(String.format("Exception while creating product: %s\n%s", e.getMessage(), e.getStackTrace()));
			return false;
		}

		return true;
	}

	@Override
	public boolean createStore(long enterpriseID, String name, String location) {
		StoreWithEnterpriseTO storeTO = new StoreWithEnterpriseTO();
		storeTO.setEnterpriseTO(Enterprise.createEnterpriseTO(enterprises.get(enterpriseID)));
		storeTO.setLocation(location);
		storeTO.setName(name);

		Collection<StoreWithEnterpriseTO> storeTOs;

		try {
			enterpriseManager.createStore(storeTO);
			storeTOs = enterpriseManager.queryStoreByName(enterpriseID, name);
		} catch (CreateException_Exception | NotInDatabaseException_Exception e) {
			LOG.error(String.format("Exception while creating store: %s\n%s", e.getMessage(), e.getStackTrace()));
			return false;
		}

		for (StoreWithEnterpriseTO recStoreTO : storeTOs) {
			if (storeTO.getLocation().equals(location)) {
				Store recStore = new Store(recStoreTO.getId(), recStoreTO.getEnterpriseTO(), location, name);
				stores.put(recStoreTO.getId(),recStore);
				storeCollections.get(enterpriseID).add(recStore);
			}
		}

		return true;
	}
}
