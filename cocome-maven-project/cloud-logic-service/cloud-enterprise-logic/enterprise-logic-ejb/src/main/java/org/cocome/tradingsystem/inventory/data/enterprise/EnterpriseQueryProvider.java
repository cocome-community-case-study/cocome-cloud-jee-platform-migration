package org.cocome.tradingsystem.inventory.data.enterprise;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.connection.QueryParameterEncoder;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

@Stateless
@Local(IEnterpriseQuery.class)
public class EnterpriseQueryProvider implements IEnterpriseQuery {

	private static final Logger LOG = Logger.getLogger(EnterpriseQueryProvider.class);

	@Inject
	IBackendQuery backendConnection;

	@Inject
	IStoreQuery storeQuery;

	@Inject
	IBackendConversionHelper csvHelper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITradingEnterprise queryEnterpriseById(long enterpriseID) throws NotInDatabaseException {
		LOG.debug("Trying to retrieve enterprise with id " + enterpriseID);
		try {
			String backendMessage = backendConnection.getEnterprises("id==" + enterpriseID);
			LOG.debug("Backend response: " + backendMessage);
			ITradingEnterprise enterprise = csvHelper.getEnterprises(backendMessage).iterator().next();
			return enterprise;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("Enterprise with ID " + enterpriseID + " could not be found!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMeanTimeToDelivery(IProductSupplier supplier, ITradingEnterprise enterprise) {
		long mttd = 0;
		try {
			/*
			 * mttd = (long) database.query(
			 * "SELECT productorder FROM ProductOrder AS productorder " +
			 * "WHERE productorder.deliveryDate IS NOT NULL " + "AND EXISTS (" +
			 * "SELECT orderentry FROM OrderEntry AS orderentry " +
			 * "WHERE orderentry.order = productorder " + "AND EXISTS (" +
			 * "SELECT product FROM Product AS product " +
			 * "JOIN product.supplier s WHERE s.id = " + supplier.getId() +
			 * " AND orderentry.product = product" + ")" + ") AND EXISTS (" +
			 * "SELECT store FROM Store AS store " +
			 * "JOIN store.enterprise e WHERE productorder.store = store " +
			 * "AND e.id = " + enterprise.getId() + ")").get(0);
			 */
			// TODO compute this here...
			LOG.error("Tried to obtain mean time to delivery, but this is not available at the moment!");

		} catch (NoSuchElementException e) {
			// Means the query returned nothing, so don't crash and return 0 as
			// mttd
		}
		return mttd;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<IProduct> queryAllProducts(long enterpriseID) {
		// Because the backend doesn't allow joins, it is neccessary to first
		// get the stores
		Collection<IStore> stores = queryStoresByEnterpriseId(enterpriseID);
		Collection<IProduct> products = new LinkedList<IProduct>();

		for (IStore store : stores) {
			products.addAll(storeQuery.queryProducts(store.getId()));
		}

		return products;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<IProductSupplier> querySuppliers(long enterpriseID) throws NotInDatabaseException {
		ITradingEnterprise enterprise = queryEnterpriseById(enterpriseID);

		return enterprise.getSuppliers();
	}

	@Override
	public ITradingEnterprise queryEnterpriseByName(String enterpriseName) throws NotInDatabaseException {
		enterpriseName = QueryParameterEncoder.encodeQueryString(enterpriseName);
		
		try {
			ITradingEnterprise enterprise = csvHelper
					.getEnterprises(backendConnection.getEnterprises("name=LIKE%20'" + enterpriseName + "'")).iterator()
					.next();
			return enterprise;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No enterprise with name '" + enterpriseName + "' was found!");
		}
	}

	@Override
	public IProductSupplier querySupplierForProduct(long enterpriseID, long productBarcode)
			throws NotInDatabaseException {
		ITradingEnterprise enterprise = queryEnterpriseById(enterpriseID);

		// Probably inefficient but not possible otherwise due to serviceadapter
		// limitations
		for (IProductSupplier supplier : enterprise.getSuppliers()) {
			for (IProduct product : supplier.getProducts()) {
				if (product.getBarcode() == productBarcode) {
					return supplier;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<IProduct> queryProductsBySupplier(long enterpriseID, long productSupplierID)
			throws NotInDatabaseException {
		ITradingEnterprise enterprise = queryEnterpriseById(enterpriseID);

		for (IProductSupplier supplier : enterprise.getSuppliers()) {
			if (supplier.getId() == productSupplierID) {
				return csvHelper.getProducts(backendConnection.getProducts("supplier.id==" + supplier.getId()));
			}
		}

		return Collections.emptyList();
	}

	@Override
	public Collection<IStore> queryStoresByEnterpriseId(long enterpriseID) {
		Collection<IStore> stores = csvHelper.getStores(backendConnection.getStores("enterprise.id==" + enterpriseID));
		return stores;
	}

	@Override
	public Collection<ITradingEnterprise> queryAllEnterprises() {
		Collection<ITradingEnterprise> enterprises = csvHelper
				.getEnterprises(backendConnection.getEnterprises("name=LIKE%20'*'"));
		return enterprises;
	}

	@Override
	public IStore queryStoreByEnterprise(long enterpriseID, long storeID) throws NotInDatabaseException {
		try {
			IStore store = csvHelper
					.getStores(backendConnection.getStores("id==" + storeID + ";Store.enterprise.id==" + enterpriseID))
					.iterator().next();
			return store;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No matching store found in database!");
		}
	}

	@Override
	public IProductSupplier querySupplierByID(long supplierID) throws NotInDatabaseException {
		try {
			IProductSupplier supplier = csvHelper
					.getProductSuppliers(backendConnection.getProductSupplier("id==" + supplierID)).iterator().next();
			return supplier;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No matching supplier found in database!");
		}
	}

	@Override
	public IProduct queryProductByID(long productID) throws NotInDatabaseException {
		try {
			IProduct product = csvHelper.getProducts(backendConnection.getProducts("id==" + productID)).iterator()
					.next();
			return product;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No matching product found in database!");
		}
	}

	@Override
	public IProduct queryProductByBarcode(long productBarcode) throws NotInDatabaseException {
		try {
			IProduct product = csvHelper.getProducts(backendConnection.getProducts("barcode==" + productBarcode))
					.iterator().next();
			return product;
		} catch (NoSuchElementException e) {
			throw new NotInDatabaseException("No matching product found in database!");
		}
	}

	@Override
	public Collection<IProduct> queryAllProducts() {
		Collection<IProduct> products = csvHelper.getProducts(backendConnection.getProducts("name=LIKE%20'*'"));

		return products;
	}

	@Override
	public Collection<IStore> queryStoreByName(long enterpriseID, String storeName) {
		storeName = QueryParameterEncoder.encodeQueryString(storeName);
		Collection<IStore> stores = csvHelper
				.getStores(backendConnection.getStores("name=LIKE%20'" + storeName + "';Store.enterprise.id==" + enterpriseID));
		return stores;
	}

}
