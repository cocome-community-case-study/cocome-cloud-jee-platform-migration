package org.cocome.cloud.web.entitywrapper;

import org.cocome.logic.stub.ProductTO;


public class ProductTOWrapper {
	private ProductTO productTO;
	
	public ProductTOWrapper(ProductTO productTO) {
		this.setProductTO(productTO);
	}

	public ProductTO getProductTO() {
		return productTO;
	}

	public void setProductTO(ProductTO productTO) {
		this.productTO = productTO;
	}
	
	public String getName() {
		return productTO.getName();
	}
	
	public void settName(String name) {
		productTO.setName(name);
	}
	
	public long getBarcode() {
		return productTO.getBarcode();
	}
	
	public void setBarcode(long barcode) {
		productTO.setBarcode(barcode);
	}
	
	public long getId() {
		return productTO.getId();
	}
	
	public void setId(long id) {
		productTO.setId(id);
	}
	
	public double getPurchasePrice() {
		return productTO.getPurchasePrice();
	}
	
	public void setPurchasePrice(double price) {
		productTO.setPurchasePrice(price);
	}
}
