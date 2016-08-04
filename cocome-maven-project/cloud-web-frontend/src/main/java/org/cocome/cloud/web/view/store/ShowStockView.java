package org.cocome.cloud.web.view.store;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.inventory.connection.IEnterpriseQuery;
import org.cocome.cloud.web.inventory.connection.IStoreQuery;
import org.cocome.cloud.web.inventory.store.IStoreInformation;
import org.cocome.cloud.web.inventory.store.ProductWrapper;
import org.cocome.cloud.web.util.Messages;

@ManagedBean
@ViewScoped
public class ShowStockView {
	@Inject
	IStoreQuery storeQuery;
	
	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	@Inject
	IStoreInformation storeInformation;
	
	public String createNewStockItem(ProductWrapper item) {
		if (storeQuery.createStockItem(storeInformation.getActiveStore(), item)) {
			storeInformation.queryStockItems();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					Messages.getLocalizedMessage("stock.add.success"), null));
			return NavigationElements.SHOW_STOCK.getNavigationOutcome();
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Messages.getLocalizedMessage("stock.add.failed"), null));
		}
		return null;
	}
	
	public String updateStockItem(ProductWrapper item) {
		if (validateStockItem(item)) {
			item.submitEdit();
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Messages.getLocalizedMessage("stock.validation.amount.failed"), null));
			return null;
		}
		
		if (storeQuery.updateStockItem(item.getOriginStore(), item)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.getLocalizedMessage("stock.update.success"), null));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.getLocalizedMessage("stock.update.failed"), null));
		}
		return null;
	}
	
	private boolean validateStockItem(ProductWrapper item) {
		if (item.getNewMaxAmount() < item.getNewMinAmount()) {
			return false;
		}
		return true;		
	}
	
	public void validatePositiveNumber(FacesContext context, UIComponent comp, Object value) {
		Long number = (Long) value;

		if (number < 0) {
			((UIInput) comp).setValid(false);
			FacesMessage wrongInputMessage = new FacesMessage("Invalid number, please input only positive numbers.");
			context.addMessage(comp.getClientId(), wrongInputMessage);
			return;
		}
	}
}
