package org.cocome.tradingsystem.inventory.application.usermanager.credentials;

import org.cocome.tradingsystem.inventory.application.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;

/**
 * Implements an authentication token credential that can be used instead of 
 * sending the password all the time. 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
class AuthToken implements ICredential {
	private char[] tokenString;

	@Override
	public CredentialType getType() {
		return CredentialType.AUTH_TOKEN;
	}

	@Override
	public boolean isMatching(ICredential credential) {
		boolean isAuthToken = credential.getType() == CredentialType.AUTH_TOKEN;
		boolean matchesAuthToken = getCredentialString().equals(credential.getCredentialString()); 
		return isAuthToken ? matchesAuthToken : false;
	}

	@Override
	public String getCredentialString() {
		return new String(tokenString);
	}

	@Override
	public void setCredentialString(String credential) {
		tokenString = credential.toCharArray();
	}

	@Override
	public char[] getCredentialChars() {
		return tokenString;
	}

	@Override
	public void setCredentialChars(char[] credential) {
		tokenString = credential;
	}

	@Override
	public char[] resetCredential(IUser user) {
		// Set the authentication token to a random password + the username + 
		// one char to indicate if the user has admin access or not
		int passLength = RandomPasswordGenerator.PASSWORD_LENGTH;
		int passUserLength = passLength + user.getUsername().length();
		
		char[] buffer = new char[passUserLength];
		char[] password = RandomPasswordGenerator.generatePassword();
		
		for (int i = 0; i < passUserLength; i++) {
			if (i < RandomPasswordGenerator.PASSWORD_LENGTH) {
				buffer[i] = password[i];
			} else if (i < passUserLength) {
				buffer[i] = user.getUsername().charAt(i - passLength);
			}
		}
		
		tokenString = buffer;
		return buffer;
	}

}
