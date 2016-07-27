package org.cocome.cloud.web.view.store;

import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.cocome.cloud.web.inventory.connection.IStoreQuery;
import org.cocome.cloud.web.inventory.store.IStoreInformation;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

@ManagedBean
@ViewScoped
// TODO add error and success faces messages
public class ReceiveOrderView {
	@Inject
	IStoreQuery storeQuery;
	
	@Inject
	IStoreInformation storeInformation;
	
	private List<ComplexOrderTO> orders;
	
	public void loadAllOrders() {
		orders = storeQuery.getAllOrders(storeInformation.getActiveStore());
	}
	
	public void loadOrder(long orderId) {
		ComplexOrderTO order = storeQuery.getOrderByID(storeInformation.getActiveStore(), orderId);
		if (order != null) {
			orders = new LinkedList<>();
			orders.add(order);
		}
	}
	
	public List<ComplexOrderTO> getOrders() {
		return orders;
	}
	
	public String rollInOrder(ComplexOrderTO order) {
		storeQuery.rollInOrder(storeInformation.getActiveStore(), order.getId());
		loadAllOrders();
		return null;
	}
}
