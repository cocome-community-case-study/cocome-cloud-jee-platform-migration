package org.cocome.cloud.web.inventory.store;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.inventory.connection.IEnterpriseQuery;
import org.cocome.cloud.web.inventory.enterprise.IEnterpriseInformation;

@Named
@ApplicationScoped
public class StorePersistence implements IStorePersistence {
	private static final Logger LOG = Logger.getLogger(StorePersistence.class);
	
	@Inject
	IEnterpriseQuery enterpriseQuery;
	
	@Override
	public String updateStore(Store store) {
		store.updateStoreInformation();
		if (enterpriseQuery.updateStore(store)) {
			store.setEditingEnabled(false);
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while updating the store!", null));
		
		return "error";
	}

}
