package org.cocome.cloud.registry.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.net.URI;
import java.rmi.NotBoundException;
import java.util.Set;


/**
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService(serviceName = "IURIRegistryManagerService",
        name = "IURIRegistryManager",
        targetNamespace = "http://registry.webservice.logic.cocome.org/")
public interface IURIRegistryManager {

    @WebMethod
    void rebind(
            @XmlElement(required = true) @WebParam(name = "name") String name,
            @XmlElement(required = true) @WebParam(name = "location") URI location);

    @WebMethod
    boolean unbind(
            @XmlElement(required = true) @WebParam(name = "name") String name);

    @WebMethod
    URI lookup(
            @XmlElement(required = true) @WebParam(name = "name") String name) throws NotBoundException;

    @WebMethod
    Set<RegistryEntry> getBoundNames();
}
