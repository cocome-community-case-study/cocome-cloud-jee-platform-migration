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

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
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

@Dependent
class Product implements Serializable, IProduct {
	private static final long serialVersionUID = -2577328715744776645L;

	private long id;

	private long barcode;

	private double purchasePrice;

	private String name;

	private IProductSupplier supplier;
	
	// Inject Instance here because otherwise CDI complains about 
	// missing the implementation bean when deploying
	@Inject
	Instance<IEnterpriseQueryLocal> enterpriseQueryInstance;
	
	private IEnterpriseQueryLocal enterpriseQuery;
	
	@Inject @EnterpriseRequired
	private Instance<IContextRegistry> registry;
	
	@PostConstruct
	private void initProduct() {
		enterpriseQuery = enterpriseQueryInstance.get();
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getBarcode()
	 */
	@Override
	public long getBarcode() {
		return barcode;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setBarcode(long)
	 */
	@Override
	public void setBarcode(long barcode) {
		this.barcode = barcode;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getSupplier()
	 */
	@Override
	public IProductSupplier getSupplier() throws NotInDatabaseException {
		if (supplier == null) {
			long enterpriseID = registry.get().getLong(RegistryKeys.ENTERPRISE_ID);
			supplier = enterpriseQuery.querySupplierForProduct(enterpriseID, barcode);
		}
		return supplier;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setSupplier(org.cocome.tradingsystem.inventory.data.enterprise.ProductSupplier)
	 */
	@Override
	public void setSupplier(IProductSupplier supplier) {
		this.supplier = supplier;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#getPurchasePrice()
	 */
	@Override
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProduct#setPurchasePrice(double)
	 */
	@Override
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

}
