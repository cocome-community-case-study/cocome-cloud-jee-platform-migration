package org.cocome.tradingsystem.inventory.application.store;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.cocome.tradingsystem.inventory.data.usermanager.Role;
import org.cocome.tradingsystem.inventory.data.usermanager.UserTO;

@XmlRootElement(name="CustomerWithStoreTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerWithStoreTO extends UserTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name="id")
	private long id;
	
	@XmlElement(name="preferredStoreTO")
	private StoreTO preferredStoreTO;
	
	@XmlElement(name="firstName")
	private String firstName;
	
	@XmlElement(name="lastName")
	private String lastName;
	
	@XmlElement(name="mailAddress")
	private String mailAddress;
	
	@XmlElement(name="creditCardInfos")
	private List<String> creditCardInfos = new LinkedList<>();
	
	public CustomerWithStoreTO() {
		super();
		this.getRoles().add(Role.CUSTOMER);
	}

	public StoreTO getPreferredStoreTO() {
		return preferredStoreTO;
	}

	public void setPreferredStoreTO(StoreTO preferredStoreTO) {
		this.preferredStoreTO = preferredStoreTO;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public List<String> getCreditCardInfos() {
		return creditCardInfos;
	}

	public void setCreditCardInfos(List<String> creditCardInfos) {
		this.creditCardInfos = creditCardInfos;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
