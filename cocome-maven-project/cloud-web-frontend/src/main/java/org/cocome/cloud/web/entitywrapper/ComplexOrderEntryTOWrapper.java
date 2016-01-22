package org.cocome.cloud.web.entitywrapper;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.cocome.logic.stub.ComplexOrderEntryTO;
import org.cocome.logic.stub.ComplexOrderTO;
import org.cocome.logic.stub.ProductWithSupplierTO;

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
	
	public XMLGregorianCalendar getOrderingDate() {
		return containingOrder.getOrderingDate();
	}
	
	public XMLGregorianCalendar getDeliveryDate() {
		return containingOrder.getDeliveryDate();
	}
	
	public ComplexOrderEntryTO getOrderTO() {
		return orderEntry;
	}
	
	public boolean isArrived() {
		int comparison = containingOrder.getDeliveryDate().compare(containingOrder.getOrderingDate());
		return comparison == DatatypeConstants.GREATER || comparison == DatatypeConstants.EQUAL;
	}
	
}
