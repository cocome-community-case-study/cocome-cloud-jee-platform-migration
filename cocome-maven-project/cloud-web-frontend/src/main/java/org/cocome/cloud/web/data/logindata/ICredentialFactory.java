package org.cocome.cloud.web.data.logindata;

public interface ICredentialFactory {
	ICredential createPlainPassword(String password);
}
