package org.cocome.tradingsystem.inventory.data.enterprise;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IProductSupplier {

	void initProductSupplier();

	/**
	 * @return A unique identifier for ProductSupplier objects
	 */
	long getId();

	/**
	 * @param id
	 *            A unique identifier for ProductSupplier objects
	 */
	void setId(long id);

	/**
	 * @return The name of the ProductSupplier
	 */
	String getName();

	/**
	 * @param name
	 *            The name of the ProductSupplier
	 */
	void setName(String name);

	/**
	 * @return The list of Products provided by the ProductSupplier
	 * @throws NotInDatabaseException 
	 */
	Collection<IProduct> getProducts() throws NotInDatabaseException;

	/**
	 * @param products
	 *            The list of Products provided by the ProductSupplier
	 */
	void setProducts(Collection<IProduct> products);

}