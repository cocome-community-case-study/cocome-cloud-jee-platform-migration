package org.cocome.cloud.web.login;

public interface IUser {
	public String getUsername();
	
	public void setCredentials(ICredential credentials);
	
	public ICredential getCredentials();
	
	public boolean checkCredentials(ICredential credentials);
	
	public boolean hasPermission(IPermission permission);
	
	public boolean hasPermission(String permission);
	
	public void addPermission(IPermission permission);
}
