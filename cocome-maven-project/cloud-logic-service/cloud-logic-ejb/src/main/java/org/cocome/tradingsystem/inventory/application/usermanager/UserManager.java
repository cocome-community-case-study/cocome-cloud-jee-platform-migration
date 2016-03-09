package org.cocome.tradingsystem.inventory.application.usermanager;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.ejb.CreateException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.store.CustomerWithStoreTO;
import org.cocome.tradingsystem.inventory.application.store.StoreTO;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.CredentialFactory;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.CredentialTO;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQueryLocal;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContextLocal;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.store.IStoreQueryLocal;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.inventory.data.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.data.usermanager.Customer;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.inventory.data.usermanager.IUserQueryLocal;
import org.cocome.tradingsystem.inventory.data.usermanager.Role;
import org.cocome.tradingsystem.inventory.data.usermanager.UserTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.qualifier.Credential;

/**
 * Implements the management of users and their data.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@ApplicationScoped
public class UserManager implements IAuthenticator, IUserManager {
	private static final Logger LOG = Logger.getLogger(UserManager.class);
	
	@Inject
	IUserQueryLocal userQuery;
	
	@Inject
	IPersistenceContextLocal persistenceCtx;
	
	@Inject
	IStoreQueryLocal storeQuery;
	
	@Inject @Credential(CredentialType.AUTH_TOKEN)
	ICredential authToken;
	
	@Inject
	Provider<IUser> userProvider;
	
	@Inject
	Provider<ICustomer> customerProvider;
	
	@Inject
	CredentialFactory credFactory;
	
	@Override
	public boolean checkCredentials(IUser user) throws NotInDatabaseException {
		IUser storedUser = getUserByName(user.getUsername());
		return storedUser.checkUserCredentials(user);
	}

	@Override
	public boolean checkHasRole(IUser user, Role role) throws NotInDatabaseException {
		IUser storedUser = getUserByName(user.getUsername());
		return storedUser.hasRole(role);
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.IUserManager#getUserByName(java.lang.String)
	 */
	@Override
	public IUser getUserByName(String username) throws NotInDatabaseException {
		return userQuery.queryUserByName(username);
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.IUserManager#getCustomerByName(java.lang.String)
	 */
	@Override
	public ICustomer getCustomerByName(String username) throws NotInDatabaseException {
		IUser user = getUserByName(username);
		ICustomer customer = userQuery.queryCustomer(user);
		customer.setUser(user);
		return customer;
	}

	@Override
	public IUser requestAuthToken(String username) throws NotInDatabaseException {
		IUser user = getUserByName(username);
		authToken.resetCredential(user);
		user.addCredential(authToken);
		return user;
	}

	@Override
	public boolean revokeAuthToken(String username) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private IUser fillUserFromTO(UserTO userTO) {
		// TODO check first if user already exists in the database 
		// and in that case use the user from there
		IUser user = userProvider.get();
		user.setUsername(userTO.getUsername());
		user.setRoles(new LinkedHashSet<>(userTO.getRoles()));
		
		Map<CredentialType, ICredential> credentials = new LinkedHashMap<>();
		for (CredentialTO credential : userTO.getCredentials()) {
			if (credential.getType() != CredentialType.AUTH_TOKEN) {
				ICredential cred = credFactory.getCredential(credential.getType());
				cred.setCredentialString(credential.getCredentialString());
				credentials.put(credential.getType(), cred);
			}
		}
		
		user.setCredentials(credentials);
		return user;
	}
	
	private ICustomer fillCustomerFromTO(CustomerWithStoreTO customerTO) {
		ICustomer customer = customerProvider.get();
		customer.setFirstName(customerTO.getFirstName());
		customer.setLastName(customerTO.getLastName());
		customer.setMailAddress(customerTO.getMailAddress());
		
		if (customerTO.getPreferredStoreTO() != null) {
			Store prefStore;
			try {
				prefStore = storeQuery.queryStoreById(customerTO.getPreferredStoreTO().getId());
				customer.setPreferredStore(prefStore);
			} catch (NotInDatabaseException e) {
				LOG.warn("No valid store found with id " + customerTO.getPreferredStoreTO().getId());
			}
		}
		
		customer.setUser(fillUserFromTO(customerTO));
		return customer;
	}

	@Override
	public boolean createUser(UserTO userTO) {
		try {
			persistenceCtx.createEntity(fillUserFromTO(userTO));
			return true;
		} catch (CreateException e) {
			LOG.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean createCustomer(CustomerWithStoreTO customerTO) {
		try {
			persistenceCtx.createEntity(fillCustomerFromTO(customerTO));
			return true;
		} catch (CreateException e) {
			LOG.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean updateUser(UserTO userTO) {
		try {
			persistenceCtx.updateEntity(fillUserFromTO(userTO));
			return true;
		} catch (UpdateException e) {
			LOG.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean updateCustomer(CustomerWithStoreTO customerTO) {
		try {
			persistenceCtx.updateEntity(fillCustomerFromTO(customerTO));
			return true;
		} catch (UpdateException e) {
			LOG.error(e.toString());
			return false;
		}
	}
}
