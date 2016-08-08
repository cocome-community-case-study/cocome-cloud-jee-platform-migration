package org.cocome.cloud.web.frontend.login;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import org.apache.log4j.Logger;
import org.cocome.cloud.web.data.login.IAuthenticator;
import org.cocome.cloud.web.data.login.ICredential;
import org.cocome.cloud.web.data.login.IUser;
import org.cocome.cloud.web.data.login.PlainCredential;
import org.cocome.cloud.web.data.login.UserRole;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.events.LogoutEvent;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;
import org.cocome.cloud.web.frontend.store.StoreInformation;

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
	private UserRole requestedRole = UserRole.ENTERPRISE_MANAGER;

	private IUser user = null;

	private long requestedStoreId = StoreInformation.STORE_ID_NOT_SET;

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
		String outcome;

		if (storedUser != null) {
			setLoggedIn(true);
			user = storedUser;
			loginEvent.fire(new LoginEvent(storedUser, requestedRole, requestedStoreId));
			LOG.info(String.format("Successful login: username %s.", getUserName()));
			outcome = isStoreRequired() ? NavigationElements.STORE_MAIN.getNavigationOutcome()
					: NavigationElements.ENTERPRISE_MAIN.getNavigationOutcome();
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			String message = context.getApplication().evaluateExpressionGet(context, "#{strings['login.failed.text']}",
					String.class);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
			outcome = NavigationElements.LOGIN.getNavigationOutcome();
			LOG.warn(String.format("Failed login: username %s.", getUserName()));
		}
		return outcome;
	}

	public String logout() {
		username = "";
		password = new PlainCredential("");
		requestedRole = UserRole.ENTERPRISE_MANAGER;
		requestedStoreId = 0;

		logoutEvent.fire(new LogoutEvent(user));
		user = null;

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return NavigationElements.LOGIN.getNavigationOutcome();
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public long getRequestedStoreId() {
		return requestedStoreId == StoreInformation.STORE_ID_NOT_SET ? 0 : requestedStoreId;
	}

	public void setRequestedStoreId(long requestedStoreId) {
		this.requestedStoreId = requestedStoreId;
	}

	public UserRole getRequestedRole() {
		return requestedRole;
	}

	public void setRequestedRole(UserRole requestedRole) {
		this.requestedRole = requestedRole;
	}

	public boolean isStoreRequired() {
		if (requestedRole.associatedView() != NavigationViewStates.ENTERPRISE_VIEW) {
			return true;
		}
		return false;
	}

	@Produces
	@javax.enterprise.context.SessionScoped
	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}
}
