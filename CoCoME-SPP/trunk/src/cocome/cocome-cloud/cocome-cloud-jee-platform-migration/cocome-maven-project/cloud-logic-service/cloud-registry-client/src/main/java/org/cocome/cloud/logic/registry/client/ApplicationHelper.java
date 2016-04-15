/***************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.cocome.cloud.logic.registry.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IURIRegistryManager;
import org.cocome.cloud.logic.stub.IURIRegistryManagerService;
import org.cocome.cloud.logic.stub.NotBoundException;
import org.cocome.cloud.logic.stub.NotBoundException_Exception;


/**
 * Contains various utility methods for application classes.
 * 
 * @author Lubomir Bulej
 */
@ApplicationScoped
public class ApplicationHelper implements IApplicationHelper {
	private static final Logger LOG = Logger.getLogger(ApplicationHelper.class);
	
	@WebServiceRef(IURIRegistryManagerService.class)
	IURIRegistryManager registryManager;

	private Set<String> registeredNames = new HashSet<>();

	@PreDestroy
	private void deregisterNames() {
		for (final String name : registeredNames) {
			try {
				unbindName(name);
			} catch (final Exception e) {
				// ignore any exceptions when unbinding the name
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.registry.client.IApplicationHelper#registerComponent(java.lang.String, javax.xml.ws.Service)
	 */
	@Override
	public void registerComponent(final String name, final Service service) throws URISyntaxException {
		registerComponent(name, service.getWSDLDocumentLocation().toURI(), false);
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.registry.client.IApplicationHelper#registerComponent(java.lang.String, java.net.URI)
	 */
	@Override
	public void registerComponent(final String name, final URI location) {
		registerComponent(name, location, false);
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.registry.client.IApplicationHelper#registerComponent(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void registerComponent(final String name, final URI location, final boolean reregister) {
		LOG.debug(String.format("Registering component %s at location %s. Reregister was set to %b.", name, location.toString(), reregister));
		
		boolean register = false;
		if (!registeredNames.contains(name) || reregister) {
			try {
				URI boundURI = lookupSavedLocation(name);
				// Name is already in the registry
				if (reregister) {
					register = true;
				} else {
					// Add the name as already registered to save 
					// the lookup next time
					registeredNames.add(name);
				}
			} catch (NotBoundException_Exception e) {
				register = true;
			}
		}
		
		if (register) {
			registryManager.rebind(name, location.toString());
			registeredNames.add(name);
		}
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.registry.client.IApplicationHelper#unregisterComponent(java.lang.String)
	 */
	@Override
	public boolean unregisterComponent(final String name) {
		LOG.debug(String.format("Unegistering component %s.", name));
		final boolean nameUnregistered = unbindName(name);
		if (nameUnregistered) {
			registeredNames.remove(name);
		}

		return nameUnregistered;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private boolean unbindName(final String name) {
		return registryManager.unbind(name);
	}

	/* (non-Javadoc)
	 * @see org.cocome.cloud.registry.client.IApplicationHelper#getComponent(java.lang.String, javax.xml.namespace.QName, java.lang.Class)
	 */
	@Override
	public <T extends Service> T getComponent(final String name, final QName serviceName, Class<T> type) throws NotBoundException_Exception {
		LOG.debug(String.format("Looking up component %s with service name %s and type %s.", name, serviceName.toString(), type.toString()));
		URI savedLocation = lookupSavedLocation(name);
		
		URL wsdlLocation = getWsdlLocation(savedLocation);

		Constructor<T> resultConstructor = getServiceConstructor(type);

		try {
			LOG.debug(String.format("Found component %s at location %s.", name, wsdlLocation.toString()));
			T result = resultConstructor.newInstance(wsdlLocation, serviceName);
			return result;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOG.error(String.format("Problem instantiating service class: %s", e.toString()));
			e.printStackTrace();
			throw new NotBoundException_Exception("Could not instantiate service class!", new NotBoundException());
		}
	}

	private <T extends Service> Constructor<T> getServiceConstructor(Class<T> type) throws NotBoundException_Exception {
		// All classes extending Service should have this constructor
		Constructor<T> resultConstructor;
		try {
			resultConstructor = type.getConstructor(URL.class, QName.class);
		} catch (NoSuchMethodException | SecurityException e) {
			LOG.error(String.format("Problem instantiating service class: %s", e.toString()));
			e.printStackTrace();
			throw new NotBoundException_Exception("Could not instantiate service class!", new NotBoundException());
		}
		return resultConstructor;
	}

	private URL getWsdlLocation(URI savedLocation) throws NotBoundException_Exception {
		URL wsdlLocation;
		try {
			wsdlLocation = savedLocation.toURL();
		} catch (MalformedURLException e) {
			LOG.error(String.format("Problem creating URL: %s", e.toString()));
			throw new NotBoundException_Exception("Bound String is not a valid URL!", new NotBoundException());
		}
		return wsdlLocation;
	}

	private URI lookupSavedLocation(final String name) throws NotBoundException_Exception {
		String savedLocation;
		savedLocation = registryManager.lookup(name);
		
		if (savedLocation == null || savedLocation.isEmpty()) {
			throw new NotBoundException_Exception("Bound String is empty!", new NotBoundException());
		}
		try {
			return new URI(savedLocation);
		} catch (URISyntaxException e) {
			throw new NotBoundException_Exception("Could not convert to URI! " + e.getMessage(), new NotBoundException());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.registry.client.IApplicationHelper#getComponentURI(java.lang.String)
	 */
	@Override
	public URI getComponentURI(final String name) throws NotBoundException_Exception {
		return lookupSavedLocation(name);
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.cloud.registry.client.IApplicationHelper#getComponentURL(java.lang.String)
	 */
	@Override
	public URL getComponentURL(final String name) throws MalformedURLException, NotBoundException_Exception {
		return lookupSavedLocation(name).toURL();
	}

	@Override
	public void registerComponent(String name, String location, boolean reregister) throws URISyntaxException {
		registerComponent(name, new URI(location), reregister);
	}

}
