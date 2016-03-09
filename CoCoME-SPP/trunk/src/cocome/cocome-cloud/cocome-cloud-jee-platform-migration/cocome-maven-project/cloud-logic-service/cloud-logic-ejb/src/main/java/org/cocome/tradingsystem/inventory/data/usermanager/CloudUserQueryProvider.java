package org.cocome.tradingsystem.inventory.data.usermanager;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQueryLocal;
import org.cocome.tradingsystem.inventory.data.store.IStoreQueryLocal;
import org.cocome.tradingsystem.remote.access.connection.GetXMLFromBackend;
import org.cocome.tradingsystem.remote.access.parsing.CSVHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.qualifier.Credential;

/**
 * Provides the functionality to query users from the service adapter.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Stateless
public class CloudUserQueryProvider implements IUserQueryLocal {
	private static final Logger LOG = Logger.getLogger(CloudUserQueryProvider.class);
	
	@Inject
	GetXMLFromBackend backendConnection;
	
	@Inject
	CSVHelper csvHelper;
	
	@Override
	public IUser queryUserByName(String username) throws NotInDatabaseException {
		Collection<IUser> users = csvHelper.getUsersFromCSV(
				backendConnection.getUser("username=LIKE%20'" + username + "'"));
		if (users.size() > 1) {
			LOG.warn("Query for user with name " + username + " returned multiple matches!");
		} else if (users.size() == 0) {
			throw new NotInDatabaseException("User with username " + username + " not found!");
		}
		return users.iterator().next();
	}

	@Override
	public ICustomer queryCustomer(IUser user) throws NotInDatabaseException {
		Collection<ICustomer> customers = csvHelper.getCustomersFromCSV(
				backendConnection.getCustomer("user.username=LIKE%20'" + user.getUsername() + "'"));
		if (customers.size() > 1) {
			LOG.warn("Query for customer with username " + user.getUsername() + " returned multiple matches!");
		} else if (customers.size() == 0) {
			throw new NotInDatabaseException("Customer with username " + user.getUsername() + " not found!");
		}
		return customers.iterator().next();
	}
}
