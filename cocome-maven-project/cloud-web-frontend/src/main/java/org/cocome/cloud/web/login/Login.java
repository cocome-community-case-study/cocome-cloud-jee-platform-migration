package org.cocome.cloud.web.login;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.cloud.logic.stub.IStoreManagerService;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;


@ManagedBean
@SessionScoped
public class Login {

	@Inject
	IAuthenticator authenticator;
	
	private String username;
	private ICredential password;
	
	private boolean loggedIn = false;
	
	private static final Logger LOG = Logger.getLogger(Login.class);

	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password.getCredentialString();
	}

	public void setPassword(String password) {
		this.password = new PlainCredential(password);
	}
	
	public boolean login() {
		if (authenticator.checkCredential(username, password)) {
			setLoggedIn(true);
			return true;
		}
		return false;
	}

	public String logout() {
		this.username = null;
		this.password = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return NavigationElements.LOGIN.getNavigationOutcome();
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

}
