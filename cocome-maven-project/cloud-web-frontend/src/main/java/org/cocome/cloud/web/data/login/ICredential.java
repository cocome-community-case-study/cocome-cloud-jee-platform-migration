package org.cocome.cloud.web.data.login;

public interface ICredential {
	public boolean isMatching(ICredential credentials);
	
	public String getCredentialString();
}
