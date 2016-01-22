package org.cocome.cloud.web.login;

public interface ICredential {
	public boolean isMatching(ICredential credentials);
	
	public String getCredentialString();
}
