package org.cocome.cloud.web.view.store;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.cocome.cloud.web.inventory.connection.IStoreQuery;
import org.cocome.cloud.web.inventory.store.IStoreInformation;
import org.cocome.cloud.web.inventory.store.ProductWrapper;

@ManagedBean
@ViewScoped
public class ShowReportView {
	private long storeId;
	private List<ProductWrapper> stockItems;
	
	@Inject
	IStoreInformation storeInformation;

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}
	
	public void loadReport(long storeId) {
		stockItems = storeInformation.getStockReport(storeId);
	}

	public List<ProductWrapper> getStockItems() {
		return stockItems;
	}
	
	public void validateStoreID(FacesContext context, UIComponent comp, Object value) {
		String input = (String) value;
		
		try {
			Long.parseLong(input);
		} catch (NumberFormatException e) {
			((UIInput) comp).setValid(false);
			FacesMessage wrongInputMessage = new FacesMessage("Invalid store id, please input only numbers.");
			context.addMessage(comp.getClientId(), wrongInputMessage);
		}	
	}
}
