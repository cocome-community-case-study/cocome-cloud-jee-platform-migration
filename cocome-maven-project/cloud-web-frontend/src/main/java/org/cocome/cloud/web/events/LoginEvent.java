package org.cocome.cloud.web.events;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;
import org.cocome.cloud.web.login.IUser;

public class LoginEvent {
	private IUser user;
	private NavigationViewStates requestedView;
	
	public LoginEvent(@NotNull IUser user, @NotNull NavigationViewStates requestedView) {
		this.user = user;
		this.requestedView = requestedView;
	}
	
	public NavigationViewStates getRequestedView() {
		return requestedView;
	}
	
	public void setRequestedView(@NotNull NavigationViewStates requestedView) {
		this.requestedView = requestedView;
	}

	public IUser getUser() {
		return user;
	}

	public void setUser(@NotNull IUser user) {
		this.user = user;
	}
	
}
