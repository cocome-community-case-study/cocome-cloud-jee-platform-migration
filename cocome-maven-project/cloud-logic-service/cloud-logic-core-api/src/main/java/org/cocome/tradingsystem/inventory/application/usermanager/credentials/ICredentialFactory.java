package org.cocome.tradingsystem.inventory.application.usermanager.credentials;

import org.cocome.tradingsystem.inventory.application.usermanager.CredentialType;

public interface ICredentialFactory {
	ICredential getCredential(CredentialType type);
}