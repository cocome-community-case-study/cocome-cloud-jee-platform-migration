package org.cocome.tradingsystem.inventory.application.usermanager.credentials;

import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Provider;

import org.cocome.tradingsystem.inventory.data.usermanager.CredentialType;
import org.cocome.tradingsystem.util.qualifier.Credential;

/**
 * Provides new credential instances for different types of credentials.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class CredentialFactory {
	@Inject
	Provider<AuthToken> authTokenProvider;
	
	@Inject
	Provider<PlainPassword> passwordProvider;
	
	@Produces @Credential(CredentialType.PASSWORD)
	public ICredential getPasswordCredentialInstance(@New PlainPassword password) {
		return password;
	}
	
	@Produces @Credential(CredentialType.AUTH_TOKEN)
	public ICredential getTokenCredentialInstance(@New AuthToken token) {
		return token;
	}
	
	public ICredential getCredential(CredentialType type) {
		switch (type) {
		case AUTH_TOKEN:
			return authTokenProvider.get();
		case PASSWORD:
			return passwordProvider.get();
		default:
			return passwordProvider.get();
		}
	}
}
