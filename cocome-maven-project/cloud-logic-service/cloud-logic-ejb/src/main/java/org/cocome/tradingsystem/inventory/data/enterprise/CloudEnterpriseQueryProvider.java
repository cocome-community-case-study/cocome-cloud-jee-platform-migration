package org.cocome.tradingsystem.inventory.data.enterprise;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.store.IStoreQueryLocal;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.connection.GetXMLFromBackend;
import org.cocome.tradingsystem.remote.access.parsing.CSVHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

@Stateless
public class CloudEnterpriseQueryProvider implements IEnterpriseQueryLocal {
	
	private static final Logger LOG = Logger.getLogger(CloudEnterpriseQueryProvider.class);
	
	@Inject
	GetXMLFromBackend backendConnection;
	
	@Inject
	IStoreQueryLocal storeQuery;
	
	@Inject
	CSVHelper csvHelper;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TradingEnterprise queryEnterpriseById(long enterpriseID) throws NotInDatabaseException {
		LOG.debug("Trying to retrieve enterprise with id " + enterpriseID);
		try {
			String backendMessage = backendConnection.getEnterprises("id==" + enterpriseID);
			LOG.debug("Backend response: " + backendMessage);
			TradingEnterprise enterprise = csvHelper.getEnterprisesFromCSV(
					backendMessage).iterator().next();
			return enterprise;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException(
					"Enterprise with ID " + enterpriseID + " could not be found!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMeanTimeToDelivery(ProductSupplier supplier,
			TradingEnterprise enterprise) {
		long mttd = 0;
		try {
		/*
		mttd = (long) database.query("SELECT productorder FROM ProductOrder AS productorder " +
						"WHERE productorder.deliveryDate IS NOT NULL " +
						"AND EXISTS (" +
						"SELECT orderentry FROM OrderEntry AS orderentry " +
						"WHERE orderentry.order = productorder " +
						"AND EXISTS (" +
						"SELECT product FROM Product AS product " +
						"JOIN product.supplier s WHERE s.id = " + supplier.getId() +
						" AND orderentry.product = product" +
						")" +
						") AND EXISTS (" +
						"SELECT store FROM Store AS store " +
						"JOIN store.enterprise e WHERE productorder.store = store " +
						"AND e.id = " +
						enterprise.getId() +
						")").get(0);*/
			// TODO compute this here...
			LOG.error("Tried to obtain mean time to delivery, but this is not available at the moment!");
			
		} catch (NoSuchElementException e) {
			// Means the query returned nothing, so don't crash and return 0 as mttd
		}
		return mttd;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Product> queryAllProducts(long enterpriseID) {
		// Because the backend doesn't allow joins, it is neccessary to first get the stores
		Collection<Store> stores = queryStoresByEnterpriseId(enterpriseID);
		Collection<Product> products = new LinkedList<Product>();
		
		for (Store store : stores) {
			products.addAll(storeQuery.queryProducts(store.getId()));
		}

		return products;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<ProductSupplier> querySuppliers(long enterpriseID) throws NotInDatabaseException {
		TradingEnterprise enterprise = queryEnterpriseById(enterpriseID);
		
		return enterprise.getSuppliers();
	}

	@Override
	public TradingEnterprise queryEnterpriseByName(String enterpriseName)
			throws NotInDatabaseException {
		try {
			TradingEnterprise enterprise = csvHelper.getEnterprisesFromCSV(
					backendConnection.getEnterprises("name=LIKE'" + enterpriseName + "'")).iterator().next();
			return enterprise;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException(
					"No enterprise with name '" + enterpriseName + "' was found!");
		}
	}

	@Override
	public ProductSupplier querySupplierForProduct(long enterpriseID,
			long productBarcode) throws NotInDatabaseException {
		TradingEnterprise enterprise = queryEnterpriseById(enterpriseID);
		
		// Probably inefficient but not possible otherwise due to serviceadapter limitations
		for (ProductSupplier supplier : enterprise.getSuppliers()) {
			for (Product product : supplier.getProducts()) {
				if (product.getBarcode() == productBarcode) {
					return supplier;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<Product> queryProductsBySupplier(long enterpriseID,
			long productSupplierID) throws NotInDatabaseException {
		TradingEnterprise enterprise = queryEnterpriseById(enterpriseID);
		
		for (ProductSupplier supplier : enterprise.getSuppliers()) {
			if (supplier.getId() == productSupplierID) {
				return csvHelper.getProductsFromCSV(
						backendConnection.getProducts("supplier.id==" + supplier.getId()));
			}
		}
		
		return Collections.emptyList();
	}

	@Override
	public Collection<Store> queryStoresByEnterpriseId(long enterpriseID) {
		Collection<Store> stores = csvHelper.getStoresFromCSV(
				backendConnection.getStores("enterprise.id==" + enterpriseID));
		return stores;
	}

	@Override
	public Collection<TradingEnterprise> queryAllEnterprises() {
		Collection<TradingEnterprise> enterprises = csvHelper.getEnterprisesFromCSV(
				backendConnection.getEnterprises("name=LIKE'*'"));
				
		return enterprises;
	}

	@Override
	public Store queryStoreByEnterprise(long enterpriseID, long storeID)
			throws NotInDatabaseException {
		try {
			Store store = csvHelper.getStoresFromCSV(
					backendConnection.getStores("id==" + storeID 
							+ ";Store.enterprise.id==" + enterpriseID)).iterator().next();
			return store;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No matching store found in database!");
		}
	}

	@Override
	public ProductSupplier querySupplierByID(long supplierID)
			throws NotInDatabaseException {
		try {
			ProductSupplier supplier = csvHelper.getProductSuppliersFromCSV(
					backendConnection.getProductSupplier("id==" + supplierID)).iterator().next();
			return supplier;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No matching supplier found in database!");
		}
	}

	@Override
	public Product queryProductByID(long productID)
			throws NotInDatabaseException {
		try {
			Product product = csvHelper.getProductsFromCSV(
					backendConnection.getProducts("id==" + productID)).iterator().next();
			return product;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No matching product found in database!");
		}
	}

	@Override
	public Product queryProductByBarcode(long productBarcode)
			throws NotInDatabaseException {
		try {
			Product product = csvHelper.getProductsFromCSV(
					backendConnection.getProducts("barcode==" + productBarcode)).iterator().next();
			return product;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No matching product found in database!");
		}
	}

	@Override
	public Collection<Product> queryAllProducts() {
		Collection<Product> products = csvHelper.getProductsFromCSV(
				backendConnection.getProducts("name=LIKE'*'"));
				
		return products;
	}

}
