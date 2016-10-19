package org.cocome.cloud.web.events;

import org.cocome.cloud.web.data.logindata.IUser;

public class LogoutEvent {
	private IUser user;

	public LogoutEvent(IUser user) {
		this.user = user;
	}
	
	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}
}
