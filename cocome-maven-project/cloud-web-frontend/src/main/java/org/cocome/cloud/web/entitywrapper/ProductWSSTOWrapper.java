package org.cocome.cloud.web.entitywrapper;

import org.cocome.logic.stub.ProductWithSupplierAndStockItemTO;
import org.cocome.logic.stub.StockItemTO;
import org.cocome.logic.stub.SupplierTO;


public class ProductWSSTOWrapper extends ProductTOWrapper {

	private ProductWithSupplierAndStockItemTO productWSSTO;
	
	private String newPrice;
	
	private boolean editingEnabled;
	
	private long orderAmount;
	
	public ProductWSSTOWrapper(ProductWithSupplierAndStockItemTO stockItem) {
		super(stockItem);
		setNewPrice("0.0");
		setEditingEnabled(false);
		this.setProductTO(stockItem);
	}

	public ProductWSSTOWrapper() {
		this(new ProductWithSupplierAndStockItemTO());
		this.getProductTO().setStockItemTO(new StockItemTO());
		this.getProductTO().setSupplierTO(new SupplierTO());
	}

	public ProductWithSupplierAndStockItemTO getProductTO() {
		return productWSSTO;
	}
	
	public void setProductTO(ProductWithSupplierAndStockItemTO productTO) {
		this.productWSSTO = productTO;
	}
	
	public StockItemTO getStockItemTO() {
		return productWSSTO.getStockItemTO();
	}

	public void setStockItemTO(StockItemTO stockItemTO) {
		this.productWSSTO.setStockItemTO(stockItemTO);
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public boolean isEditingEnabled() {
		return editingEnabled;
	}

	public void setEditingEnabled(boolean editingEnabled) {
		this.editingEnabled = editingEnabled;
	}

	public long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(long orderAmount) {
		this.orderAmount = orderAmount;
	}
	
}
