package org.cocome.tradingsystem.inventory.application.usermanager.credentials;

import org.cocome.tradingsystem.inventory.application.usermanager.CredentialType;
import org.cocome.tradingsystem.util.qualifier.Credential;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Provides new credential instances for different types of credentials.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Dependent
public class CredentialFactory implements ICredentialFactory {

	@Inject
	private Provider<AuthToken> authTokenProvider;
	
	@Inject
	private Provider<PlainPassword> passwordProvider;
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredentialFactory#getPasswordCredentialInstance(org.cocome.tradingsystem.inventory.application.usermanager.credentials.PlainPassword)
	 */
	@Produces @Credential(CredentialType.PASSWORD)
	public ICredential getPasswordCredentialInstance(@New PlainPassword password) {
		return password;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredentialFactory#getTokenCredentialInstance(org.cocome.tradingsystem.inventory.application.usermanager.credentials.AuthToken)
	 */
	@Produces @Credential(CredentialType.AUTH_TOKEN)
	public ICredential getTokenCredentialInstance(@New AuthToken token) {
		return token;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredentialFactory#getCredential(org.cocome.tradingsystem.inventory.application.usermanager.CredentialType)
	 */
	@Override
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
