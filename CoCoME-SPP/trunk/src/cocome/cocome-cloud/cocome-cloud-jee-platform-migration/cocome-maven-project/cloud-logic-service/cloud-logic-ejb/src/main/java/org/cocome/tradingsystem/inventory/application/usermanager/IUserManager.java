package org.cocome.tradingsystem.inventory.application.usermanager;

import org.cocome.tradingsystem.inventory.application.store.CustomerWithStoreTO;
import org.cocome.tradingsystem.inventory.data.usermanager.ICustomer;
import org.cocome.tradingsystem.inventory.data.usermanager.IUser;
import org.cocome.tradingsystem.inventory.data.usermanager.UserTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Interface for the management of users.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IUserManager {

	IUser getUserByName(String username) throws NotInDatabaseException;

	ICustomer getCustomerByName(String username) throws NotInDatabaseException;
	
	boolean createUser(UserTO userTO);
	
	boolean createCustomer(CustomerWithStoreTO customerTO);
	
	boolean updateUser(UserTO userTO);
	
	boolean updateCustomer(CustomerWithStoreTO customerTO);
}