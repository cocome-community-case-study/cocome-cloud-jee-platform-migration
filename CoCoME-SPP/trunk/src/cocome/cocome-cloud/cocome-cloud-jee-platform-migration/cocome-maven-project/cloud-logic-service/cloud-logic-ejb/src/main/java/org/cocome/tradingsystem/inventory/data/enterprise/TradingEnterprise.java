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
import javax.inject.Inject;
import javax.persistence.Basic;

import org.cocome.tradingsystem.inventory.data.store.Store;

/**
 * Represents a TradingEnterprise in the database.
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

public class TradingEnterprise implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private Collection<ProductSupplier> suppliers;

	private Collection<Store> stores;
	
	@Inject 
	private IEnterpriseQueryLocal enterpriseQuery;
	
	
	@PostConstruct
	public void initTradingEnterprise() {
		suppliers = null;
		stores = null;
	}

	/**
	 * @return id a unique identifier of this TradingEnterprise
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            a unique identifier of this TradingEnterprise
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return Name of this TradingEnterprise
	 */
	@Basic
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Name of this TradingEnterprise
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Collection of Stores related to the TradingEnterprise
	 */
	public Collection<Store> getStores() {
		if (stores == null) {
			stores = enterpriseQuery.queryStoresByEnterpriseId(id);
		}
		return stores;
	}

	/**
	 * @param stores
	 *            Collection of Stores related to the TradingEnterprise
	 */
	public void setStores(Collection<Store> stores) {
		this.stores = stores;
	}

	/**
	 * @return Collection of Suppliers related to the TradingEnterprise
	 */
	public Collection<ProductSupplier> getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            Collection of Suppliers related to the TradingEnterprise
	 */
	public void setSuppliers(Collection<ProductSupplier> suppliers) {
		this.suppliers = suppliers;
	}

}
