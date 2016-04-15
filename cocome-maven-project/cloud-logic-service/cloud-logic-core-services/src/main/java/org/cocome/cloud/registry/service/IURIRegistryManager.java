package org.cocome.cloud.registry.service;

import java.net.URI;
import java.rmi.NotBoundException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;


/**
 * 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService(serviceName = "IURIRegistryManagerService",
			name = "IURIRegistryManager",
			targetNamespace = "http://registry.webservice.logic.cocome.org/")
public interface IURIRegistryManager {

	@WebMethod
	public void rebind(
			@XmlElement(required = true) @WebParam(name = "name") String name,
			@XmlElement(required = true) @WebParam(name = "location") URI location);
	
	@WebMethod
	public boolean unbind(
			@XmlElement(required = true) @WebParam(name = "name") String name);
	
	@WebMethod
	public URI lookup(
			@XmlElement(required = true) @WebParam(name = "name") String name) throws NotBoundException;
	
	@WebMethod
	public Set<RegistryEntry> getBoundNames();
}
