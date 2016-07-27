package org.cocome.cloud.web.inventory.store;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.inventory.connection.IEnterpriseQuery;
import org.cocome.cloud.web.inventory.connection.IStoreQuery;
import org.cocome.cloud.web.inventory.enterprise.IEnterpriseInformation;

@Named
@ApplicationScoped
public class StorePersistence implements IStorePersistence {
	private static final Logger LOG = Logger.getLogger(StorePersistence.class);

	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	@Inject
	IStoreQuery storeQuery;

	private String getLocalizedMessage(@NotNull String key) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Locale locale = ctx.getViewRoot().getLocale();

		ResourceBundle strings = ResourceBundle.getBundle("cocome.frontend.Strings", locale);

		return strings.getString(key);
	}

	@Override
	public String updateStore(@NotNull Store store) {
		store.updateStoreInformation();
		if (enterpriseQuery.updateStore(store)) {
			store.setEditingEnabled(false);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, getLocalizedMessage("store.update.success"), null));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, getLocalizedMessage("store.update.failed"), null));

		return "error";
	}

	@Override
	public String createStore(long enterpriseID, @NotNull String name, @NotNull String location) {
		if (enterpriseQuery.createStore(enterpriseID, name, location)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, getLocalizedMessage("store.create.success"), null));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, getLocalizedMessage("store.create.failed"), null));
		}

		return NavigationElements.SHOW_ENTERPRISES.getNavigationOutcome();
	}
	
	public String updateStockItemSalesPrice(@NotNull ProductWrapper stockItem) {
		stockItem.submitSalesPrice();
		if (storeQuery.updateStockItem(stockItem.getOriginStore(), stockItem)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, getLocalizedMessage("stock.update.success"), null));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, getLocalizedMessage("stock.update.failed"), null));
		}
		return NavigationElements.SHOW_STOCK.getNavigationOutcome();
	}
}
