package org.cocome.cloud.webservice.enterprise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.FillTransferObjects;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQueryLocal;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.generator.ItemizedDatabaseFiller;
import org.cocome.tradingsystem.inventory.data.generator.StorizedDatabaseFiller;
import org.cocome.tradingsystem.inventory.data.generator.TestDatabaseFiller;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContextLocal;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.ICashDeskRegistryFactory;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

@WebService(endpointInterface = "org.cocome.cloud.webservice.enterprise.IEnterpriseManager")
@Stateless
public class EnterpriseManager implements IEnterpriseManager {
	
	private static final Logger LOG = Logger.getLogger(EnterpriseManager.class);
	
	@Inject
	IEnterpriseQueryLocal enterpriseQuery;
	
	@Inject
	IPersistenceContextLocal persistenceContext;
	
	@Inject
	ICashDeskRegistryFactory registryFact;
	
	@EJB
	StorizedDatabaseFiller storizedFiller;
	
	@EJB
	ItemizedDatabaseFiller itemizedFiller;
	
	@EJB
	TestDatabaseFiller testFiller;
	
	private void setContextRegistry(long enterpriseID) {
		IContextRegistry registry = new CashDeskRegistry("enterprise#" + enterpriseID);
		registry.putLong(RegistryKeys.ENTERPRISE_ID, enterpriseID);
		registryFact.setEnterpriseContext(registry);
	}
	
	@Override
	public EnterpriseTO queryEnterpriseById(long enterpriseId) throws NotInDatabaseException {
		setContextRegistry(enterpriseId);
		TradingEnterprise enterprise;
		try {
			enterprise = enterpriseQuery
					.queryEnterpriseById(enterpriseId);
		} catch (NotInDatabaseException e) {
			LOG.error("Got NotInDatabaseException for enterprise: " + e);
			e.printStackTrace();
			throw e;
		}
		EnterpriseTO enterpriseTO = FillTransferObjects
				.fillEnterpriseTO(enterprise);
		return enterpriseTO;
	}

	@Override
	public long getMeanTimeToDelivery(SupplierTO supplierTO, EnterpriseTO enterpriseTO) throws NotInDatabaseException {
		setContextRegistry(enterpriseTO.getId());
		
		ProductSupplier supplier;
		TradingEnterprise enterprise;
		try {
			supplier = enterpriseQuery.querySupplierByID(supplierTO.getId());
			enterprise = enterpriseQuery.queryEnterpriseById(enterpriseTO.getId());
		} catch (NotInDatabaseException e) {
			LOG.error("Got NotInDatabaseException: " + e);
			e.printStackTrace();
			throw e;
		}
		
		return enterpriseQuery.getMeanTimeToDelivery(supplier, enterprise);
	}

	@Override
	public Collection<ProductTO> getAllEnterpriseProducts(long enterpriseId) throws NotInDatabaseException {
		setContextRegistry(enterpriseId);
		
		Collection<Product> products;
		try {
			products = enterpriseQuery.queryAllProducts(enterpriseId);
		} catch (NotInDatabaseException e) {
			LOG.error("Got NotInDatabaseException: " + e);
			e.printStackTrace();
			throw e;
		}
		
		Collection<ProductTO> productTOs = new ArrayList<ProductTO>(products.size());
		
		for(Product product : products) {
			productTOs.add(FillTransferObjects.fillProductTO(product));
		}
		
		return productTOs;
	}

