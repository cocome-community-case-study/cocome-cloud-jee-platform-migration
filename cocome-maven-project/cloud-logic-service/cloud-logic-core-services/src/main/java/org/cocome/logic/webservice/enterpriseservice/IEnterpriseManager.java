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

package org.cocome.logic.webservice.enterpriseservice;

import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.CreateException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService(targetNamespace = "http://enterprise.webservice.logic.cocome.org/")
public interface IEnterpriseManager {

    /* CRUD for {@link EnterpriseTO} **************/

    @WebMethod
    Collection<EnterpriseTO> getEnterprises();

    /**
     * @return A EnterpriseTO object with the specified id.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    @WebMethod
    EnterpriseTO queryEnterpriseById(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId) throws NotInDatabaseException;

    /**
     * @param enterpriseName the unique identifier of a TradingEnterprise entity
     * @return A EnterpriseTO object containing the enterprise with the specified name.
     * @throws NotInDatabaseException if a trading enterprise with the given name could not be found
     */
    @WebMethod
    EnterpriseTO queryEnterpriseByName(
            @XmlElement(required = true) @WebParam(name = "enterpriseName") String enterpriseName) throws NotInDatabaseException;

    @WebMethod
    long createEnterprise(
            @XmlElement(required = true) @WebParam(name = "enterpriseName") String enterpriseName)
            throws CreateException;

    @WebMethod
    void deleteEnterprise(
            @XmlElement(required = true) @WebParam(name = "enterpriseTO") EnterpriseTO enterpriseTO)
            throws NotInDatabaseException, UpdateException, IOException;

    /* CRUD for {@link PlantTO} **************/

    /**
     * @param enterpriseId the unique identifier of a TradingEnterprise entity
     * @return A collection of {@link PlantTO} objects belonging to the given enterprise.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    @WebMethod
    Collection<PlantTO> queryPlantsByEnterpriseID(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId) throws NotInDatabaseException;

    /**
     * @param plantId the unique identifier of a Plant entity
     * @return A PlantWithEntepriseTO object with the given store identifier and
     * belonging to the given enterprise.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    @WebMethod
    PlantTO queryPlantByID(
            @XmlElement(required = true) @WebParam(name = "plantID") long plantId) throws NotInDatabaseException;

    /**
     * Queries the database for a plant with the given name in the given enterprise.
     * If there are multiple plants with the given name, all of them are returned.
     * If there is no plant with the given name, an empty collection is returned.
     *
     * @param enterpriseId the unique identifier of a TradingEnterprise entity
     * @param plantName    the name of a Plant entity
     * @return All PLantWithEntepriseTO objects matching the given plant name and
     * belonging to the given enterprise.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    @WebMethod
    Collection<PlantTO> queryPlantByName(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId,
            @XmlElement(required = true) @WebParam(name = "PlantName") String plantName) throws NotInDatabaseException;

    @WebMethod
    long createPlant(
            @XmlElement(required = true) @WebParam(name = "plantTO") PlantTO plantTO)
            throws CreateException;

    /**
     * Updates the plant object. This method requires the EnterpriseTO to be present and to have
     * at least the id attribute set.
     *
     * @param plantTO
     * @throws UpdateException
     * @throws NotInDatabaseException
     */
    @WebMethod
    void updatePlant(
            @XmlElement(required = true) @WebParam(name = "plantTO") PlantTO plantTO)
            throws UpdateException, NotInDatabaseException;

    @WebMethod
    void deletePlant(
            @XmlElement(required = true)
            @WebParam(name = "plantTO") PlantTO plantTO)
            throws NotInDatabaseException, UpdateException, IOException;

    /**
     * Retrieves all products that are sold in this enterprise.
     * Note that there is no information included about the stores in which
     * this product is available.
     *
     * @param enterpriseId The enterprise for which all products should be retrieved
     * @return All {@code ProductTO}s available in the given enterprise
     * @throws NotInDatabaseException
     */
    @WebMethod
    Collection<ProductTO> getAllEnterpriseProducts(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId)
            throws NotInDatabaseException;

    /* CRUD for {@link ProductTO} **************/

    /**
     * Retrieves all products that are registered in the database.
     * Note that there is no information included about the stores in which
     * these products are available.
     *
     * @return All {@code ProductTO}s available or an empty collection if there are none
     */
    @WebMethod
    Collection<ProductTO> getAllProducts();

    /**
     * Retrieves the product with the given ID if it is stored in the database.
     * The product does not have to be available in any store.
     *
     * @return The {@code ProductTO} with the given ID or null if it was not found
     * @throws NotInDatabaseException
     */
    @WebMethod
    ProductTO getProductByID(
            @XmlElement(required = true) @WebParam(name = "productID") long productID) throws NotInDatabaseException;

    /**
     * Retrieves the product with the given ID if it is stored in the database.
     * The product does not have to be available in any store.
     *
     * @param enterpriseID the enterprise to search
     * @param supplierID   the supplier ID
     * @return The {@code ProductTO} with the given supplier ID or null if it was not found
     * @throws NotInDatabaseException
     */
    @WebMethod
    Collection<ProductTO> getProductsBySupplier(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID,
            @XmlElement(required = true) @WebParam(name = "supplierID") long supplierID) throws NotInDatabaseException;

    /**
     * Retrieves the product with the given barcode if it is stored in the database.
     * The product does not have to be available in any store.
     *
     * @return The {@code ProductTO} with the given ID or null if it was not found
     * @throws NotInDatabaseException
     */
    @WebMethod
    ProductTO getProductByBarcode(
            @XmlElement(required = true) @WebParam(name = "barcode") long barcode) throws NotInDatabaseException;

