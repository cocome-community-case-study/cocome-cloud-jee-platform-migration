package org.cocome.cloud.web.usecase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.cocome.tradingsystem.inventory.application.store.Barcode;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderEntryTO;


public class OrderItemList {
	private HashMap<Barcode, ComplexOrderEntryTO> orderItems = new HashMap<Barcode, ComplexOrderEntryTO>();
	
	public List<ComplexOrderEntryTO> getOrderItems() {
		return new ArrayList<ComplexOrderEntryTO>(orderItems.values());
	}
	
	public void setOrderEntries(List<ComplexOrderEntryTO> orderEntries) {
		orderItems.clear();
		for (ComplexOrderEntryTO entry : orderEntries) {
			orderItems.put(entry.getProductTO().getBarcode(), entry);
		}
	}
	
	public ComplexOrderEntryTO getOrderByBarcode(Barcode productBarcode) {
		return orderItems.get(productBarcode);
	}
	
	public void setOrderWithBarcode(Barcode productBarcode, ComplexOrderEntryTO order) {
		orderItems.put(productBarcode, order);
	}
	
	public void insertOrders(Collection<ComplexOrderEntryTO> orders) {
		for (ComplexOrderEntryTO order : orders) {
			this.setOrderWithBarcode(order.getProductTO().getBarcode(), order);
		}
	}
	
	public void clear() {
		orderItems.clear();
	}
}
