package org.cocome.tradingsystem.inventory.data.usermanager;

import java.util.Collection;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.remote.access.connection.IBackendQuery;
import org.cocome.tradingsystem.remote.access.connection.QueryParameterEncoder;
import org.cocome.tradingsystem.remote.access.parsing.IBackendConversionHelper;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Provides the functionality to query users from the service adapter.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Stateless
@Local(IUserQuery.class)
public class EnterpriseUserQueryProvider implements IUserQuery {
	private static final Logger LOG = Logger.getLogger(EnterpriseUserQueryProvider.class);
	
	@Inject
	IBackendQuery backendConnection;
	
	@Inject
	IBackendConversionHelper csvHelper;
	
	@Override
	public IUser queryUserByName(String username) throws NotInDatabaseException {
		username = QueryParameterEncoder.encodeQueryString(username);
		
		Collection<IUser> users = csvHelper.getUsers(
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
		String encUsername = QueryParameterEncoder.encodeQueryString(user.getUsername());
		Collection<ICustomer> customers = csvHelper.getCustomers(
				backendConnection.getCustomer("user.username=LIKE%20'" + encUsername + "'"));
		if (customers.size() > 1) {
			LOG.warn("Query for customer with username " + user.getUsername() + " returned multiple matches!");
		} else if (customers.size() == 0) {
			throw new NotInDatabaseException("Customer with username " + user.getUsername() + " not found!");
		}
		return customers.iterator().next();
	}
}
