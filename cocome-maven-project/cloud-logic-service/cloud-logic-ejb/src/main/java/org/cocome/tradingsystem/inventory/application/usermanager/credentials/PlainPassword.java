package org.cocome.tradingsystem.inventory.application.usermanager.credentials;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;

/**
 * Implements a plain password credential. This is very insecure.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class PlainPassword implements ICredential {
	private final static Logger LOG = Logger.getLogger(PlainPassword.class);
	private static final CredentialType TYPE = CredentialType.PASSWORD;
	private char[] password;
	
	@PostConstruct
	public void initPassword() {
		resetCredential(null);
	}

	@Override
	public boolean isMatching(ICredential credentials) {
		if (credentials == null) return false;
		LOG.debug("Comparing " + password.toString() + " with " 
				+ credentials.getCredentialString());
		
		boolean result = Arrays.equals(password, credentials.getCredentialChars());
		LOG.debug("Result was " + result);
		return result;
	}

	@Override
	public String getCredentialString() {
		return new String(password);
	}

	@Override
	public CredentialType getType() {
		return TYPE;
	}

	@Override
	public void setCredentialString(String credential) {
		password = credential.toCharArray();
	}

	@Override
	public char[] resetCredential(IUser user) {
		password = RandomPasswordGenerator.generatePassword();
		return password;
	}

	@Override
	public char[] getCredentialChars() {
		return password;
	}

	@Override
	public void setCredentialChars(char[] credential) {
		password = credential;
	}
	

}
