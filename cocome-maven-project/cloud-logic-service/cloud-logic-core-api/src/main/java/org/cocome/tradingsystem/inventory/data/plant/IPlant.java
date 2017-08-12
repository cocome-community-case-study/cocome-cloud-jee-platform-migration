package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IPlant {

	void initPlant();

	/**
	 * @return A unique identifier for Store objects
	 */
	long getId();

	/**
	 * @param id
	 *            A unique identifier for Store objects
	 */
	void setId(long id);

	/**
	 * Returns the name of the store.
	 * 
	 * @return Store name.
	 */
	String getName();

	/**
	 * @param name
	 *            the name of the Store
	 */
	void setName(String name);

	/**
	 * Returns the location of the store.
	 * 
	 * @return Store location.
	 */
	String getLocation();

	/**
	 * Sets the location of the store.
	 * 
	 * @param location
	 *            store location
	 */
	void setLocation(String location);

	/**
	 * @return The enterprise which the Store belongs to
	 */
	ITradingEnterprise getEnterprise() throws NotInDatabaseException;

	/**
	 * @param enterprise
	 *            The enterprise which the Store belongs to
	 */
	void setEnterprise(ITradingEnterprise enterprise);

	String toString();

	int compareTo(IPlant o);

	long getEnterpriseId();

	void setEnterpriseId(long enterpriseId);

}