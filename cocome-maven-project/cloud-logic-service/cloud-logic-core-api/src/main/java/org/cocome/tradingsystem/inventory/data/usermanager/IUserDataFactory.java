package org.cocome.tradingsystem.inventory.data.usermanager;

import org.cocome.tradingsystem.inventory.application.store.CustomerWithStoreTO;
import org.cocome.tradingsystem.inventory.application.usermanager.CredentialTO;
import org.cocome.tradingsystem.inventory.application.usermanager.UserTO;
import org.cocome.tradingsystem.inventory.application.usermanager.credentials.ICredential;

public interface IUserDataFactory {
	public CustomerWithStoreTO fillCustomerWithStoreTO(ICustomer customer);
	
	public CredentialTO fillCredentialTO(ICredential credential);
	
	public UserTO fillUserTO(IUser user);

	public IUser getNewUser();
	
	public IUser convertToUser(UserTO userTO);

	public ICustomer getNewCustomer();
}
