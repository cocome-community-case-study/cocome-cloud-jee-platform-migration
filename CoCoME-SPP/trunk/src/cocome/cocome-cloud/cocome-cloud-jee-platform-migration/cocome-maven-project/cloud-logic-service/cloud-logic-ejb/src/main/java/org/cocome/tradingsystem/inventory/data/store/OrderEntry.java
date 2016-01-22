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
 * Represents a single product order entry in the database.
 * 
 * @author Yannick Welsch
 */

public class OrderEntry implements Serializable {
	private static final long serialVersionUID = -7683436740437770058L;

	private long id;

	private long amount;
	
	private long productBarcode;

	private Product product;

//	private ProductOrder productOrder;
	
	@Inject
	private IStoreQueryLocal storeQuery;
	
	public OrderEntry() {
	}

	/**
	 * Gets identifier value
	 * 
	 * @return The id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets identifier.
	 * 
	 * @param id
	 *            Identifier value.
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return The amount of ordered products
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            The amount of ordered products
	 */
	public void setAmount(final long amount) {
		this.amount = amount;
	}

//	/**
//	 * @return The ProductOrder where the OrderEntry belongs to
//	 */
//	public ProductOrder getOrder() {
//		if (productOrder == null) {
//			productOrder = storeQuery.queryProductOrder(
//					registry.getLong(RegistryKeys.STORE_ID), 
//					productBarcode, amount);
//		}
//		return productOrder;
//	}
//
//	/**
//	 * @param productOrder
//	 *            The ProductOrder where the OrderEntry belongs to
//	 */
//	public void setOrder(final ProductOrder productOrder) {
//		this.productOrder = productOrder;
//	}

	/**
	 * @return The product which is ordered
	 */
	public Product getProduct() {
		if (product == null) {
			product = storeQuery.queryProductByBarcode(productBarcode);
		}
		return product;
	}

	/**
	 * @param product
	 *            The product which is ordered
	 */
	public void setProduct(final Product product) {
		this.product = product;
	}

	public long getProductBarcode() {
		return (productBarcode == 0 && product != null) ? product.getBarcode() : productBarcode;
	}

	public void setProductBarcode(long productBarcode) {
		this.productBarcode = productBarcode;
	}
	
	@Override
	public String toString() {
		return "OrderEntry: [ID: " + id + ", product barcode: " + productBarcode + ", order amount: " + amount + "]";
	}

}
