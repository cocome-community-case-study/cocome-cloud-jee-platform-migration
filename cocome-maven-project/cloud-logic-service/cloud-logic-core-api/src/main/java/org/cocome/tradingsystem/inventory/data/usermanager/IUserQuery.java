package org.cocome.tradingsystem.inventory.data.usermanager;

import javax.ejb.Local;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;


/**
 * Interface for queries to retrieve information about users.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Local
public interface IUserQuery {
	public IUser queryUserByName(String username) throws NotInDatabaseException;
	public ICustomer queryCustomer(IUser user) throws NotInDatabaseException;
}
