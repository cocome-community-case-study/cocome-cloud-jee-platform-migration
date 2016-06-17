package org.cocome.cloud.web.login;

import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import org.apache.log4j.Logger;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.events.LogoutEvent;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;


@ManagedBean
@SessionScoped
public class Login {

	@Inject
	IAuthenticator authenticator;
	
	@Inject
	Event<LoginEvent> loginEvent;
	
	@Inject
	Event<LogoutEvent> logoutEvent;
	
	private String username = "";
	private ICredential password = new PlainCredential("");
	private NavigationViewStates requestedView = NavigationViewStates.ENTERPRISE_VIEW;
	private long requestedStoreId;
	
	private boolean loggedIn = false;
	
	private static final Logger LOG = Logger.getLogger(Login.class);

	public String getUserName() {
		return username;
	}

	public void setUserName(@NotNull String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password.getCredentialString();
	}

	public void setPassword(@NotNull String password) {
		this.password = new PlainCredential(password);
	}
	
	public String login() {
		IUser storedUser = authenticator.checkCredential(username, password); 
		if (storedUser != null) {
			setLoggedIn(true);
			loginEvent.fire(new LoginEvent(storedUser, requestedView));
			LOG.info(String.format("Successful login: username %s.", getUserName()));
			return NavigationElements.MAIN_PAGE.getNavigationOutcome();
		}
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, 
						"#{strings['login.failed.text']}", null));
		LOG.warn(String.format("Failed login: username %s.", getUserName()));
		return NavigationElements.LOGIN.getNavigationOutcome();
	}

	public String logout() {
		this.username = "";
		this.password = new PlainCredential("");
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return NavigationElements.LOGIN.getNavigationOutcome();
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public NavigationViewStates getRequestedView() {
		return requestedView;
	}

	public void setRequestedView(NavigationViewStates requestedView) {
		this.requestedView = requestedView;
	}

	public long getRequestedStoreId() {
		return requestedStoreId;
	}

	public void setRequestedStoreId(long requestedStoreId) {
		this.requestedStoreId = requestedStoreId;
	}

	public boolean isStoreRequired() {
		if (requestedView != NavigationViewStates.ENTERPRISE_VIEW) {
			return true;
		}
		return false;
	}
}
