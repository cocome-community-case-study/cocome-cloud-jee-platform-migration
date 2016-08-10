package org.cocome.cloud.web.data.store;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.backend.enterprise.IEnterpriseQuery;
import org.cocome.cloud.web.backend.store.IStoreQuery;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.util.Messages;

@Named
@ApplicationScoped
public class StorePersistence implements IStorePersistence {
	private static final Logger LOG = Logger.getLogger(StorePersistence.class);

	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	@Inject
	IStoreQuery storeQuery;

	@Override
	public String updateStore(@NotNull Store store) {
		store.updateStoreInformation();
		if (enterpriseQuery.updateStore(store)) {
			store.setEditingEnabled(false);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.getLocalizedMessage("store.update.success"), null));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.getLocalizedMessage("store.update.failed"), null));

		return "error";
	}

	@Override
	public String createStore(long enterpriseID, @NotNull String name, @NotNull String location) {
		if (enterpriseQuery.createStore(enterpriseID, name, location)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.getLocalizedMessage("store.create.success"), null));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.getLocalizedMessage("store.create.failed"), null));
		}

		return NavigationElements.SHOW_ENTERPRISES.getNavigationOutcome();
	}
	
	public boolean updateStockItem(@NotNull ProductWrapper stockItem) {
		return storeQuery.updateStockItem(stockItem.getOriginStore(), stockItem);
	}
}
