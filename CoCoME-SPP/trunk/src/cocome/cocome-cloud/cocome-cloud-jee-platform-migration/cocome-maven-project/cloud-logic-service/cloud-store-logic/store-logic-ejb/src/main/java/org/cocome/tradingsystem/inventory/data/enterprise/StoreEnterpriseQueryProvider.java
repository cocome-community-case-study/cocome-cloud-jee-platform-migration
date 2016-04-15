package org.cocome.tradingsystem.inventory.data.enterprise;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.registry.client.IApplicationHelper;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.IEnterpriseManagerService;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.registry.service.Names;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

@Stateless
@Local(IEnterpriseQueryLocal.class)
public class StoreEnterpriseQueryProvider implements IEnterpriseQueryLocal {
	private static final Logger LOG = Logger.getLogger(StoreEnterpriseQueryProvider.class);

	@Inject
	long defaultEnterpriseIndex;

	@Inject
	IApplicationHelper applicationHelper;

	@Inject
	IEnterpriseDataFactory enterpriseFactory;

	@Inject
	IStoreDataFactory storeFactory;

	private IEnterpriseManager lookupEnterpriseManager(long enterpriseID) throws NotInDatabaseException {
		IEnterpriseManagerService enterpriseService;
		try {
			enterpriseService = applicationHelper.getComponent(Names.getEnterpriseManagerRegistryName(enterpriseID),
					IEnterpriseManagerService.SERVICE, IEnterpriseManagerService.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
			if (enterpriseID == defaultEnterpriseIndex) {
				throw new NotInDatabaseException(
						"Exception occured while retrieving the enterprise service: " + e.getMessage());
			} else {
				LOG.info("Looking up default enterprise server because there was none registered for id "
						+ enterpriseID);
				return lookupEnterpriseManager(defaultEnterpriseIndex);
			}
		}
		return enterpriseService.getIEnterpriseManagerPort();
	}

	private IEnterpriseManager lookupEnterpriseManager(String enterpriseName) throws NotInDatabaseException {
		IEnterpriseManagerService enterpriseService;
		try {
			enterpriseService = applicationHelper.getComponent(Names.getEnterpriseManagerRegistryName(enterpriseName),
					IEnterpriseManagerService.SERVICE, IEnterpriseManagerService.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| MalformedURLException | NoSuchMethodException | SecurityException | NotBoundException_Exception e) {
			LOG.info("Looking up default enterprise server because there was none registered for name "
					+ enterpriseName);
			return lookupEnterpriseManager(defaultEnterpriseIndex);
		}
		return enterpriseService.getIEnterpriseManagerPort();
	}

	@Override
	public ITradingEnterprise queryEnterpriseById(long enterpriseID) throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager = lookupEnterpriseManager(enterpriseID);

		try {
			return enterpriseFactory.convertToEnterprise(enterpriseManager.queryEnterpriseById(enterpriseID));
		} catch (NotInDatabaseException_Exception e) {
			throw new NotInDatabaseException(e.getFaultInfo().getMessage());
		}
	}

	@Override
	public Collection<IStore> queryStoresByEnterpriseId(long enterpriseID) {
		IEnterpriseManager enterpriseManager;
		List<StoreWithEnterpriseTO> storeTOList;
		try {
			enterpriseManager = lookupEnterpriseManager(enterpriseID);
			storeTOList = enterpriseManager.queryStoresByEnterpriseID(enterpriseID);
		} catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
			LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
			return Collections.emptyList();
		}

		List<IStore> storeList = new ArrayList<>(storeTOList.size());

		for (StoreWithEnterpriseTO storeTO : storeTOList) {
			storeList.add(storeFactory.convertToStore(storeTO));
		}
		return storeList;
	}

