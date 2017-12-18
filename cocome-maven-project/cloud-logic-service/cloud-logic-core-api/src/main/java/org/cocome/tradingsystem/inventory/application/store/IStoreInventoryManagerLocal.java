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

package org.cocome.tradingsystem.inventory.application.store;

import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.CreateException;
import javax.ejb.Local;
import java.util.List;

/**
 * Remote interface to the management facet of the store inventory application.
 * The store console uses this interface to obtain information about the store
 * and its stock as well as to change product sales prices, while the product
 * dispatcher uses it to manage movement of products among stores.
 * <p>
 * TODO Consider splitting the interface so that both the store console and product dispatcher have their own. --LB
 *
 * @author Sebastian Herold
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Local
public interface IStoreInventoryManagerLocal {

    /**
     * Registers the sale of products in the store inventory. Updates the amount
     * of stock items and checks for items that are running low on stock so that
     * they can be transported from nearby stores with sufficient stock. Used
     * for realization of UC 1 and UC 8.
     *
     * @param sale the sale to be registered in stock
     * @return an ticket id that can be used to observe the status of on demand production orders.
     * It is equal to 0 if the sale only consists of stock items
     * @throws ProductOutOfStockException
     * @throws NotInDatabaseException
     * @throws UpdateException
     */
    long accountSale(long storeID, final SaleTO sale)
            throws ProductOutOfStockException, NotInDatabaseException, UpdateException;

    /**
     * Returns information on the store in which the component is running. This
     * information is retrieved by the component during configuration and
     * initialization.
     *
     * @return Store and enterprise information about the local store.
     * @throws NotInDatabaseException
     */
    StoreWithEnterpriseTO getStore(long storeID) throws NotInDatabaseException;

    /**
     * Determines products and stock items which are nearly out of stock,
     * meaning amount is lower than 10% of maximal stock. Used for realization
     * of UC 3.
     *
     * @return List of products and their stock item in the given store.
     */
    List<ProductWithItemTO> getProductsWithLowStock(long storeID);

    /**
     * Returns all products offered by a given store (a stock item exists for a
     * product) and the supplier for each of them. Used for realization of UC 3.
     * <p>
     * TODO Consider returning all enterprise products, so that the manager can also new products sold by other stores within the enterprise. -- LB
     *
     * @return List of products and their suppliers
     * @throws NotInDatabaseException
     */
    List<ProductWithSupplierTO> getAllProducts(long storeID) throws NotInDatabaseException;

    /**
     * Returns all stock items offered by this store (a stock item exists for a
     * product) and the supplier and corresponding stock item for each of them.
     *
     * @return List of products, their suppliers and the corresponding stock item.
     * @throws NotInDatabaseException
     */
    List<ProductWithSupplierAndItemTO> getProductsWithStockItems(long storeID) throws NotInDatabaseException;

    /**
     * Returns all items offered by this store as on demand delivery (a on demand item exists for a
     * product) and the supplier and corresponding stock item for each of them.
     *
     * @return List of products, their suppliers and the corresponding stock item.
     * @throws NotInDatabaseException
     */
    List<ProductWithSupplierAndItemTO> getProductsWithOnDemandItems(long storeID) throws NotInDatabaseException;

    /**
     * Orders products for a store. A separate product order is created for
     * each supplier, with the ordering date set to the date of method
     * execution.
     * <p>
     * Used for realization of UC 3.
     *
     * @param complexOrder order for a store which contains all products to be ordered
     * @return List of product orders, one for each supplier that is affected.
     * @throws NotInDatabaseException
     * @throws CreateException
     * @throws UpdateException
     */
    List<ComplexOrderTO> orderProducts(long storeID, ComplexOrderTO complexOrder) throws NotInDatabaseException, CreateException, UpdateException;

    /**
     * Returns order information for a given order id. Used for realization of
     * UC 4.
     *
     * @param orderId the order entity identifier
     * @return Detailed order information of the desired order.
     * @throws NotInDatabaseException if there is no order with the given id
     */
    ComplexOrderTO getOrder(long storeID, long orderId) throws NotInDatabaseException;

    /**
     * Returns all outstanding product orders in the store.
     *
     * @return List ofDetailed order information of the desired order. NULL, if there
     * is no order with the given id.
     * @throws NotInDatabaseException
     */
    List<ComplexOrderTO> getOutstandingOrders(long storeID) throws NotInDatabaseException;

    /**
     * Updates stocks after order delivery. Adds amount of ordered items to the
     * stock items of the store. Sets delivery date to date of method execution.
     * Used for realization of UC 4.
     *
     * @param orderId the entity identifier of the order to roll in
     * @throws InvalidRollInRequestException
     * @throws NotInDatabaseException
     * @throws UpdateException
     */
    void rollInReceivedOrder(long storeID, long orderId)
            throws InvalidRollInRequestException, NotInDatabaseException, UpdateException;

    /**
     * Updates sales price of a stock item. Used for realization of UC 7.
     *
     * @param stockItemTO Stock item with new price.
     * @return Instance of ProductWithItemTO which holds product
     * information and updated price information for stock item
     * identified by <code>stockItemTO</code>.
     * @throws NotInDatabaseException
     * @throws UpdateException
     */
    ProductWithItemTO changePrice(long storeID, ProductWithItemTO stockItemTO) throws NotInDatabaseException, UpdateException;

    /**
     * Initiates the delivery of products that ran out at another store. The
     * products to be moved are marked as unavailable at this store afterwards.
     * <p>
     * Method required for UC 8 (product exchange (on low stock) among stores).
     * <p>
     * TODO Consider returning information on products that could be moved instead aborting when one product is out of stock. -- LB
     *
     * @param movedProductAmounts products and their amounts to be moved to another store
     * @throws ProductOutOfStockException if the local stock of any of the required products is less than
     *                                    the required amount
     * @throws UpdateException
     * @author SDQ
     */
    void markProductsUnavailableInStock(long storeID,
                                        ProductMovementTO movedProductAmounts
    ) throws ProductOutOfStockException, UpdateException;

    /**
     * Updates a stock item.
     *
     * @param itemTO item with new information.
     * @return Instance of ProductWithItemTO which holds product
     * information and updated price information for item
     * identified by {@code itemTO}.
     * @throws NotInDatabaseException
     * @throws UpdateException
     */
    ProductWithItemTO updateItem(long storeID, ProductWithItemTO itemTO) throws NotInDatabaseException, UpdateException;

    /**
     * Creates a new item.
     *
     * @param storeID ID of the store where to create the new item.
     * @param itemTO  Instance of ProductWithItemTO which holds the product
     *                information and the information about the new item.
     * @throws NotInDatabaseException
     * @throws CreateException
     */
    long createItem(long storeID, ProductWithItemTO itemTO)
            throws NotInDatabaseException, CreateException;

    /**
     * Deletes the given item.
     *
     * @param storeID ID of the store where to create the new item.
     * @param itemTO  Instance of ProductWithItemTO which holds the product
     *                information and the information about the item.
     * @throws NotInDatabaseException
     * @throws UpdateException
     */
    void deleteItem(long storeID, ProductWithItemTO itemTO)
            throws NotInDatabaseException, UpdateException;
}
