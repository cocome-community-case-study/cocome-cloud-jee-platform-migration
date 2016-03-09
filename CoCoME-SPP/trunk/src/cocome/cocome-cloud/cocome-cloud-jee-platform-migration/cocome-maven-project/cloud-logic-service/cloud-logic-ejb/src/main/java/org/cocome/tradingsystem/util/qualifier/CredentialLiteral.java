package org.cocome.tradingsystem.util.qualifier;

import javax.enterprise.util.AnnotationLiteral;

import org.cocome.tradingsystem.inventory.data.usermanager.CredentialType;

public class CredentialLiteral extends AnnotationLiteral<Credential> implements Credential {
	private static final long serialVersionUID = 1L;
	
	private CredentialType value;

	public CredentialLiteral(CredentialType value) {
		this.value = value;
	}
	
	@Override
	public CredentialType value() {
		return value;
	}
	
	
}