	@Override
	public void createEnterprise(String enterpriseName) throws CreateException {
		TradingEnterprise enterprise = new TradingEnterprise();
		enterprise.setName(enterpriseName);
		try {
			persistenceContext.createEntity(enterprise);
		} catch (CreateException e) {
			LOG.error("Got CreateException: " + e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void createStore(StoreWithEnterpriseTO storeTO) throws CreateException {
		Store store = new Store();
		store.setEnterpriseName(storeTO.getEnterpriseTO().getName());
		store.setLocation(storeTO.getLocation());
		store.setName(storeTO.getName());
		try {
			persistenceContext.createEntity(store);
		} catch (CreateException e) {
			LOG.error("Got CreateException: " + e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Collection<EnterpriseTO> getEnterprises() {
		Collection<TradingEnterprise> enterprises = enterpriseQuery.queryAllEnterprises();
		Collection<EnterpriseTO> enterpriseTOs = new ArrayList<EnterpriseTO>(enterprises.size());
		for(TradingEnterprise enterprise : enterprises) {
			enterpriseTOs.add(FillTransferObjects.fillEnterpriseTO(enterprise));
		}
		return enterpriseTOs;
	}

	@Override
	public Collection<StoreWithEnterpriseTO> queryStoresByEnterpriseID(long enterpriseId) 
			throws NotInDatabaseException {
		Collection<Store> stores = enterpriseQuery.queryStoresByEnterpriseId(enterpriseId);
		Collection<StoreWithEnterpriseTO> storeTOs = new ArrayList<StoreWithEnterpriseTO>(stores.size());
		for(Store store : stores) {
			try {
				storeTOs.add(FillTransferObjects.fillStoreWithEnterpriseTO(store));
			} catch (NotInDatabaseException e) {
				LOG.error("Got NotInDatabaseException: " + e);
				e.printStackTrace();
				throw e;
			}
		}
		return storeTOs;
	}

	@Override
	public void updateStore(StoreWithEnterpriseTO storeTO) 
			throws NotInDatabaseException, UpdateException {
		Store store;
		try {
			store = enterpriseQuery.queryStoreByEnterprise(
					storeTO.getEnterpriseTO().getId(), storeTO.getId());
		} catch (NotInDatabaseException e) {
			LOG.error("Got NotInDatabaseException: " + e);
			e.printStackTrace();
			throw e;
		}
		
		TradingEnterprise enterprise;
		try {
			enterprise = enterpriseQuery.queryEnterpriseById(
					storeTO.getEnterpriseTO().getId());
		} catch (NotInDatabaseException e) {
			LOG.error("Got NotInDatabaseException: " + e);
			e.printStackTrace();
			throw e;
		}
		
		store.setEnterprise(enterprise);
		store.setEnterpriseName(enterprise.getName());
		store.setLocation(storeTO.getLocation());
		store.setName(storeTO.getName());
		
		try {
			persistenceContext.updateEntity(store);
		} catch (UpdateException e) {
			LOG.error("Got UpdateException: " + e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void createProduct(ProductTO productTO) 
			throws CreateException {		
		Product product = new Product();
		product.setBarcode(productTO.getBarcode());
		product.setName(productTO.getName());
		product.setPurchasePrice(productTO.getPurchasePrice());
				
		try {
			persistenceContext.createEntity(product);
		} catch (CreateException e) {
			LOG.error("Got CreateException: " + e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateProduct(ProductWithSupplierTO productTO) 
			throws NotInDatabaseException, UpdateException {
		Product product;
		try {
			if (productTO.getId() != 0) {
				product = enterpriseQuery.queryProductByID(productTO.getId());
				product.setBarcode(productTO.getBarcode());
			} else {
				product = enterpriseQuery.queryProductByBarcode(productTO
						.getBarcode());
			}
		} catch (NotInDatabaseException e) {
			LOG.error("Got NotInDatabaseException: " + e);
			e.printStackTrace();
			throw e;
		}
		
		product.setName(productTO.getName());
		product.setPurchasePrice(productTO.getPurchasePrice());
		
		if (productTO.getSupplierTO().getId() != 0) {
			ProductSupplier supplier;
			try {
				supplier = enterpriseQuery.querySupplierByID(productTO.getSupplierTO().getId());
			} catch (NotInDatabaseException e) {
				LOG.error("Got NotInDatabaseException: " + e);
				e.printStackTrace();
				throw e;
			}
			product.setSupplier(supplier);
		}
		
		try {
			persistenceContext.updateEntity(product);
		} catch (UpdateException e) {
			LOG.error("Got UpdateException: " + e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Collection<ProductTO> getAllProducts() {
		Collection<Product> products = enterpriseQuery.queryAllProducts(); 
		Collection<ProductTO> productTOs = new ArrayList<ProductTO>(products.size());
		
		for (Product product : products) {
			productTOs.add(FillTransferObjects.fillProductTO(product));
		}
		return productTOs;
	}

	@Override
	public void fillTestDatabase(int storeCount, int cashDesksPerStore, String dirString) 
			throws NotInDatabaseException, CreateException, IOException {
		try {
			testFiller.fillDatabase(storeCount, cashDesksPerStore, dirString);
		} catch (NotInDatabaseException | CreateException | IOException e) {
			LOG.error("Got an exception while filling the test database: " + e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void fillItemizedDatabase(int stockItemCount, int cashDesksPerStore, String dirString) 
			throws NotInDatabaseException, CreateException, IOException {
		try {
			itemizedFiller.fillDatabase(stockItemCount, cashDesksPerStore, dirString);
		} catch (NotInDatabaseException | CreateException | IOException e) {
			LOG.error("Got an exception while filling the itemized database: " + e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void fillStorizedDatabase(int storeCount, int cashDesksPerStore, String dirString) 
			throws NotInDatabaseException, CreateException, IOException {
		try {
			storizedFiller.fillDatabase(storeCount, cashDesksPerStore,
					dirString);
		} catch (NotInDatabaseException | CreateException | IOException e) {
			LOG.error("Got an exception while filling the itemized database: "
					+ e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public ProductTO getProductByID(long productID) throws NotInDatabaseException {
		Product product = enterpriseQuery.queryProductByID(productID);
		return FillTransferObjects.fillProductTO(product);
	}
	
	@Override
	public ProductTO getProductByBarcode(long barcode) throws NotInDatabaseException {
		Product product = enterpriseQuery.queryProductByBarcode(barcode);
		return FillTransferObjects.fillProductTO(product);
	}

}
