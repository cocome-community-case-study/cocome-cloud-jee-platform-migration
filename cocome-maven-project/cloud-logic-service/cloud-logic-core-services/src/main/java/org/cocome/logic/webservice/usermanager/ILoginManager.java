package org.cocome.logic.webservice.usermanager;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.tradingsystem.inventory.application.store.CustomerWithStoreTO;
import org.cocome.tradingsystem.inventory.application.usermanager.Role;
import org.cocome.tradingsystem.inventory.application.usermanager.UserTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Webservice interface for the authentication and modification methods for users of the system. 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService
public interface ILoginManager {

	@WebMethod
	public UserTO requestAuthToken(
			@XmlElement(required = true) @WebParam(name = "userTO") UserTO userTO) throws NotInDatabaseException;
	
	@WebMethod
	public boolean removeAuthToken(
			@XmlElement(required = true) @WebParam(name = "userTO") UserTO userTO);
//	
//	@WebMethod
//	public boolean isLoggedIn(
//			@XmlElement(required = true) @WebParam(name = "userTO") UserTO userTO);
	
	@WebMethod
	public boolean checkCredentials(
			@XmlElement(required = true) @WebParam(name = "userTO") UserTO userTO) throws NotInDatabaseException;
	
	@WebMethod
	public UserTO getUserTO(
			@XmlElement(required = true) @WebParam(name = "userTO") UserTO accessingUser,
			@XmlElement(required = true) @WebParam(name = "username") String username) throws NotInDatabaseException;
	
	
	@WebMethod
	public CustomerWithStoreTO getCustomerWithStoreTO(
			@XmlElement(required = true) @WebParam(name = "userTO") UserTO accessingUser,
			@XmlElement(required = true) @WebParam(name = "username") String usermame) throws NotInDatabaseException;
	
	@WebMethod
	public List<Role> getUserRoles(
			@XmlElement(required = true) @WebParam(name = "username") String username) throws NotInDatabaseException;
	
	@WebMethod
	public boolean createNewUser(
			@XmlElement(required = true) @WebParam(name = "userTO") UserTO newUserTO);
	
	@WebMethod
	public boolean createNewCustomer(
			@XmlElement(required = true) @WebParam(name = "newCustomerTO") CustomerWithStoreTO newCustomerTO);
	
	@WebMethod
	public boolean updateUser(
			@XmlElement(required = true) @WebParam(name = "userTO") UserTO userTO);
	
	@WebMethod
	public boolean updateCustomer(
			@XmlElement(required = true) @WebParam(name = "customerTO") CustomerWithStoreTO customerTO);
	
}
