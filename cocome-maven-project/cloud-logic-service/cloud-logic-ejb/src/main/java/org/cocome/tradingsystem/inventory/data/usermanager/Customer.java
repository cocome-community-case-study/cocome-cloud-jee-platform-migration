package org.cocome.tradingsystem.inventory.data.usermanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.Inheritance;

import org.cocome.tradingsystem.inventory.data.store.Store;

import java.io.Serializable;

/**
 * Represents a customer of the pickup shop.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class Customer implements Serializable, ICustomer {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String firstName;
	private String lastName;
	private String mailAddress;
	private Set<String> creditCardInfoSet = new LinkedHashSet<String>();
	private Store preferredStore;
	private IUser user;

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getMailAddress()
	 */
	@Override
	public String getMailAddress() {
		return mailAddress;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setMailAddress(java.lang.String)
	 */
	@Override
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getPreferredStore()
	 */
	@Override
	public Store getPreferredStore() {
		return preferredStore;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setPreferredStore(org.cocome.tradingsystem.inventory.data.store.Store)
	 */
	@Override
	public void setPreferredStore(Store preferredStore) {
		this.preferredStore = preferredStore;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getLastName()
	 */
	@Override
	public String getLastName() {
		return lastName;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setLastName(java.lang.String)
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setFirstName(java.lang.String)
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#getCreditCardInfo()
	 */
	@Override
	public Set<String> getCreditCardInfo() {
		return creditCardInfoSet;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#setCreditCardInfo(java.util.Set)
	 */
	@Override
	public void setCreditCardInfo(Set<String> creditCardInfo) {
		this.creditCardInfoSet = creditCardInfo;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#addCreditCardInfo(java.lang.String)
	 */
	@Override
	public void addCreditCardInfo(String creditCardInfo) {
		if (creditCardInfo != null && !creditCardInfo.isEmpty()) {
			// TODO Add some validity check for this info perhaps
			creditCardInfoSet.add(creditCardInfo);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.usermanager.ICustomer#removeCreditCardInfo(java.lang.String)
	 */
	@Override
	public void removeCreditCardInfo(String creditCardInfo) {
		creditCardInfoSet.remove(creditCardInfo);
	}

	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long customerID) {
		this.id = customerID;
	}
}
