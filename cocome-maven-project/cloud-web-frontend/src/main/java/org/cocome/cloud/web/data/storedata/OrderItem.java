package org.cocome.cloud.web.data.storedata;

import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;

public class OrderItem {
	private ProductWrapper product;
	private int amount;

	public OrderItem(ProductWrapper product, int amount) {
		this.setProduct(product);
		this.setAmount(amount);
	}

	public ProductWrapper getProduct() {
		return product;
	}

	public void setProduct(ProductWrapper product) {
		this.product = product;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public static ComplexOrderEntryTO convertToOrderEntryTO(OrderItem item) {
		ComplexOrderEntryTO orderTO = new ComplexOrderEntryTO();
		orderTO.setAmount(item.amount);
		orderTO.setProductTO(ProductWrapper.convertToProductTO(item.product));
		return orderTO;
	}
}
