package org.cocome.tradingsystem.inventory.data.usermanager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.usermanager.CredentialTO;
import org.cocome.tradingsystem.inventory.application.usermanager.CredentialType;
import org.cocome.tradingsystem.inventory.application.usermanager.CredentialTypeNotFoundException;
import org.cocome.tradingsystem.inventory.application.usermanager.Role;
import org.cocome.tradingsystem.inventory.application.usermanager.UserTO;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.util.qualifier.CredentialLiteral;

/**
 * Represents a user of the system.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Dependent
class User implements IUser, Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(User.class);
	
	private String username;
	
	private Map<CredentialType, ICredential> credentials = new HashMap<CredentialType, ICredential>((int) (CredentialType.SIZE / 0.75));
	
	private Set<Role> roles = new HashSet<Role>((int) (Role.SIZE / 0.75));
	
	@Inject @Any
	Instance<ICredential> credentialInstance;
	
	protected void initRoles() {
		// No default roles for a user
	}
	
	protected void initCredentials() {
		// No default credentials
	}
	
	@PostConstruct
	public void initUser() {
		initRoles();
		initCredentials();
	}
	
	public void initUser(UserTO userTO) {
		this.username = userTO.getUsername();
		
		for (CredentialTO cred : userTO.getCredentials()) {
			ICredential credential = credentialInstance.select(
					new CredentialLiteral(cred.getType())).get();
			credential.setCredentialChars(cred.getCredentialChars());
			credentials.put(cred.getType(), credential);
		}
		
		// Don't initialize the roles from the passed in TO but query them from the database
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void setCredentials(Map<CredentialType, ICredential> credentials) {
		this.credentials = credentials;
	}

	@Override
	public Map<CredentialType, ICredential> getCredentials() {
		return credentials;
	}
	
	@Override
	public void addCredential(ICredential credential) {
		if (!credentials.containsKey(credential.getType())) {
			credentials.put(credential.getType(), credential);
		}
	}
	
	@Override
	public void removeCredential(CredentialType credentialType) {
		credentials.remove(credentialType);
	}

	@Override
	public boolean checkCredential(ICredential credential) {
		ICredential storedCred = credentials.get(credential.getType());
		boolean result = false;
		if (storedCred != null) {
			result = storedCred.isMatching(credential);
		}
		return result;
	}
	
	@Override
	public boolean checkUserCredentials(IUser userToCheck) {
		boolean result = false;
		
		LOG.debug("Usernames " + username + " and " + userToCheck.getUsername());
		
		// Username has to match as part of the credentials
		if (username.equals(userToCheck.getUsername())) {
			result = true;
		}
		
		for (ICredential credential : credentials.values()) {
			ICredential credToCheck = userToCheck.getCredential(credential.getType());
			if (credToCheck == null) {
				// No credential of the given type found
				LOG.info("Missing credential of type: " + credential.getType());
				continue;
			}
			
			LOG.debug("Checking credential with type " + credToCheck.getType());
			
			if (credToCheck.getType() == CredentialType.AUTH_TOKEN) {
				// If an authentication token is present, check only this token
				result = credential.isMatching(credential);
				break;
			}
			
			result = credential.isMatching(credToCheck) ? result : false;
		}
		LOG.debug("Endresult was " + result);
		return result;
	}

	@Override
	public boolean hasRole(Role role) {
		LOG.debug("Checking for permission " + role.label() + " in " + roles.toString());
		return roles.contains(role);
	}

	@Override
	public void addRole(Role role) {
		if (role != null) {
			LOG.debug("Adding role " + role.label() + " to user " + username);
			roles.add(role);
		}
	}

	@Override
	public void withdrawRole(Role role) {
		LOG.debug("Withdrawing role " + role.label() + " from user " + username);
		roles.remove(role);
	}
	
	@Override
	public Set<Role> getRoles() {
		return roles;
	}
	
	@Override
	public void setRoles(Set<Role> roles) {
		if (roles != null) {
			LOG.debug("Setting roles for user " + username);
			this.roles = roles;
		} else {
			initRoles();
		}
	}
	
	@Override
	public String resetCredential(CredentialType credentialType) throws CredentialTypeNotFoundException {
		ICredential cred = credentials.get(credentialType);
		if (cred != null) {
			return new String(cred.resetCredential(this));
		}
		throw new CredentialTypeNotFoundException("No credential with the type " + credentialType + "registered!");
	}

	@Override
	public ICredential getCredential(CredentialType credentialType) {
		return credentials.get(credentialType);
	}

//	@Override
//	public boolean isLoggedIn() {
//		return loggedIn;
//	}
//
//	@Override
//	public boolean logOut() {
//		this.loggedIn = false;
//		return true;
//	}
//
//	@Override
//	public void logIn() {
//		try {
//			LOG.debug("Trying to login as " + username);
//			loggedIn = authenticator.checkCredentials(this);
//		} catch (NotInDatabaseException e) {
//			loggedIn = false;
//			LOG.error("Login for user " + username + " failed!");
//		}
//	}
}
