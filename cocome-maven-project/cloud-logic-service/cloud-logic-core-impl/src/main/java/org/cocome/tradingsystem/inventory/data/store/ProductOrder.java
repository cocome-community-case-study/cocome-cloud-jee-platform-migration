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
public class ProductOrder implements Serializable, IProductOrder {
	private static final long serialVersionUID = -8340585715760459030L;

	private long id;

	private Date deliveryDate;

	private Date orderingDate;

	private Collection<IOrderEntry> orderEntries;

	private String storeName;
	
	private String storeLocation;

	private IStore store;
	
	@Inject
	private Instance<IStoreQuery> storeQueryInstance;
	
	private IStoreQuery storeQuery;
	
	@PostConstruct
	private void initProductOrder() {
		storeQuery = storeQueryInstance.get();
		orderEntries = new LinkedList<>();
	}
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public Collection<IOrderEntry> getOrderEntries() {
		return orderEntries;
	}

	@Override
	public void setOrderEntries(final Collection<IOrderEntry> orderEntries) {
		this.orderEntries = orderEntries;
	}

	@Override
	public Date getOrderingDate() {
		return orderingDate;
	}

	@Override
	public void setOrderingDate(final Date orderingDate) {
		this.orderingDate = orderingDate;
	}

	@Override
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	@Override
	public void setDeliveryDate(final Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Override
	public IStore getStore() {
		if (store == null) {
			store = storeQuery.queryStore(storeName, storeLocation);
		}
		return store;
	}

	@Override
	public void setStore(final IStore store) {
		this.store = store;
	}

	@Override
	public String getStoreName() {
		return storeName;
	}

	@Override
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Override
	public String getStoreLocation() {
		return storeLocation;
	}

	@Override
	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

}
