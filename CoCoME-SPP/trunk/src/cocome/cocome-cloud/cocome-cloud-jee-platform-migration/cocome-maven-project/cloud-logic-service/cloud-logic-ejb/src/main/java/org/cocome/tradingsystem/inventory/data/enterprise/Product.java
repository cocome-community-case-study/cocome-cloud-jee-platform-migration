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

package org.cocome.tradingsystem.inventory.data.enterprise;

import java.io.Serializable;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.qualifier.EnterpriseRequired;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

/**
 * This class represents a Product in the database
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

public class Product implements Serializable {
	private static final long serialVersionUID = -2577328715744776645L;

	private long id;

	private long barcode;

	private double purchasePrice;

	private String name;

	private ProductSupplier supplier;
	
	@Inject
	private IEnterpriseQueryLocal enterpriseQuery;
	
	@Inject @EnterpriseRequired
	private Instance<IContextRegistry> registry;

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
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return The barcode of the product
	 */
	public long getBarcode() {
		return barcode;
	}

	/**
	 * @param barcode
	 *            The barcode of the product
	 */
	public void setBarcode(long barcode) {
		this.barcode = barcode;
	}

	/**
	 * @return The name of the product
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name of the product
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The ProductSupplier of this product
	 * @throws NotInDatabaseException 
	 */
	public ProductSupplier getSupplier() throws NotInDatabaseException {
		if (supplier == null) {
			long enterpriseID = registry.get().getLong(RegistryKeys.ENTERPRISE_ID);
			supplier = enterpriseQuery.querySupplierForProduct(enterpriseID, barcode);
		}
		return supplier;
	}

	/**
	 * @param supplier
	 *            The ProductSupplier of this product
	 */
	public void setSupplier(ProductSupplier supplier) {
		this.supplier = supplier;
	}

	/**
	 * @return The purchase price of this product
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice
	 *            The purchase price of this product
	 */
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

}
