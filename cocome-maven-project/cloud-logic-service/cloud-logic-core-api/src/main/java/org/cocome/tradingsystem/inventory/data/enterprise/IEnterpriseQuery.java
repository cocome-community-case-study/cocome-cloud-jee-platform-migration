/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.application.reporting.IReportingLocal;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.Local;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 * This interface provides methods for querying the database. It is used by the
 * inventory application. The methods are derived from methods defined in the {@link IReportingLocal} interface.
 *
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */
@Local
public interface IEnterpriseQuery {

    /**
     * Retrieves an enterprise with the given id from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @return A TradingEnterprise object with the specified id.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    ITradingEnterprise queryEnterpriseById(
            long enterpriseID) throws NotInDatabaseException;

    /**
     * Retrieves all stores belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @return All stores found in the given enterprise or an empty collection
     */
    Collection<IStore> queryStoresByEnterpriseId(
            long enterpriseID);

    /**
     * Retrieves a specific store belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @param storeID      the unique identifier of the Store entity
     * @return The Store if found
     * @throws NotInDatabaseException if no Store could be found in the given enterprise
     */
    IStore queryStoreByEnterprise(
            long enterpriseID, long storeID) throws NotInDatabaseException;


    /**
     * Retrieves a specific store belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @return A list of stores with the given name or an empty list, if none was found.
     */
    Collection<IStore> queryStoreByName(
            long enterpriseID, String storeName);

    /**
     * Retrieves an enterprise with the given name from the database.
     *
     * @param enterpriseName the name of a TradingEnterprise entity
     * @return A TradingEnterprise object with the specified name if found.
     * @throws NotInDatabaseException if a trading enterprise with the given name could not be found
     */
    ITradingEnterprise queryEnterpriseByName(
            String enterpriseName) throws NotInDatabaseException;

    /**
     * Retrieves the mean time to delivery of the given supplier and
     * enterprise.
     *
     * @param supplier   The supplier which delivers the products
     * @param enterprise The enterprise for which the products are delivered
     * @return The mean time to delivery in milliseconds
     */
    long getMeanTimeToDelivery(IProductSupplier supplier,
                               ITradingEnterprise enterprise);

    /**
     * Retrieves all products that are sold in this enterprise.
     * Note that there is no information included about the stores in which
     * this product is available.
     *
     * @param enterpriseID The enterprise for which all products should be retrieved
     * @return All {@code Product}s available in the given enterprise
     * @throws EntityNotFoundException if the trading enterprise with the given id could not be found
     */
    Collection<IProduct> queryAllProducts(long enterpriseID) throws NotInDatabaseException;

    /**
     * Retrieves all products available.
     * Note that there is no information included about the stores in which
     * this product is available.
     *
     * @return All {@code Product}s available in the database or an
     * empty collection if there are none
     */
    Collection<IProduct> queryAllProducts();

    /**
     * Retrieves the product with the given id.
     *
     * @param productID The id of the product which should be retrieved
     * @return The product if it is found
     * @throws NotInDatabaseException if the product with the given id could not be found
     */
    IProduct queryProductByID(long productID) throws NotInDatabaseException;

    /**
     * Retrieves the product with the given barcode.
     *
     * @param productBarcode The barcode of the product which should be retrieved
     * @return The product if it is found
     * @throws NotInDatabaseException if the product with the given barcode could not be found
     */
    IProduct queryProductByBarcode(long productBarcode) throws NotInDatabaseException;

    /**
     * Retrieves the suppliers registered in this enterprise.
     *
     * @param enterpriseID the id of the enterprise for which to retrieve the suppliers
     * @return all suppliers found and an empty collection if none is found
     * @throws NotInDatabaseException
     */
    Collection<IProductSupplier> querySuppliers(long enterpriseID) throws NotInDatabaseException;

    /**
     * Retrieves the supplier with the given id if he is registered in this enterprise.
     *
     * @param supplierID the id of the supplier
     * @return the supplier
     * @throws NotInDatabaseException if no supplier could be found
     */
    IProductSupplier querySupplierByID(long supplierID) throws NotInDatabaseException;

    /**
     * Retrieves the suppliers registered in this enterprise for the given product.
     *
     * @param enterpriseID   the id of the enterprise for which to retrieve the suppliers
     * @param productBarcode the barcode of the product to look for
     * @return the first supplier found and null if none is found
     * @throws NotInDatabaseException
     */
    IProductSupplier querySupplierForProduct(long enterpriseID, long productBarcode) throws NotInDatabaseException;

    /**
     * Retrieves the products registered in this enterprise for the given product supplier.
     *
     * @param enterpriseID      the id of the enterprise for which to retrieve the suppliers
     * @param productSupplierID the id of the product supplier
     * @return a collection of available products for the given supplier
     * @throws NotInDatabaseException
     */
    Collection<IProduct> queryProductsBySupplier(long enterpriseID, long productSupplierID) throws NotInDatabaseException;

    /**
     * Retrieves all enterprises listed in the database.
     *
     * @return a collection of all found trading enterprises or an
     * empty one if no enterprise was found
     */
    Collection<ITradingEnterprise> queryAllEnterprises();

    /**
     * Retrieves a specific plant belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @param plantName    the name of the target plant
     * @return A list of plants with the given name or an empty list, if none was found.
     */
    Collection<IPlant> queryPlantByName(
            long enterpriseID, String plantName);

    /**
     * Retrieves all plants belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @return All plants found in the given enterprise or an empty collection
     */
    Collection<IPlant> queryPlantsByEnterpriseId(
            long enterpriseID);

    /**
     * Retrieves a specific plant belonging to this enterprise from the database.
     *
     * @param enterpriseID the unique identifier of a TradingEnterprise entity
     * @param plantID      the unique identifier of the Store entity
     * @return The Plant if found
     * @throws NotInDatabaseException if no Plant could be found in the given enterprise
     */
    IPlant queryPlantByEnterprise(
            long enterpriseID, long plantID) throws NotInDatabaseException;

}
