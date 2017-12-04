package org.cocome.cloud.web.data.logindata;

import java.io.Serializable;

public interface IUser extends Serializable {
	String getUsername();
	
	void setCredentials(ICredential credentials);
	
	ICredential getCredentials();
	
	boolean checkCredentials(ICredential credentials);
	
	boolean hasPermissionString(String permission);
	
	boolean hasPermission(IPermission permission);
	
	void addPermission(IPermission permission);
}
