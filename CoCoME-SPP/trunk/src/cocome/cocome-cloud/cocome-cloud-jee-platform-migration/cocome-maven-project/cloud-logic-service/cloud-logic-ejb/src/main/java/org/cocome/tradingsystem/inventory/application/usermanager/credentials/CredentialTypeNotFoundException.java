package org.cocome.tradingsystem.inventory.application.usermanager.credentials;

/**
 * Indicates that no credential with the given type was found.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class CredentialTypeNotFoundException extends Exception {

	public CredentialTypeNotFoundException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
