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

import javax.inject.Inject;

import org.cocome.tradingsystem.inventory.data.enterprise.Product;

/**
 * Represents a concrete product in the, store including sales price,
 * amount, ...
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

public class StockItem implements Serializable {

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

	private Store store;

	private Product product;
	
	@Inject
	private IStoreQueryLocal storeQuery;
	
	/**
	 * @return A unique identifier of this StockItem.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            a unique identifier of this StockItem
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return The currently available amount of items of a product
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the currently available amount of items of a product
	 */
	public void setAmount(final long amount) {
		this.amount = amount;
	}

	/**
	 * This method will be used while computing the low stock item list.
	 * 
	 * @return The maximum capacity of a product in a store.
	 */
	public long getMaxStock() {
		return maxStock;
	}

	/**
	 * This method enables the definition of the maximum capacity of a product
	 * in a store.
	 * 
	 * @param maxStock
	 *            the maximum capacity of a product in a store
	 */
	public void setMaxStock(final long maxStock) {
		this.maxStock = maxStock;
	}

	/**
	 * @return
	 *         The minimum amount of products which has to be available in a
	 *         store.
	 */
	public long getMinStock() {
		return minStock;
	}

	/**
	 * @param minStock
	 *            the minimum amount of products which has to be available in a
	 *            store
	 */
	public void setMinStock(final long minStock) {
		this.minStock = minStock;
	}

	/**
	 * @return The Product of a StockItem.
	 */
	public Product getProduct() {
		if (product == null) {
			product = storeQuery.queryProductByBarcode(productBarcode);
		}
		return product;
	}

	/**
	 * @param product
	 *            the Product of a StockItem
	 */
	public void setProduct(final Product product) {
		this.product = product;
	}

	/**
	 * @return The sales price of the StockItem
	 */
	public double getSalesPrice() {
		return salesPrice;
	}

	/**
	 * @param salesPrice
	 *            the sales price of the StockItem
	 */
	public void setSalesPrice(final double salesPrice) {
		this.salesPrice = salesPrice;
	}

	/**
	 * Required for UC 8
	 * 
	 * @return incomingAmount
	 *         the amount of products that will be delivered in the near future
	 */
	public long getIncomingAmount() {
		return this.incomingAmount;
	}

	/**
	 * Set the amount of products that will be delivered in the near future.
	 * <p>
	 * Required for UC 8
	 * 
	 * @param incomingAmount
	 *            the absolute amount (no delta) of incoming products
	 */
	public void setIncomingAmount(final long incomingAmount) {
		this.incomingAmount = incomingAmount;
	}

	/**
	 * @return The store where the StockItem belongs to
	 */
	public Store getStore() {
		if(store == null) {
			store = storeQuery.queryStore(storeName, storeLocation);
		}
		return store;
	}

	/**
	 * @param store
	 *            The store where the StockItem belongs to
	 */
	public void setStore(final Store store) {
		this.store = store;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public long getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(long productBarcode) {
		this.productBarcode = productBarcode;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

}
