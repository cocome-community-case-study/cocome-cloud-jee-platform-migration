package org.cocome.tradingsystem.inventory.data.usermanager;

import java.util.Map;
import java.util.Set;

import org.cocome.tradingsystem.inventory.application.usermanager.credentials.CredentialTypeNotFoundException;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Interface for users of the system. This interface is mainly 
 * used for authentication purposes. 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IUser {
	public void initUser(UserTO user);
	
	public String getUsername();
	
	public void setCredentials(Map<CredentialType, ICredential> credentials);
	
	public Map<CredentialType, ICredential> getCredentials();
	
	public ICredential getCredential(CredentialType credentialType);
	
	public void addCredential(ICredential credential);
	
	public void removeCredential(CredentialType credentialType);
	
	/**
	 * Resets the credential of the given type to a new value.
	 * This can be used to handle password reset requests when 
	 * called with the {@code CredentialType.PLAIN_PASSWORD} or 
	 * {@code CredentialType.HASHED_PASSWORD}.    
	 * 
	 * @param credentialType 
	 * @return the new credential string, e.g. the new password
	 * @throws CredentialTypeNotFoundException if no credential 
	 * 		with this type is registered for this user
	 */
	public String resetCredential(CredentialType credentialType) throws CredentialTypeNotFoundException;
	
	public boolean checkCredential(ICredential credential);
	
	public boolean checkUserCredentials(IUser userToCheck);
	
	public boolean hasRole(Role role);
	
	public void addRole(Role role);
	
	public Set<Role> getRoles();
	
	public void setRoles(Set<Role> roles);
	
	public void withdrawRole(Role role);
	
	public void setUsername(String username);
	
//	public boolean isLoggedIn();
//	
//	public boolean logOut();
//	
//	public void logIn();
}
