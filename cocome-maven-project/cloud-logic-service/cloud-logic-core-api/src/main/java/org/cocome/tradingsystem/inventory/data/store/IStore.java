package org.cocome.tradingsystem.inventory.data.store;

import java.util.Collection;

import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IStore {

	void initStore();

	/**
	 * @return A unique identifier for Store objects
	 */
	long getId();

	/**
	 * @param id
	 *            A unique identifier for Store objects
	 */
	void setId(long id);

	/**
	 * Returns the name of the store.
	 * 
	 * @return Store name.
	 */
	String getName();

	/**
	 * @param name
	 *            the name of the Store
	 */
	void setName(String name);

	/**
	 * Returns the location of the store.
	 * 
	 * @return Store location.
	 */
	String getLocation();

	/**
	 * Sets the location of the store.
	 * 
	 * @param location
	 *            store location
	 */
	void setLocation(String location);

	/**
	 * @return The enterprise which the Store belongs to
	 */
	ITradingEnterprise getEnterprise() throws NotInDatabaseException;

	/**
	 * @param enterprise
	 *            The enterprise which the Store belongs to
	 */
	void setEnterprise(ITradingEnterprise enterprise);

	/**
	 * @return All product orders of the Store.
	 */
	Collection<IProductOrder> getProductOrders();

	/**
	 * @param productOrders
	 *            all product orders of the Store
	 */
	void setProductOrders(Collection<IProductOrder> productOrders);

	/**
	 * @return
	 *         A list of StockItem objects. A StockItem represents a concrete
	 *         product in the store including sales price, ...
	 */
	Collection<IStockItem> getStockItems();

	/**
	 * @param stockItems
	 *            A list of StockItem objects. A StockItem represents a concrete
	 *            product in the store including sales price, ...
	 */
	void setStockItems(Collection<IStockItem> stockItems);

	String toString();

	int compareTo(IStore o);

	String getEnterpriseName();

	void setEnterpriseName(String enterpriseName);

}