	@Override
	public IStore queryStoreByEnterprise(long enterpriseID, long storeID) throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager = lookupEnterpriseManager(enterpriseID);
		try {
			return storeFactory.convertToStore(enterpriseManager.queryStoreByEnterpriseID(enterpriseID, storeID));
		} catch (NotInDatabaseException_Exception e) {
			throw new NotInDatabaseException(e.getFaultInfo().getMessage());
		}
	}

	@Override
	public ITradingEnterprise queryEnterpriseByName(String enterpriseName) throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager = lookupEnterpriseManager(enterpriseName);
		try {
			EnterpriseTO enterprise = enterpriseManager.queryEnterpriseByName(enterpriseName);
			return enterpriseFactory.convertToEnterprise(enterprise);
		} catch (NotInDatabaseException_Exception e) {
			throw new NotInDatabaseException(e.getFaultInfo().getMessage());
		}
	}

	@Override
	public long getMeanTimeToDelivery(IProductSupplier supplier, ITradingEnterprise enterprise) {
		IEnterpriseManager enterpriseManager;
		try {
			enterpriseManager = lookupEnterpriseManager(enterprise.getId());
		} catch (NotInDatabaseException e) {
			LOG.error("Error retrieving enterprise manager: " + e.getMessage());
			return 0;
		}
		SupplierTO supplierTO = enterpriseFactory.fillSupplierTO(supplier);
		EnterpriseTO enterpriseTO = enterpriseFactory.fillEnterpriseTO(enterprise);
		try {
			return enterpriseManager.getMeanTimeToDelivery(supplierTO, enterpriseTO);
		} catch (NotInDatabaseException_Exception e) {
			LOG.error("Error querying mean time to delivery: " + e.getFaultInfo().getMessage());
			return 0;
		}
	}

	@Override
	public Collection<IProduct> queryAllProducts(long enterpriseID) throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager;
		List<ProductTO> productTOList;
		try {
			enterpriseManager = lookupEnterpriseManager(enterpriseID);
			productTOList = enterpriseManager.getAllEnterpriseProducts(enterpriseID);
		} catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
			LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
			return Collections.emptyList();
		}

		List<IProduct> productList = new ArrayList<>(productTOList.size());

		for (ProductTO productTO : productTOList) {
			productList.add(enterpriseFactory.convertToProduct(productTO));
		}
		return productList;
	}

	@Override
	public Collection<IProduct> queryAllProducts() {
		IEnterpriseManager enterpriseManager;
		List<ProductTO> productTOList;
		try {
			enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
			productTOList = enterpriseManager.getAllEnterpriseProducts(defaultEnterpriseIndex);
		} catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
			LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
			return Collections.emptyList();
		}

		List<IProduct> productList = new ArrayList<>(productTOList.size());

		for (ProductTO productTO : productTOList) {
			productList.add(enterpriseFactory.convertToProduct(productTO));
		}
		return productList;
	}

	@Override
	public IProduct queryProductByID(long productID) throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager;
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		try {
			return enterpriseFactory.convertToProduct(enterpriseManager.getProductByID(productID));
		} catch (NotInDatabaseException_Exception e) {
			throw new NotInDatabaseException(e.getFaultInfo().getMessage());
		}
	}

	@Override
	public IProduct queryProductByBarcode(long productBarcode) throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager;
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		try {
			return enterpriseFactory.convertToProduct(enterpriseManager.getProductByBarcode(productBarcode));
		} catch (NotInDatabaseException_Exception e) {
			throw new NotInDatabaseException(e.getFaultInfo().getMessage());
		}
	}

	@Override
	public Collection<IProductSupplier> querySuppliers(long enterpriseID) throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager;
		List<SupplierTO> supplierTOList;
		try {
			enterpriseManager = lookupEnterpriseManager(enterpriseID);
			supplierTOList = enterpriseManager.querySuppliers(enterpriseID);
		} catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
			LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
			return Collections.emptyList();
		}

		List<IProductSupplier> supplierList = new ArrayList<>(supplierTOList.size());

		for (SupplierTO supplierTO : supplierTOList) {
			supplierList.add(enterpriseFactory.convertToSupplier(supplierTO));
		}
		return supplierList;
	}

	@Override
	public IProductSupplier querySupplierByID(long supplierID) throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager;
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		try {
			return enterpriseFactory.convertToSupplier(enterpriseManager.getSupplierByID(supplierID));
		} catch (NotInDatabaseException_Exception e) {
			throw new NotInDatabaseException(e.getFaultInfo().getMessage());
		}
	}

	@Override
	public IProductSupplier querySupplierForProduct(long enterpriseID, long productBarcode)
			throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager;
		enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
		try {
			return enterpriseFactory
					.convertToSupplier(enterpriseManager.querySupplierForProduct(enterpriseID, productBarcode));
		} catch (NotInDatabaseException_Exception e) {
			throw new NotInDatabaseException(e.getFaultInfo().getMessage());
		}
	}

	@Override
	public Collection<IProduct> queryProductsBySupplier(long enterpriseID, long productSupplierID)
			throws NotInDatabaseException {
		IEnterpriseManager enterpriseManager;
		List<ProductTO> productTOList;
		try {
			enterpriseManager = lookupEnterpriseManager(enterpriseID);
			productTOList = enterpriseManager.getProductsBySupplier(enterpriseID, productSupplierID);
		} catch (NotInDatabaseException | NotInDatabaseException_Exception e) {
			LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
			return Collections.emptyList();
		}

		List<IProduct> productList = new ArrayList<>(productTOList.size());

		for (ProductTO productTO : productTOList) {
			productList.add(enterpriseFactory.convertToProduct(productTO));
		}
		return productList;
	}

	@Override
	public Collection<ITradingEnterprise> queryAllEnterprises() {
		IEnterpriseManager enterpriseManager;
		List<EnterpriseTO> enterpriseTOList;
		try {
			enterpriseManager = lookupEnterpriseManager(defaultEnterpriseIndex);
			enterpriseTOList = enterpriseManager.getEnterprises();
		} catch (NotInDatabaseException e) {
			LOG.error("Got error while looking up stores by enterprise: " + e.getMessage());
			return Collections.emptyList();
		}

		List<ITradingEnterprise> enterpriseList = new ArrayList<>(enterpriseTOList.size());

		for (EnterpriseTO enterpriseTO : enterpriseTOList) {
			enterpriseList.add(enterpriseFactory.convertToEnterprise(enterpriseTO));
		}
		return enterpriseList;
	}

}
