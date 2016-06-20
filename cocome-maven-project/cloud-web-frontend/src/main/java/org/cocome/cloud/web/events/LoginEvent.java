package org.cocome.cloud.web.events;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;
import org.cocome.cloud.web.login.IUser;
import org.cocome.cloud.web.login.UserRole;

public class LoginEvent {
	private IUser user;
	private UserRole role;
	
	public LoginEvent(@NotNull IUser user, @NotNull UserRole role) {
		this.user = user;
		this.role = role;
	}
	
	public UserRole getRole() {
		return role;
	}
	
	public void setRole(@NotNull UserRole role) {
		this.role = role;
	}

	public IUser getUser() {
		return user;
	}

	public void setUser(@NotNull IUser user) {
		this.user = user;
	}
	
	public NavigationViewStates getRequestedView() {
		return role.associatedView();
	}
	
}
