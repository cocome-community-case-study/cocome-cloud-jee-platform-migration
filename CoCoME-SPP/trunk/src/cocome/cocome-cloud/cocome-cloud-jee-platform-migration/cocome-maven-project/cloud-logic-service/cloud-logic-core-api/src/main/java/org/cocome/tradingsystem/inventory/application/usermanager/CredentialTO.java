package org.cocome.tradingsystem.inventory.application.usermanager;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Transport object for credentials.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@XmlType(name="CredentialTO", namespace="http://usermanager.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name="CredentialTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class CredentialTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="credentialString", required=true)
	private String credentialChars;
	
	@XmlElement(name="type", required=true)
	private CredentialType type;

	public String getCredentialString() {
		return new String(credentialChars);
	}

	public void setCredentialString(String credential) {
		credentialChars = credential;
	}

	public CredentialType getType() {
		return type;
	}

	public void setType(CredentialType type) {
		this.type = type;
	}
	
	public void setCredentialChars(char[] credential) {
		credentialChars = new String(credential);
	}
	
	public char[] getCredentialChars() {
		return credentialChars.toCharArray();
	}
	
}
