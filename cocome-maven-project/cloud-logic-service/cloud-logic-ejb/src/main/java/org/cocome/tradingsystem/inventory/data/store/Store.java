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

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQueryLocal;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Represents a store in the database.
 * 
 * @author Yannick Welsch
 */

public class Store implements Serializable, Comparable<Store> {
	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private String location;

	private String enterpriseName;
	
	private TradingEnterprise enterprise;

	private Collection<ProductOrder> productOrders;

	private Collection<StockItem> stockItems;
	
	@Inject
	IStoreQueryLocal storeQuery;
	
	@Inject
	IEnterpriseQueryLocal enterpriseQuery;
	
	@PostConstruct
	public void initStore() {
		enterprise = null;
		productOrders = null;
		stockItems = null;
	}

	/**
	 * @return A unique identifier for Store objects
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            A unique identifier for Store objects
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Returns the name of the store.
	 * 
	 * @return Store name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name of the Store
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the location of the store.
	 * 
	 * @return Store location.
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Sets the location of the store.
	 * 
	 * @param location
	 *            store location
	 */
	public void setLocation(final String location) {
		this.location = location;
	}

	/**
	 * @return The enterprise which the Store belongs to
	 */
	public TradingEnterprise getEnterprise() throws NotInDatabaseException {
		if (enterprise == null) {
			enterprise = enterpriseQuery.queryEnterpriseByName(enterpriseName);
		}
		return enterprise;
	}

	/**
	 * @param enterprise
	 *            The enterprise which the Store belongs to
	 */
	public void setEnterprise(final TradingEnterprise enterprise) {
		this.enterprise = enterprise;
	}

	/**
	 * @return All product orders of the Store.
	 */
	public Collection<ProductOrder> getProductOrders() {
		if (productOrders == null) {
			productOrders = storeQuery.queryAllOrders(id);
		}
		return productOrders;
	}

	/**
	 * @param productOrders
	 *            all product orders of the Store
	 */
	public void setProductOrders(final Collection<ProductOrder> productOrders) {
		this.productOrders = productOrders;
	}

	/**
	 * @return
	 *         A list of StockItem objects. A StockItem represents a concrete
	 *         product in the store including sales price, ...
	 */
	public Collection<StockItem> getStockItems() {
		if (stockItems == null) {
			stockItems = storeQuery.queryAllStockItems(id);
		}
		return stockItems;
	}

	/**
	 * @param stockItems
	 *            A list of StockItem objects. A StockItem represents a concrete
	 *            product in the store including sales price, ...
	 */
	public void setStockItems(final Collection<StockItem> stockItems) {
		this.stockItems = stockItems;
	}

	@Override
	public String toString() {
		return "[Id:"+getId()+",Name:"+this.getName()+",Location:"+this.getLocation()+"]";
	}

	@Override
	public int compareTo(Store o) {
		try {
			if(this.getEnterprise().getName().equals(o.getEnterprise().getName())
					&& this.getName().equals(o.getName())
					&& this.getLocation().equals(o.getLocation())
					){
				return 0;
			}
		} catch (NotInDatabaseException e) {
			return 0;
		}
		return 1;	
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

}
