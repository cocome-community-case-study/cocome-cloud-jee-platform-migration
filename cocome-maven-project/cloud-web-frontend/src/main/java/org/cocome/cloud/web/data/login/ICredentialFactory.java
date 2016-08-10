package org.cocome.cloud.web.data.login;

public interface ICredentialFactory {
	ICredential createPlainPassword(String password);
}
