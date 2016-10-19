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

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;

/**
 * Represents a concrete product in the, store including sales price,
 * amount, ...
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

@Dependent
class StockItem implements Serializable, IStockItem {

	private static final long serialVersionUID = -293179135307588628L;
	private long id;

	private double salesPrice;

	private long amount;

	private long minStock;

	private long maxStock;

	private long incomingAmount;
	
	private String storeName;
	
	private String storeLocation;
	
	private long productBarcode;

	private IStore store;

	private IProduct product;
	
	@Inject
	Instance<IStoreQuery> storeQueryInstance;
	
	private IStoreQuery storeQuery;
	
	@PostConstruct
	private void initStockItem() {
		storeQuery = storeQueryInstance.get();
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setId(long)
	 */
	@Override
	public void setId(final long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getAmount()
	 */
	@Override
	public long getAmount() {
		return amount;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setAmount(long)
	 */
	@Override
	public void setAmount(final long amount) {
		this.amount = amount;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getMaxStock()
	 */
	@Override
	public long getMaxStock() {
		return maxStock;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setMaxStock(long)
	 */
	@Override
	public void setMaxStock(final long maxStock) {
		this.maxStock = maxStock;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getMinStock()
	 */
	@Override
	public long getMinStock() {
		return minStock;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setMinStock(long)
	 */
	@Override
	public void setMinStock(final long minStock) {
		this.minStock = minStock;
	}

	/**
	 * @return The Product of a StockItem.
	 */
	@Override
	public IProduct getProduct() {
		if (product == null) {
			product = storeQuery.queryProductByBarcode(productBarcode);
		}
		return product;
	}

	/**
	 * @param product
	 *            the Product of a StockItem
	 */
	@Override
	public void setProduct(final IProduct product) {
		this.product = product;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getSalesPrice()
	 */
	@Override
	public double getSalesPrice() {
		return salesPrice;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setSalesPrice(double)
	 */
	@Override
	public void setSalesPrice(final double salesPrice) {
		this.salesPrice = salesPrice;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getIncomingAmount()
	 */
	@Override
	public long getIncomingAmount() {
		return this.incomingAmount;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setIncomingAmount(long)
	 */
	@Override
	public void setIncomingAmount(final long incomingAmount) {
		this.incomingAmount = incomingAmount;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getStore()
	 */
	@Override
	public IStore getStore() {
		if(store == null) {
			store = storeQuery.queryStore(storeName, storeLocation);
		}
		return store;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setStore(org.cocome.tradingsystem.inventory.data.store.Store)
	 */
	@Override
	public void setStore(final IStore store) {
		this.store = store;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getStoreName()
	 */
	@Override
	public String getStoreName() {
		return storeName;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setStoreName(java.lang.String)
	 */
	@Override
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getProductBarcode()
	 */
	@Override
	public long getProductBarcode() {
		return productBarcode;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setProductBarcode(long)
	 */
	@Override
	public void setProductBarcode(long productBarcode) {
		this.productBarcode = productBarcode;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#getStoreLocation()
	 */
	@Override
	public String getStoreLocation() {
		return storeLocation;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IStockItem#setStoreLocation(java.lang.String)
	 */
	@Override
	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

}
