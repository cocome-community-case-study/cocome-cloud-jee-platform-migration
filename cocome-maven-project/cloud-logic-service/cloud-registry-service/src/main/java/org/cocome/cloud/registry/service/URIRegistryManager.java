package org.cocome.cloud.registry.service;

import java.net.URI;
import java.rmi.NotBoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

@WebService(serviceName = "IURIRegistryManagerService",
			name = "IURIRegistryManager",
			endpointInterface = "org.cocome.cloud.registry.service.IURIRegistryManager",
			targetNamespace = "http://registry.webservice.logic.cocome.org/")
@Stateless
public class URIRegistryManager implements IURIRegistryManager {
	
	@Inject
	IRegistry<URI> registry;

	@Override
	public void rebind(String name, URI location) {
		registry.rebind(name, location);
	}

	@Override
	public boolean unbind(String name) {
		return registry.unbind(name);
	}

	@Override
	public URI lookup(String name) throws NotBoundException {
		return registry.lookup(name);
	}

	@Override
	public Set<RegistryEntry> getBoundNames() {
		Set<Entry<String, URI>> registryEntries = registry.getEntries();
		Set<RegistryEntry> entries = new LinkedHashSet<>(registryEntries.size(), 1);
		for (Entry<String, URI> entry : registryEntries) {
			RegistryEntry regEntry = new RegistryEntry();
			regEntry.setLocation(entry.getValue());
			regEntry.setName(entry.getKey());
			entries.add(regEntry);
		}
		return entries;
	}

}
