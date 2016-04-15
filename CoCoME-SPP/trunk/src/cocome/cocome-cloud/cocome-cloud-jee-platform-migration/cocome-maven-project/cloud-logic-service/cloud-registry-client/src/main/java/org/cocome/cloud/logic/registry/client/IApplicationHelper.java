package org.cocome.cloud.logic.registry.client;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.cocome.cloud.logic.stub.NotBoundException_Exception;

public interface IApplicationHelper {

	/**
	 * 
	 * @param name
	 * @param service
	 * @throws URISyntaxException
	 */
	void registerComponent(String name, Service service) throws URISyntaxException;

	/**
	 * 
	 * @param name
	 * @param location
	 */
	void registerComponent(String name, URI location);

	/**
	 * 
	 * @param name
	 * @param remote
	 */
	void registerComponent(String name, URI location, boolean reregister);
	
	/**
	 * 
	 * @param name
	 * @param location
	 * @param reregister
	 * @throws URISyntaxException 
	 */
	void registerComponent(String name, String location, boolean reregister) throws URISyntaxException;

	/**
	 * 
	 * @param name
	 * @return
	 */
	boolean unregisterComponent(String name);

	/**
	 * 
	 * @param name
	 * @param serviceName
	 * @param type
	 * @return
	 * @throws NotBoundException_Exception
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws MalformedURLException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	<T extends Service> T getComponent(String name, QName serviceName, Class<T> type)
			throws NotBoundException_Exception, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, MalformedURLException, NoSuchMethodException,
			SecurityException;

	/**
	 * 
	 * @param name
	 * @return
	 * @throws URISyntaxException
	 * @throws NotBoundException_Exception
	 */
	URI getComponentURI(String name) throws URISyntaxException, NotBoundException_Exception;

	/**
	 * 
	 * @param name
	 * @return
	 * @throws MalformedURLException
	 * @throws NotBoundException_Exception
	 */
	URL getComponentURL(String name) throws MalformedURLException, NotBoundException_Exception;

}