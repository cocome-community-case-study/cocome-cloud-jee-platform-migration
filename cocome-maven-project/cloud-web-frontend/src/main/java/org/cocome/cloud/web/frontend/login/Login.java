package org.cocome.cloud.web.frontend.login;

import org.apache.log4j.Logger;
import org.cocome.cloud.web.data.logindata.*;
import org.cocome.cloud.web.events.LoginEvent;
import org.cocome.cloud.web.events.LogoutEvent;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;
import org.cocome.cloud.web.frontend.plant.PlantInformation;
import org.cocome.cloud.web.frontend.store.StoreInformation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

@Named
@SessionScoped
public class Login implements Serializable {
    private static final long serialVersionUID = 8680398263548700980L;

    @Inject
    private IAuthenticator authenticator;

    @Inject
    private ICredentialFactory credFactory;

    @Inject
    private Event<LoginEvent> loginEvent;

    @Inject
    private Event<LogoutEvent> logoutEvent;

    private String username = "";
    private ICredential password;
    private UserRole requestedRole = UserRole.ENTERPRISE_MANAGER;

    private IUser user = null;

    private long requestedStoreId = StoreInformation.STORE_ID_NOT_SET;

    private long requestedPlantId = PlantInformation.PLANT_ID_NOT_SET;

    private boolean loggedIn = false;

    private static final Logger LOG = Logger.getLogger(Login.class);

    @PostConstruct
    private void init() {
        password = credFactory.createPlainPassword("");
    }

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
        this.password = credFactory.createPlainPassword(password);
    }

    public String login() {
        IUser storedUser = authenticator.checkCredential(username, password);
        String outcome;

        if (storedUser != null) {
            setLoggedIn(true);
            user = storedUser;
            loginEvent.fire(new LoginEvent(storedUser, requestedRole, requestedStoreId, requestedPlantId));
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
        password = credFactory.createPlainPassword("");
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

    public long getRequestedPlantId() {
        return requestedPlantId == PlantInformation.PLANT_ID_NOT_SET ? 0 : requestedPlantId;
    }

    public void setRequestedPlantId(long requestedPlantId) {
        this.requestedPlantId = requestedPlantId;
    }

    public UserRole getRequestedRole() {
        return requestedRole;
    }

    public void setRequestedRole(UserRole requestedRole) {
        this.requestedRole = requestedRole;
    }

    public boolean isStoreRequired() {
        return Arrays.asList(
                NavigationViewStates.STORE_VIEW,
                NavigationViewStates.CASHPAD_VIEW,
                NavigationViewStates.DEFAULT_VIEW)
                .contains(requestedRole.associatedView());
    }

    public boolean isPlantRequired() {
        return requestedRole.associatedView() == NavigationViewStates.PLANT_VIEW;
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
