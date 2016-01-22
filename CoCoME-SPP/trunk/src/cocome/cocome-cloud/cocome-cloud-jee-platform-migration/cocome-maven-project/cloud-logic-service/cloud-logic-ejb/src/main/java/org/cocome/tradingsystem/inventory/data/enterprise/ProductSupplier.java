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

public class ProductSupplier implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private Collection<Product> products;
	
	@Inject
	private IEnterpriseQueryLocal enterpriseQuery;
	
	@Inject @EnterpriseRequired
	private Instance<IContextRegistry> registry;
	
	@PostConstruct
	public void initProductSupplier() {
		products = null;
	}

	/**
	 * @return A unique identifier for ProductSupplier objects
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            A unique identifier for ProductSupplier objects
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return The name of the ProductSupplier
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name of the ProductSupplier
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The list of Products provided by the ProductSupplier
	 * @throws NotInDatabaseException 
	 */
	public Collection<Product> getProducts() throws NotInDatabaseException {
		if (products == null) {
			long enterpriseID = registry.get().getLong(RegistryKeys.ENTERPRISE_ID);
			products = enterpriseQuery.queryProductsBySupplier(enterpriseID, id);
		}
		return products;
	}

	/**
	 * @param products
	 *            The list of Products provided by the ProductSupplier
	 */
	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

}
