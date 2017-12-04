package org.cocome.cloud.web.data.enterprisedata;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.IEnterpriseQuery;

@Named
@ApplicationScoped
public class EnterprisePersistence implements IEnterprisePersistence {

	@Inject
	private IEnterpriseQuery enterpriseQuery;

	@Override
	public String createEnterprise(String name) throws NotInDatabaseException_Exception {
		// TODO: Check if name is ok and doesn't include illegal characters
		if (enterpriseQuery.createEnterprise(name)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully created the new enterprise!", null));
			return "show_enterprises";
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating the new enterprise!", null));
		return null;
	}

	@Override
	public String updateEnterprise(EnterpriseViewData enterprise) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateProduct(String name, long barcode, double purchasePrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createProduct(String name, long barcode, double purchasePrice) throws NotInDatabaseException_Exception {
		// TODO: Check if name is ok and doesn't include illegal characters
		if (enterpriseQuery.createProduct(name, barcode, purchasePrice)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully created the new product!", null));
			return "show_products";
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating the new product!", null));
		return null;
	}

}
