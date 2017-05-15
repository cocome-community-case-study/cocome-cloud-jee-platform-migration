package org.cocome.tradingsystem.inventory.data.enterprise;

import java.util.Collection;

import org.cocome.tradingsystem.inventory.data.store.IStore;

public interface ITradingEnterprise {

	void initTradingEnterprise();

	/**
	 * @return id a unique identifier of this TradingEnterprise
	 */
	long getId();

	/**
	 * @param id
	 *            a unique identifier of this TradingEnterprise
	 */
	void setId(long id);

	/**
	 * @return Name of this TradingEnterprise
	 */
	String getName();

	/**
	 * @param name
	 *            Name of this TradingEnterprise
	 */
	void setName(String name);

	/**
	 * @return Collection of Stores related to the TradingEnterprise
	 */
	Collection<IStore> getStores();

	/**
	 * @param stores
	 *            Collection of Stores related to the TradingEnterprise
	 */
	void setStores(Collection<IStore> stores);

	/**
	 * @return Collection of Suppliers related to the TradingEnterprise
	 */
	Collection<IProductSupplier> getSuppliers();

	/**
	 * @param suppliers
	 *            Collection of Suppliers related to the TradingEnterprise
	 */
	void setSuppliers(Collection<IProductSupplier> suppliers);

}