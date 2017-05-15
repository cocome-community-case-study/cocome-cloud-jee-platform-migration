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
import java.util.Date;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * The ProductOrder class represents an ProductOrder of a Store in the database.
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

@Dependent
class ProductOrder implements Serializable, IProductOrder {
	private static final long serialVersionUID = -8340585715760459030L;

	private long id;

	private Date deliveryDate;

	private Date orderingDate;

	private Collection<IOrderEntry> orderEntries;
	
	private String storeName;
	
	private String storeLocation;

	private IStore store;
	
	@Inject
	Instance<IStoreQuery> storeQueryInstance;
	
	private IStoreQuery storeQuery;
	
	@PostConstruct
	private void initProductOrder() {
		storeQuery = storeQueryInstance.get();
		orderEntries = new LinkedList<IOrderEntry>();
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#getOrderEntries()
	 */
	@Override
	public Collection<IOrderEntry> getOrderEntries() {
		return orderEntries;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#setOrderEntries(java.util.Collection)
	 */
	@Override
	public void setOrderEntries(final Collection<IOrderEntry> orderEntries) {
		this.orderEntries = orderEntries;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#getOrderingDate()
	 */
	@Override
	public Date getOrderingDate() {
		return orderingDate;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#setOrderingDate(java.util.Date)
	 */
	@Override
	public void setOrderingDate(final Date orderingDate) {
		this.orderingDate = orderingDate;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#getDeliveryDate()
	 */
	@Override
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#setDeliveryDate(java.util.Date)
	 */
	@Override
	public void setDeliveryDate(final Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#getStore()
	 */
	@Override
	public IStore getStore() {
		if (store == null) {
			store = storeQuery.queryStore(storeName, storeLocation);
		}
		return store;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#setStore(org.cocome.tradingsystem.inventory.data.store.Store)
	 */
	@Override
	public void setStore(final IStore store) {
		this.store = store;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#getStoreName()
	 */
	@Override
	public String getStoreName() {
		return storeName;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#setStoreName(java.lang.String)
	 */
	@Override
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#getStoreLocation()
	 */
	@Override
	public String getStoreLocation() {
		return storeLocation;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IProductOrder#setStoreLocation(java.lang.String)
	 */
	@Override
	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

}
