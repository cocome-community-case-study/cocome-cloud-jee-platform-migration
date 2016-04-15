package org.cocome.tradingsystem.inventory.data.usermanager;

import java.util.Set;

import org.cocome.tradingsystem.inventory.data.store.IStore;

/**
 * Interface for customers of the pickup shop. 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface ICustomer {

	public String getMailAddress();

	public void setMailAddress(String mailAddress);

	public IStore getPreferredStore();

	public void setPreferredStore(IStore preferredStore);

	public String getLastName();

	public void setLastName(String lastName);

	public String getFirstName();

	public void setFirstName(String firstName);

	public Set<String> getCreditCardInfo();

	public void setCreditCardInfo(Set<String> creditCardInfo);

	public void addCreditCardInfo(String creditCardInfo);

	public void removeCreditCardInfo(String creditCardInfo);

	public void setUser(IUser user);
	
	public IUser getUser();
	
	public long getID();
	
	public void setID(long id);
}