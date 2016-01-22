package org.cocome.cloud.web.login;

import javax.ejb.Local;

@Local
public interface IAuthenticator {	
	public boolean checkCredentials(IUser user);
	
	public boolean checkHasPermission(IUser user, IPermission permission);

}
