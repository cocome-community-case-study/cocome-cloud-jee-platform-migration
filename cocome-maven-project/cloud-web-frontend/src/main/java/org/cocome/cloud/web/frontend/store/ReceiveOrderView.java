package org.cocome.cloud.web.frontend.store;

import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.cocome.cloud.web.backend.store.IStoreQuery;
import org.cocome.tradingsystem.inventory.application.store.ComplexOrderTO;

@ManagedBean
@ViewScoped
// TODO use localized messages from properties file
public class ReceiveOrderView {
	@Inject
	IStoreQuery storeQuery;
	
	@Inject
	StoreInformation storeInformation;
	
	private List<ComplexOrderTO> orders;
	
	public void loadAllOrders() {
		orders = storeQuery.getAllOrders(storeInformation.getActiveStore());
	}
	
	public void loadOrder(long orderId) {
		ComplexOrderTO order = storeQuery.getOrderByID(storeInformation.getActiveStore(), orderId);
		if (order != null) {
			orders = new LinkedList<>();
			orders.add(order);
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not find the requested order!"));
		}
	}
	
	public List<ComplexOrderTO> getOrders() {
		return orders;
	}
	
	public String rollInOrder(ComplexOrderTO order) {
		if (storeQuery.rollInOrder(storeInformation.getActiveStore(), order.getId())) {
			loadAllOrders();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Order was rolled in successfully!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error while rolling in the order!"));
		}
		return null;
	}
	
	public void validateOrderID(FacesContext context, UIComponent comp, Object value) {
		String input = (String) value;
		
		try {
			Long.parseLong(input);
		} catch (NumberFormatException e) {
			((UIInput) comp).setValid(false);
			FacesMessage wrongInputMessage = new FacesMessage("Invalid order id, please input only numbers.");
			context.addMessage(comp.getClientId(), wrongInputMessage);
		}
	}
}
