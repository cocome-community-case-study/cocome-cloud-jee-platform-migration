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

import org.cocome.tradingsystem.inventory.application.store.Barcode;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQueryLocal;
import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;

/**
 * Represents a single product order entry in the database.
 * 
 * @author Yannick Welsch
 */

@Dependent
class OrderEntry implements Serializable, IOrderEntry {
	private static final long serialVersionUID = -7683436740437770058L;

	private long id;

	private long amount;
	
	private Barcode productBarcode;

	private IProduct product;

//	private ProductOrder productOrder;
	
	@Inject
	Instance<IStoreQueryLocal> storeQueryInstance;
	
	private IStoreQueryLocal storeQuery;
	
	@PostConstruct
	public void initOrderEntry() {
		storeQuery = storeQueryInstance.get();
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IOrderEntry#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IOrderEntry#setId(long)
	 */
	@Override
	public void setId(final long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IOrderEntry#getAmount()
	 */
	@Override
	public long getAmount() {
		return amount;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IOrderEntry#setAmount(long)
	 */
	@Override
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
	@Override
	public IProduct getProduct() {
		if (product == null) {
			product = storeQuery.queryProductByBarcode(productBarcode);
		}
		return product;
	}

	/**
	 * @param product
	 *            The product which is ordered
	 */
	@Override
	public void setProduct(final IProduct product) {
		this.product = product;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IOrderEntry#getProductBarcode()
	 */
	@Override
	public Barcode getProductBarcode() {
		return (productBarcode == null && product != null) ? product.getBarcode() : productBarcode;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IOrderEntry#setProductBarcode(long)
	 */
	@Override
	public void setProductBarcode(Barcode productBarcode) {
		this.productBarcode = productBarcode;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.store.IOrderEntry#toString()
	 */
	@Override
	public String toString() {
		return "OrderEntry: [ID: " + id + ", product barcode: " + productBarcode + ", order amount: " + amount + "]";
	}

}
