package org.cocome.cloud.web.entitywrapper;

import java.util.Date;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;

public class ComplexOrderEntryTOWrapper {

	private ComplexOrderEntryTO orderEntry;
	
	private ComplexOrderTO containingOrder;
	
	public ComplexOrderTO getContainingOrder() {
		return containingOrder;
	}

	public void setContainingOrder(ComplexOrderTO containingOrder) {
		this.containingOrder = containingOrder;
	}

	public ComplexOrderEntryTOWrapper(ComplexOrderEntryTO orderEntry, ComplexOrderTO containingOrder) {
		this.orderEntry = orderEntry;
		this.containingOrder = containingOrder;
	}
	
	public ProductWithSupplierTO getProductTO() {
		return orderEntry.getProductTO();
	}
	
	public void setProductTO(ProductWithSupplierTO productTO) {
		orderEntry.setProductTO(productTO);
	}
	
	public Date getOrderingDate() {
		return containingOrder.getOrderingDate();
	}
	
	public Date getDeliveryDate() {
		return containingOrder.getDeliveryDate();
	}
	
	public ComplexOrderEntryTO getOrderTO() {
		return orderEntry;
	}
	
	public boolean isArrived() {
		int comparison = containingOrder.getDeliveryDate().compareTo(containingOrder.getOrderingDate());
		return comparison == DatatypeConstants.GREATER || comparison == DatatypeConstants.EQUAL;
	}
	
}