    @WebMethod
    long createProduct(
            @XmlElement(required = true) @WebParam(name = "productTO") ProductTO productTO)
            throws CreateException;

    @WebMethod
    void updateProduct(
            @XmlElement(required = true) @WebParam(name = "productTO") ProductWithSupplierTO productTO)
            throws UpdateException, NotInDatabaseException;

    /* CRUD for {@link SupplierTO} **************/

    /**
     * Retrieves the supplier with the given ID if it is stored in the database.
     *
     * @param supplierID the supplier ID
     * @return The {@code SupplierTO} with the given supplier ID or null if it was not found
     * @throws NotInDatabaseException
     */
    @WebMethod
    SupplierTO getSupplierByID(
            @XmlElement(required = true) @WebParam(name = "supplierID") long supplierID) throws NotInDatabaseException;

    /**
     * Retrieves all suppliers registered in the given enterprise.
     *
     * @param enterpriseID the enterprise to search
     * @return The {@code SupplierTO} with the given enterprise ID
     * @throws NotInDatabaseException
     */
    @WebMethod
    Collection<SupplierTO> querySuppliers(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID) throws NotInDatabaseException;

    /**
     * Retrieves the suppliers registered in this enterprise for the given product.
     *
     * @param enterpriseID   the id of the enterprise for which to retrieve the suppliers
     * @param productBarcode the barcode of the product to look for
     * @return the first supplier found and null if none is found
     * @throws NotInDatabaseException
     */
    @WebMethod
    SupplierTO querySupplierForProduct(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseID,
            @XmlElement(required = true) @WebParam(name = "productBarcode") long productBarcode) throws NotInDatabaseException;

    /* CRUD for {@link StoreTO} **************/

    /**
     * @param enterpriseId the unique identifier of a TradingEnterprise entity
     * @return A collection of StoreWithEntepriseTO objects belonging to the given enterprise.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    @WebMethod
    Collection<StoreWithEnterpriseTO> queryStoresByEnterpriseID(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId) throws NotInDatabaseException;

    /**
     * @param enterpriseId the unique identifier of a TradingEnterprise entity
     * @param storeId      the unique identifier of a Store entity
     * @return A StoreWithEntepriseTO object with the given store identifier and
     * belonging to the given enterprise.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    @WebMethod
    StoreWithEnterpriseTO queryStoreByEnterpriseID(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId,
            @XmlElement(required = true) @WebParam(name = "storeID") long storeId) throws NotInDatabaseException;

    /**
     * Queries the database for a store with the given name in the given enterprise.
     * If there are multiple stores with the given name, all of them are returned.
     * If there is no store with the given name, an empty collection is returned.
     *
     * @param enterpriseId the unique identifier of a TradingEnterprise entity
     * @param storeName    the name of a Store entity
     * @return All StoreWithEntepriseTO objects matching the given store name and
     * belonging to the given enterprise.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    @WebMethod
    Collection<StoreWithEnterpriseTO> queryStoreByName(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId,
            @XmlElement(required = true) @WebParam(name = "storeName") String storeName) throws NotInDatabaseException;

    @WebMethod
    long createStore(
            @XmlElement(required = true) @WebParam(name = "storeTO") StoreWithEnterpriseTO storeTO)
            throws CreateException;

    /**
     * Updates the store object. This method requires the EnterpriseTO to be present and to have
     * at least the id attribute set.
     *
     * @param storeTO
     * @throws UpdateException
     * @throws NotInDatabaseException
     */
    @WebMethod
    void updateStore(
            @XmlElement(required = true) @WebParam(name = "storeTO") StoreWithEnterpriseTO storeTO)
            throws UpdateException, NotInDatabaseException;

    /* CRUD for {@link CustomProductTO} **************/

    /**
     * Retrieves all products that are sold in this enterprise.
     * Note that there is no information included about the stores in which
     * this product is available.
     *
     * @param enterpriseId The enterprise for which all products should be retrieved
     * @return All {@code ProductTO}s available in the given enterprise
     * @throws NotInDatabaseException
     */
    @WebMethod
    Collection<CustomProductTO> getAllEnterpriseCustomProducts(
            @XmlElement(required = true) @WebParam(name = "enterpriseID") long enterpriseId)
            throws NotInDatabaseException;

    /**
     * Retrieves all products that are registered in the database.
     * Note that there is no information included about the stores in which
     * these products are available.
     *
     * @return All {@code ProductTO}s available or an empty collection if there are none
     */
    @WebMethod
    Collection<CustomProductTO> getAllCustomProducts();

    @WebMethod
    long createCustomProduct(
            @XmlElement(required = true) @WebParam(name = "customProductTO") CustomProductTO customProductTO)
            throws CreateException;

    @WebMethod
    void updateCustomProduct(
            @XmlElement(required = true) @WebParam(name = "customProductTO") CustomProductTO customProductTO)
            throws UpdateException, NotInDatabaseException;

    @WebMethod
    void deleteCustomProduct(
            @XmlElement(required = true)
            @WebParam(name = "customProductTO") CustomProductTO customProductTO)
            throws NotInDatabaseException, UpdateException, IOException;

    /* CRUD for {@link ProductionUnitClassTO} **************/



    /* Other Methods **************/

    /**
     * @param supplier   The supplier which delivers the products
     * @param enterprise The enterprise for which the products are delivered
     * @return The mean time to delivery in milliseconds
     * @throws NotInDatabaseException
     */
    @WebMethod
    long getMeanTimeToDelivery(
            @XmlElement(required = true) @WebParam(name = "productSupplier") SupplierTO supplier,
            @XmlElement(required = true) @WebParam(name = "enterprise") EnterpriseTO enterprise) throws NotInDatabaseException;

}
