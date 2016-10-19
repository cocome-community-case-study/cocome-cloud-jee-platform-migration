package org.cocome.cloud.web.data.logindata;

public interface ICredential {
	public boolean isMatching(ICredential credentials);
	
	public String getCredentialString();
}
