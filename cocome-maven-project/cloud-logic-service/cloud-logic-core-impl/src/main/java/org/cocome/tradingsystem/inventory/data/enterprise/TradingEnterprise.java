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
import javax.persistence.Basic;

import org.cocome.tradingsystem.inventory.data.store.IStore;

/**
 * Represents a TradingEnterprise in the database.
 * 
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */

@Dependent
public class TradingEnterprise implements Serializable, ITradingEnterprise {
	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private Collection<IProductSupplier> suppliers;

	private Collection<IStore> stores;
	
	@Inject
	private Instance<IEnterpriseQuery> enterpriseQueryInstance;
	
	private IEnterpriseQuery enterpriseQuery;
	
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise#initTradingEnterprise()
	 */
	@PostConstruct
	public void initTradingEnterprise() {
		enterpriseQuery = enterpriseQueryInstance.get();
		suppliers = null;
		stores = null;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise#getName()
	 */
	@Override
	@Basic
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Collection of Stores related to the TradingEnterprise
	 */
	@Override
	public Collection<IStore> getStores() {
		if (stores == null) {
			stores = enterpriseQuery.queryStoresByEnterpriseId(id);
		}
		return stores;
	}

	/**
	 * @param stores
	 *            Collection of Stores related to the TradingEnterprise
	 */
	@Override
	public void setStores(Collection<IStore> stores) {
		this.stores = stores;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise#getSuppliers()
	 */
	@Override
	public Collection<IProductSupplier> getSuppliers() {
		return suppliers;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise#setSuppliers(java.util.Collection)
	 */
	@Override
	public void setSuppliers(Collection<IProductSupplier> suppliers) {
		this.suppliers = suppliers;
	}
	
	@Override
	public String toString() {
		return String.format("TradingEnterprise[id: %d, name: %s]", id, name);
	}

}
