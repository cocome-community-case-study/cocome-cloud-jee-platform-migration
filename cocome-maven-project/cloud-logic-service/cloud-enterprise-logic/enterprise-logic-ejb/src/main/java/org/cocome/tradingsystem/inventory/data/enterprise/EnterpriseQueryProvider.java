package org.cocome.tradingsystem.inventory.data.enterprise;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.inventory.data.store.IStoreQuery;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.connection.QueryParameterEncoder;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

@Stateless
@Local
public class EnterpriseQueryProvider implements IEnterpriseQuery {

    private static final Logger LOG = Logger.getLogger(EnterpriseQueryProvider.class);

    @Inject
    private IBackendQuery backendConnection;

    @Inject
    private IStoreQuery storeQuery;

    @Inject
    private IBackendConversionHelper csvHelper;

    @Override
    public ITradingEnterprise queryEnterpriseById(long enterpriseID) throws NotInDatabaseException {
        LOG.debug("Trying to retrieve enterprise with id " + enterpriseID);
        try {
            String backendMessage = backendConnection.getEnterprises("id==" + enterpriseID);
            LOG.debug("Backend response: " + backendMessage);
            return csvHelper.getEnterprises(backendMessage).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("Enterprise with ID " + enterpriseID + " could not be found!");
        }
    }

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

    @Override
    public Collection<ICustomProduct> queryAllCustomProducts(long enterpriseID) throws NotInDatabaseException {
        //TODO
        return null;
    }

    @Override
    public Collection<ICustomProduct> queryAllCustomProducts() {
        //TODO
        return null;
    }

    @Override
    public Collection<IProduct> queryAllProducts(long enterpriseID) {
        // Because the backend doesn't allow joins, it is neccessary to first
        // get the stores
        Collection<IStore> stores = queryStoresByEnterpriseId(enterpriseID);
        Collection<IProduct> products = new LinkedList<>();

        for (IStore store : stores) {
            products.addAll(storeQuery.queryProducts(store.getId()));
        }

        return products;
    }

    @Override
    public Collection<IProductSupplier> querySuppliers(long enterpriseID) throws NotInDatabaseException {
        ITradingEnterprise enterprise = queryEnterpriseById(enterpriseID);

        return enterprise.getSuppliers();
    }

    @Override
    public ITradingEnterprise queryEnterpriseByName(String enterpriseName) throws NotInDatabaseException {
        enterpriseName = QueryParameterEncoder.encodeQueryString(enterpriseName);

        try {
            return csvHelper
                    .getEnterprises(backendConnection.getEnterprises("name=LIKE%20'" + enterpriseName + "'")).iterator()
                    .next();
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
        return csvHelper.getStores(backendConnection.getStores("enterprise.id==" + enterpriseID));
    }

    @Override
    public Collection<IPlant> queryPlantsByEnterpriseId(long enterpriseID) {
        return csvHelper.getPlants(backendConnection.getEntity("Plant", "enterprise.id==" + enterpriseID));
    }

    @Override
    public Collection<IProductionUnitClass> queryProductionUnitClassesByEnterpriseId(long enterpriseID) {
        return csvHelper.getProductionUnitClasses(backendConnection.getEntity(
                "ProductionUnitClass",
                "enterprise.id==" + enterpriseID));
    }

    @Override
    public IPlant queryPlantByEnterprise(long enterpriseID, long plantID) throws NotInDatabaseException {
        try {
            return csvHelper
                    .getPlants(backendConnection.getEntity("Plant",
                            "id==" + plantID + ";Plant.enterprise.id==" + enterpriseID))
                    .iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching plant found in database!");
        }
    }

    @Override
    public IProductionUnitClass queryProductionUnitClassByEnterprise(long enterpriseID, long productionUnitClassID) throws NotInDatabaseException {
        try {
            return csvHelper
                    .getProductionUnitClasses(backendConnection.getEntity("ProductionUnitClass",
                            "id==" + productionUnitClassID + ";ProductionUnitClass.enterprise.id==" + enterpriseID))
                    .iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching production unit class found in database!");
        }
    }

    @Override
    public ICustomProduct queryCustomProductByID(long productID) throws NotInDatabaseException {
        //TODO
        return null;
    }

    @Override
    public ICustomProduct queryCustomProductByBarcode(long productBarcode) throws NotInDatabaseException {
        //TODO
        return null;
    }


    @Override
    public Collection<IPlant> queryPlantByName(long enterpriseID, String plantName) {
        plantName = QueryParameterEncoder.encodeQueryString(plantName);
        return csvHelper
                .getPlants(backendConnection.getEntity("Plant", "name=LIKE%20'" + plantName
                        + "';Plant.enterprise.id==" + enterpriseID));
    }

    @Override
    public Collection<ITradingEnterprise> queryAllEnterprises() {
        return csvHelper
                .getEnterprises(backendConnection.getEnterprises("name=LIKE%20'*'"));
    }

    @Override
    public IStore queryStoreByEnterprise(long enterpriseID, long storeID) throws NotInDatabaseException {
        try {
            return csvHelper
                    .getStores(backendConnection.getStores("id==" + storeID + ";Store.enterprise.id==" + enterpriseID))
                    .iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching store found in database!");
        }
    }

    @Override
    public IProductSupplier querySupplierByID(long supplierID) throws NotInDatabaseException {
        try {
            return csvHelper
                    .getProductSuppliers(backendConnection.getProductSupplier("id==" + supplierID)).iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching supplier found in database!");
        }
    }

    @Override
    public IProduct queryProductByID(long productID) throws NotInDatabaseException {
        try {
            return csvHelper.getProducts(backendConnection.getProducts("id==" + productID)).iterator()
                    .next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching product found in database!");
        }
    }

    @Override
    public IProduct queryProductByBarcode(long productBarcode) throws NotInDatabaseException {
        try {
            return csvHelper.getProducts(backendConnection.getProducts("barcode==" + productBarcode))
                    .iterator().next();
        } catch (NoSuchElementException e) {
            throw new NotInDatabaseException("No matching product found in database!");
        }
    }

    @Override
    public Collection<IProduct> queryAllProducts() {
        return csvHelper.getProducts(backendConnection.getProducts("name=LIKE%20'*'"));
    }

    @Override
    public Collection<IStore> queryStoreByName(long enterpriseID, String storeName) {
        storeName = QueryParameterEncoder.encodeQueryString(storeName);
        return csvHelper
                .getStores(backendConnection.getStores("name=LIKE%20'" + storeName
                        + "';Store.enterprise.id==" + enterpriseID));
    }

}
