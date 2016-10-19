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
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Represents a store in the database.
 * 
 * @author Yannick Welsch
 */

@Dependent
class Store implements Serializable, Comparable<IStore>, IStore {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(Store.class);

	private long id;

	private String name;

	private String location;

	private String enterpriseName;
	
	private ITradingEnterprise enterprise;

	private Collection<IProductOrder> productOrders;

	private Collection<IStockItem> stockItems;
	
	@Inject
	Instance<IStoreQuery> storeQueryInstance;
	
	IStoreQuery storeQuery;
	
	@Inject
	Instance<IEnterpriseQuery> enterpriseQueryInstance;
	
	IEnterpriseQuery enterpriseQuery;
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#initStore()
	 */
	@Override
	@PostConstruct
	public void initStore() {
		storeQuery = storeQueryInstance.get();
		enterpriseQuery = enterpriseQueryInstance.get();
		
		enterprise = null;
		productOrders = null;
		stockItems = null;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#getId()
	 */
	@Override
	public long getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#setId(long)
	 */
	@Override
	public void setId(final long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#getLocation()
	 */
	@Override
	public String getLocation() {
		return this.location;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#setLocation(java.lang.String)
	 */
	@Override
	public void setLocation(final String location) {
		this.location = location;
	}

	/**
	 * @return The enterprise which the Store belongs to
	 */
	@Override
	public ITradingEnterprise getEnterprise() throws NotInDatabaseException {
		if (enterprise == null) {
			enterprise = enterpriseQuery.queryEnterpriseByName(enterpriseName);
			LOG.debug(String.format("Retrieved enterprise [%d, %s] for store %s", enterprise.getId(), enterprise.getName(), name));
		}
		return enterprise;
	}

	/**
	 * @param enterprise
	 *            The enterprise which the Store belongs to
	 */
	@Override
	public void setEnterprise(final ITradingEnterprise enterprise) {
		this.enterprise = enterprise;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#getProductOrders()
	 */
	@Override
	public Collection<IProductOrder> getProductOrders() {
		if (productOrders == null) {
			productOrders = storeQuery.queryAllOrders(id);
		}
		return productOrders;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#setProductOrders(java.util.Collection)
	 */
	@Override
	public void setProductOrders(final Collection<IProductOrder> productOrders) {
		this.productOrders = productOrders;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#getStockItems()
	 */
	@Override
	public Collection<IStockItem> getStockItems() {
		if (stockItems == null) {
			stockItems = storeQuery.queryAllStockItems(id);
		}
		return stockItems;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#setStockItems(java.util.Collection)
	 */
	@Override
	public void setStockItems(final Collection<IStockItem> stockItems) {
		this.stockItems = stockItems;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#toString()
	 */
	@Override
	public String toString() {
		return "[Id:"+getId()+",Name:"+this.getName()+",Location:"+this.getLocation()+"]";
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#compareTo(org.cocome.tradingsystem.inventory.data.store.Store)
	 */
	@Override
	public int compareTo(IStore o) {
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

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#getEnterpriseName()
	 */
	@Override
	public String getEnterpriseName() {
		return enterpriseName;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStore#setEnterpriseName(java.lang.String)
	 */
	@Override
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

}
