package org.cocome.tradingsystem.inventory.data.store;

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;

public interface IOrderEntry {

	/**
	 * Gets identifier value
	 * 
	 * @return The id.
	 */
	long getId();

	/**
	 * Sets identifier.
	 * 
	 * @param id
	 *            Identifier value.
	 */
	void setId(long id);

	/**
	 * @return The amount of ordered products
	 */
	long getAmount();

	/**
	 * @param amount
	 *            The amount of ordered products
	 */
	void setAmount(long amount);

	/**
	 * @return The product which is ordered
	 */
	IProduct getProduct();

	/**
	 * @param product
	 *            The product which is ordered
	 */
	void setProduct(IProduct product);

	long getProductBarcode();

	void setProductBarcode(long productBarcode);

	String toString();

}