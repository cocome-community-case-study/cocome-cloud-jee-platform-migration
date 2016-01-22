/***************************************************************************
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
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.data.store;

import java.util.Collection;

import javax.ejb.Local;

import org.cocome.tradingsystem.inventory.application.store.IStoreInventoryLocal;
import org.cocome.tradingsystem.inventory.application.store.IStoreInventoryManagerLocal;
import org.cocome.tradingsystem.inventory.data.enterprise.Product;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * This interface provides methods for querying the database. The interface will
 * be used by the InventoryApplication. The methods are derived from methods
 * defined in {@link IStoreInventoryManagerLocal} and {@link IStoreInventoryLocal}.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 */
@Local
public interface IStoreQueryLocal {

	//
	// Global queries.
	//

	/**
	 * Queries the database for a {@link Store} entity with given name and location.
	 * If more than one entity match these criteria, the first match will be returned.
	 * 
	 * @param name
	 *            store name
	 * @param location
	 *            store location, may contain wildcard characters and may be empty.
	 *            
	 * @return
	 *         {@link Store} entity or {@code null} if there is no store with
	 *         the given name and location.
	 * 
	 */
	public Store queryStore(String name, String location);

	/**
	 * Returns a {@link Store} entity corresponding to the given unique identifier.
	 * 
	 * @param storeId
	 *            unique identifier of a {@link Store} entity
	 *            
	 * @return
	 *         {@link Store} entity
	 *         
	 * @throws NotInDatabaseException 
	 *             if a store with the given id could not be found
	 * @throws 
	 */
	public Store queryStoreById(long storeId) throws NotInDatabaseException;

	/**
	 * Returns a {@link StockItem} entity corresponding to the given unique identifier.
	 * 
	 * @param stockItemId
	 *            unique identifier of a {@link StockItem} entity
	 *            
	 * @return
	 *         {@link StockItem} entity
	 * 
	 * @throws NotInDatabaseException 
	 *             if a stock item with the given id could not be found
	 */
	public StockItem queryStockItemById(long stockItemId) throws NotInDatabaseException ;

	/**
	 * Returns a {@link Product} entity corresponding to the given unique identifier.
	 * 
	 * @param productId
	 *            unique identifier of a {@link Product} entity
	 *            
	 * @return
	 *         {@link Product} entity
	 * 
	 * @throws NotInDatabaseException 
	 *             if a product with the given id could not be found
	 */
	public Product queryProductById(long productId) throws NotInDatabaseException ;

	/**
	 * Queries the database for a {@link Product} entity with given barcode.
	 * 
	 * @param barcode
	 *            barcode of the product to find
	 *            
	 * @return
	 *         {@link Product} entity with the specified barcode, or {@code null} if there is no such product.
	 */
	public Product queryProductByBarcode(long barcode);

	/**
	 * Queries the database for a {@link ProductOrder} with given identifier.
	 * <p>
	 * The following methods from StoreIf use this method: List<ComplexOrderTO> orderProducts(ComplexOrderTO complexOrder, StoreTO storeTO); ComplexOrderTO
	 * getOrder(int orderId); void rollInReceivedOrder(ComplexOrderTO complexOrder, StoreTO store);
	 * 
	 * @param orderId
	 *            unique identifier of a {@link ProductOrder} entity
	 *            
	 * @return
	 *         An instance of {@link ProductOrder} with the specified id.
	 * 
	 * @throws NotInDatabaseException 
	 *             if a product order with the given id could not be found
	 */
	public ProductOrder queryOrderById(long orderId) throws NotInDatabaseException ;

	//
	// Queries limited to a given store.
	//

	/**
	 * Queries the specified store for all the products it carries, i.e.
	 * products for which there is a stock item.
	 * 
	 * @param storeId
	 *            unique identifier of a {@link Store} entity
	 *            
	 * @return
	 *         A collection of products carried by the given store.
	 */
	public Collection<Product> queryProducts(long storeId);

	/**
	 * Queries the specified store for all outstanding product orders, i.e.
	 * product orders which do not have their delivery date set.
	 * 
	 * @param storeId
	 *            unique identifier of a {@link Store} entity
	 *            
	 * @return
	 *         A collection of outstanding product orders in the specified store.
	 */
	public Collection<ProductOrder> queryOutstandingOrders(long storeId);
	
	/**
	 * Queries the specified store for all product orders
	 * 
	 * @param storeId
	 *            unique identifier of a {@link Store} entity
	 *            
	 * @return
	 *         A collection of product orders in the specified store.
	 */
	public Collection<ProductOrder> queryAllOrders(long storeId);
	
	/**
	 * Queries the specified store for all product orders containing the given product.
	 * 
	 * @param storeId
	 * 			unique identifier of a {@link Store} entity
	 *            
	 * @param productBarcode
	 * 			barcode of the required product
	 * 
	 * @param amount
	 * 			amount ordered of the product in the required order
	 *           
	 * @return
	 *         The required product order in the specified store.
	 *         
	 * @throws NotInDatabaseException 
	 * 			if no order with the given information can be found
	 */
	public ProductOrder queryProductOrder(long storeId, long productBarcode, long amount)  
			throws NotInDatabaseException ;

	/**
	 * Queries a store for all its stock items.
	 * 
	 * @param storeId
	 *            unique identifier of a {@link Store} entity
	 *            
	 * @return
	 *         A collection of {@link StockItem} entities.
	 */
	public Collection<StockItem> queryAllStockItems(long storeId);

	/**
	 * Queries a given store for all stock items with low stock.
	 * 
	 * @param storeId
	 *            unique identifier of a {@link Store} entity
	 * @param pctx
	 *            the persistence context
	 * @return
	 *         A collection of {@link StockItem} entities objects representing
	 *         low stock products in the given store.
	 */
	public Collection<StockItem> queryLowStockItems(long storeId);

	/**
	 * Queries the specified store for a stock item associated with product
	 * identified by specified barcode.
	 * 
	 * @param storeId
	 *            unique identifier of a {@link Store} entity
	 * @param productBarcode
	 *            product barcode for which to find the stock item
	 * @param pctx
	 *            the persistence context
	 * @return
	 *         The StockItem from the given store for a product with the given
	 *         barcode, or {@code null} if the stock item could not be found.
	 */
	public StockItem queryStockItem(
			long storeId, long productBarcode
			);

	/**
	 * Returns the stock for the given productIds.
	 * 
	 * @param storeId
	 *            unique identifier of a {@link Store} entity
	 * @param productIds
	 *            {@link Product} entity identifiers to look up in the stock
	 * @param pctx
	 *            the persistence context
	 * @return
	 *         The products as StockItems (including amounts)
	 */
	public Collection<StockItem> queryStockItemsByProductId(
			long storeId, long[] productIds
			);

}
