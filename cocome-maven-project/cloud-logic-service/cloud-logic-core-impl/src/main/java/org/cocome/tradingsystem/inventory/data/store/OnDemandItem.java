/*
 ***************************************************************************
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
 ***************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.store;

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Represents a concrete product in the, store including sales price,
 * amount, ...
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

@Dependent
public class OnDemandItem implements Serializable, IOnDemandItem {

	private static final long serialVersionUID = -293179135307588628L;
	private long id;

	private double salesPrice;

	private long storeId;
	
	private long productBarcode;

	private IStore store;

	private IProduct product;
	
	@Inject
	private Instance<IStoreQuery> storeQueryInstance;
	
	private IStoreQuery storeQuery;
	
	@PostConstruct
	private void init() {
		storeQuery = storeQueryInstance.get();
	}
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public IProduct getProduct() {
		if (product == null) {
			product = storeQuery.queryProductByBarcode(productBarcode);
		}
		return product;
	}

	@Override
	public void setProduct(final IProduct product) {
		this.product = product;
	}

	@Override
	public double getSalesPrice() {
		return salesPrice;
	}

	@Override
	public void setSalesPrice(final double salesPrice) {
		this.salesPrice = salesPrice;
	}

	@Override
	public IStore getStore() throws NotInDatabaseException {
		if(store == null) {
			store = storeQuery.queryStoreById(storeId);
		}
		return store;
	}

	@Override
	public void setStore(final IStore store) {
		this.store = store;
	}

	@Override
	public long getStoreId() {
		return storeId;
	}

	@Override
	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	@Override
	public long getProductBarcode() {
		return productBarcode;
	}

	@Override
	public void setProductBarcode(long productBarcode) {
		this.productBarcode = productBarcode;
	}
}
