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
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.qualifier.EnterpriseRequired;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

/**
 * This class represents a ProductSupplier in the database.
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

@Dependent
public class ProductSupplier implements Serializable, IProductSupplier {
	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private Collection<IProduct> products;
	
	@Inject
	Instance<IEnterpriseQuery> enterpriseQueryInstance;
	
	private IEnterpriseQuery enterpriseQuery;
	
	@Inject @EnterpriseRequired
	private Instance<IContextRegistry> registry;
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier#initProductSupplier()
	 */
	@PostConstruct
	public void initProductSupplier() {
		enterpriseQuery = enterpriseQueryInstance.get();
		products = null;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier#getProducts()
	 */
	@Override
	public Collection<IProduct> getProducts() throws NotInDatabaseException {
		if (products == null) {
			long enterpriseID = registry.get().getLong(RegistryKeys.ENTERPRISE_ID);
			products = enterpriseQuery.queryProductsBySupplier(enterpriseID, id);
		}
		return products;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier#setProducts(java.util.Collection)
	 */
	@Override
	public void setProducts(Collection<IProduct> products) {
		this.products = products;
	}

}
