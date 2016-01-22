package org.cocome.cloud.web.usecase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.cocome.logic.stub.ComplexOrderEntryTO;

public class OrderItemList {
	private HashMap<Long, ComplexOrderEntryTO> orderItems = new HashMap<Long, ComplexOrderEntryTO>();
	
	public List<ComplexOrderEntryTO> getOrderItems() {
		return new ArrayList<ComplexOrderEntryTO>(orderItems.values());
	}
	
	public void setOrderEntries(List<ComplexOrderEntryTO> orderEntries) {
		orderItems.clear();
		for (ComplexOrderEntryTO entry : orderEntries) {
			orderItems.put(entry.getProductTO().getBarcode(), entry);
		}
	}
	
	public ComplexOrderEntryTO getOrderByBarcode(long productBarcode) {
		return orderItems.get(productBarcode);
	}
	
	public void setOrderWithBarcode(long productBarcode, ComplexOrderEntryTO order) {
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
