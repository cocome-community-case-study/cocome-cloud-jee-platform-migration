package org.cocome.cloud.web.events;

import org.cocome.cloud.web.login.IUser;

public class LogoutEvent {
	private IUser user;

	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}
}
