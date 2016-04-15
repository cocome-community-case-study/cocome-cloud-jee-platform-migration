package org.cocome.tradingsystem.inventory.application.usermanager;

import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Interface for the authentication of users.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IAuthenticator {	
	public boolean checkCredentials(IUser user) throws NotInDatabaseException;
	
	public boolean checkHasRole(IUser user, Role role) throws NotInDatabaseException;
	
	IUser requestAuthToken(String username) throws NotInDatabaseException;

	boolean revokeAuthToken(String username);

}
