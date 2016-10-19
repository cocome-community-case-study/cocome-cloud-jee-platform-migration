package org.cocome.cloud.web.events;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.web.data.logindata.IUser;
import org.cocome.cloud.web.data.logindata.UserRole;
import org.cocome.cloud.web.frontend.navigation.NavigationViewStates;

public class LoginEvent {
	private IUser user;
	private UserRole role;
	private long storeID;
	
	public LoginEvent(@NotNull IUser user, @NotNull UserRole role, long storeID) {
		this.user = user;
		this.role = role;
		this.setStoreID(storeID);
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

	public long getStoreID() {
		return storeID;
	}

	public void setStoreID(long storeID) {
		this.storeID = storeID;
	}
	
}
