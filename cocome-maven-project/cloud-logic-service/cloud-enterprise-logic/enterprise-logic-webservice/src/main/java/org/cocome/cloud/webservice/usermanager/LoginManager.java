package org.cocome.cloud.webservice.usermanager;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import org.cocome.logic.webservice.usermanager.ILoginManager;
import org.cocome.tradingsystem.inventory.application.store.CustomerWithStoreTO;
import org.cocome.tradingsystem.inventory.application.usermanager.IAuthenticator;
import org.cocome.tradingsystem.inventory.application.usermanager.IUserManager;
import org.cocome.tradingsystem.inventory.application.usermanager.Role;
import org.cocome.tradingsystem.inventory.application.usermanager.UserTO;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.inventory.data.usermanager.IUserDataFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Implements the webservice interface for the authentication and modification methods for users of the system. 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService(serviceName= "ILoginManagerService", 
			name = "ILoginManager", 
			endpointInterface = "org.cocome.logic.webservice.usermanager.ILoginManager",
			targetNamespace = "http://usermanager.webservice.logic.cocome.org/")
@Stateless
public class LoginManager implements ILoginManager {
	private static final Logger LOG = Logger.getLogger(LoginManager.class);
	
	@Inject
	IAuthenticator authenticator;
	
	@Inject
	IUserManager userManager;
	
	@Inject
	IUserDataFactory userFactory;
	
	@Override
	public UserTO getUserTO(UserTO accessingUser, String username) throws NotInDatabaseException {		
		checkAccessingUserCredentials(accessingUser);
		
		IUser user = userManager.getUserByName(accessingUser.getUsername());
		
		if (accessingUser.getUsername().equals(username) || 
				user.hasRole(Role.ADMIN)) {
			/* TODO Don't hardcode the permission for accessing a user.
			 * Better inject a principal and set the roles and name,
			 * this way we can use annotations to define access rights. 
			*/
			return userFactory.fillUserTO(
					userManager.getUserByName((username)));
		} else {
			throw new NotInDatabaseException("You are not allowed to access this information.");
		}
	}

	private void checkAccessingUserCredentials(UserTO accessingUser) throws NotInDatabaseException {
		if (accessingUser != null) {
			LOG.debug("Accessing user is " + accessingUser.getUsername());
			if (checkCredentials(accessingUser)) {
				return;
			}
		}
		throw new NotInDatabaseException("You are not allowed to access this information.");
	}

	@Override
	public CustomerWithStoreTO getCustomerWithStoreTO(UserTO accessingUser, String username)
			throws NotInDatabaseException {
		checkAccessingUserCredentials(accessingUser);
		
		LOG.debug("Checking for access rights of " + accessingUser.getUsername() + " for user " + username);
		
		IUser user = userManager.getUserByName(accessingUser.getUsername());
		
		if (accessingUser.getUsername().equals(username) || 
				user.hasRole(Role.ADMIN)) {
			return userFactory.fillCustomerWithStoreTO(
					userManager.getCustomerByName(username));
		} else {
			throw new NotInDatabaseException("You are not allowed to access this information.");
		}
	}

	@Override
	public boolean checkCredentials(UserTO userTO) throws NotInDatabaseException {
		IUser user = userFactory.convertToUser(userTO);
		
		return authenticator.checkCredentials(user);
	}
	
	public List<Role> getUserRoles(String username) throws NotInDatabaseException {
		LOG.debug("Retrieving user roles for user " + username);
		
		IUser user = userManager.getUserByName(username);
		
		return new ArrayList<Role>(user.getRoles());
	}

	@Override
	public UserTO requestAuthToken(UserTO userTO) throws NotInDatabaseException {
		if (checkCredentials(userTO)) {
			return userFactory.fillUserTO(
					authenticator.requestAuthToken(userTO.getUsername()));
		}
		throw new NotInDatabaseException("You are not allowed to access this information.");
	}

	@Override
	public boolean removeAuthToken(UserTO userTO) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createNewUser(UserTO newUserTO) {
		return userManager.createUser(newUserTO);
	}

	

	@Override
	public boolean createNewCustomer(CustomerWithStoreTO newCustomerTO) {
		return userManager.createCustomer(newCustomerTO);
	}

	@Override
	public boolean updateUser(UserTO userTO) {
		return userManager.updateUser(userTO);
	}

	@Override
	public boolean updateCustomer(CustomerWithStoreTO customerTO) {
		return userManager.updateCustomer(customerTO);
	}
}
