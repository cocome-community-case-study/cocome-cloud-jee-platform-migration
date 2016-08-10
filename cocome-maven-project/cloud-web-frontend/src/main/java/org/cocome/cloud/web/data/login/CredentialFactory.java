package org.cocome.cloud.web.data.login;

public class CredentialFactory implements ICredentialFactory {
	@Override
	public ICredential createPlainPassword(String password) {
		return new PlainCredential(password);
	}

}
