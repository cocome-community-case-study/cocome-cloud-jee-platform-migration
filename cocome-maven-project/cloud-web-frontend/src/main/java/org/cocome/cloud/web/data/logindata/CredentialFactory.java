package org.cocome.cloud.web.data.logindata;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class CredentialFactory implements ICredentialFactory {
	@Override
	public ICredential createPlainPassword(String password) {
		return new PlainCredential(password);
	}

}
