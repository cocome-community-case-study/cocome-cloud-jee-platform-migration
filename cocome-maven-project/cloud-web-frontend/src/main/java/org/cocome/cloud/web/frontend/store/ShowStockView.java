package org.cocome.cloud.web.frontend.store;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.cocome.cloud.web.data.storedata.IStorePersistence;
import org.cocome.cloud.web.data.storedata.ProductWrapper;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;

@ManagedBean
@ViewScoped
public class ShowStockView {
	@Inject
	IStorePersistence storePersistence;
	
	@Inject
	StoreInformation storeInformation;
	
	public String createNewStockItem(ProductWrapper item) {
		if (storePersistence.createStockItem(storeInformation.getActiveStore(), item)) {
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
		
		if (storePersistence.updateStockItem(item.getOriginStore(), item)) {
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
}
