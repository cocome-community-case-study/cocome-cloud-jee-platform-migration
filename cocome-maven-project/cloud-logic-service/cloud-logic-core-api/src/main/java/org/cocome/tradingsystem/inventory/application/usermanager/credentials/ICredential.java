package org.cocome.tradingsystem.inventory.application.usermanager.credentials;

import org.cocome.tradingsystem.inventory.application.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;

/**
 * Interface for credentials.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface ICredential {
	public CredentialType getType();
	
	public boolean isMatching(ICredential credential);
	
	public String getCredentialString();
	
	public void setCredentialString(String credential);
	
	public char[] getCredentialChars();
	
	public void setCredentialChars(char[] credential);
	
	public char[] resetCredential(IUser user);
}
