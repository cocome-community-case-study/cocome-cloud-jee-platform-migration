package org.cocome.tradingsystem.inventory.data.store;

import org.cocome.tradingsystem.inventory.data.enterprise.IProduct;

public interface IStockItem {

	/**
	 * @return A unique identifier of this StockItem.
	 */
	long getId();

	/**
	 * @param id
	 *            a unique identifier of this StockItem
	 */
	void setId(long id);

	/**
	 * @return The currently available amount of items of a product
	 */
	long getAmount();

	/**
	 * @param amount
	 *            the currently available amount of items of a product
	 */
	void setAmount(long amount);

	/**
	 * This method will be used while computing the low stock item list.
	 * 
	 * @return The maximum capacity of a product in a store.
	 */
	long getMaxStock();

	/**
	 * This method enables the definition of the maximum capacity of a product
	 * in a store.
	 * 
	 * @param maxStock
	 *            the maximum capacity of a product in a store
	 */
	void setMaxStock(long maxStock);

	/**
	 * @return
	 *         The minimum amount of products which has to be available in a
	 *         store.
	 */
	long getMinStock();

	/**
	 * @param minStock
	 *            the minimum amount of products which has to be available in a
	 *            store
	 */
	void setMinStock(long minStock);

	/**
	 * @return The Product of a StockItem.
	 */
	IProduct getProduct();

	/**
	 * @param product
	 *            the Product of a StockItem
	 */
	void setProduct(IProduct product);

	/**
	 * @return The sales price of the StockItem
	 */
	double getSalesPrice();

	/**
	 * @param salesPrice
	 *            the sales price of the StockItem
	 */
	void setSalesPrice(double salesPrice);

	/**
	 * Required for UC 8
	 * 
	 * @return incomingAmount
	 *         the amount of products that will be delivered in the near future
	 */
	long getIncomingAmount();

	/**
	 * Set the amount of products that will be delivered in the near future.
	 * <p>
	 * Required for UC 8
	 * 
	 * @param incomingAmount
	 *            the absolute amount (no delta) of incoming products
	 */
	void setIncomingAmount(long incomingAmount);

	/**
	 * @return The store where the StockItem belongs to
	 */
	IStore getStore();

	/**
	 * @param store
	 *            The store where the StockItem belongs to
	 */
	void setStore(IStore store);

	String getStoreName();

	void setStoreName(String storeName);

	edu.kit.ipd.sdq.evaluation.Barcode getProductBarcode();

	void setProductBarcode(edu.kit.ipd.sdq.evaluation.Barcode productBarcode);

	String getStoreLocation();

	void setStoreLocation(String storeLocation);

}