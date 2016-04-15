package org.cocome.tradingsystem.inventory.application.usermanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Transport object for users.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@XmlType(name="UserTO", namespace="http://usermanager.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name="UserTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name="username", required=true)
	private String username;
	
	@XmlElement(name="roles", required=true)
	private List<Role> roles = new LinkedList<>();
	
	@XmlElement(name="credentials")
	private List<CredentialTO> credentials = new LinkedList<>();
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public List<CredentialTO> getCredentials() {
		return credentials;
	}
	
	public void setCredentials(List<CredentialTO> credentials) {
		this.credentials = credentials;
	}
}
