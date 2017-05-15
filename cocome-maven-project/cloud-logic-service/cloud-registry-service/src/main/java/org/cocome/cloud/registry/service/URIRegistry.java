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

package org.cocome.cloud.registry.service;

import java.net.URI;
import java.rmi.NotBoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.log4j.Logger;

@Singleton
@Startup
public class URIRegistry implements IRegistry<URI> {

	private static final Logger LOG = Logger.getLogger(URIRegistry.class);

	private Map<String, URI> registryMap = new LinkedHashMap<>();
	
	//



	/* (non-Javadoc)
	 * @see org.cocome.registry.service.IRegistry#rebind(java.lang.String, java.net.URI)
	 */
	@Override
	public void rebind(final String name, final URI location) {
		URIRegistry.assertStringNotEmpty(name);
		URIRegistry.assertObjectNotNull(location);

		registryMap.put(name, location);
		
		LOG.debug(String.format(
		"Registered %s as %s in URI registry",
		location, name));
	}

	/* (non-Javadoc)
	 * @see org.cocome.registry.service.IRegistry#unbind(java.lang.String)
	 */
	@Override
	public boolean unbind(
			final String name
			) {

		URIRegistry.assertStringNotEmpty(name);
		
		URI removedEntry = registryMap.remove(name);
		
		if (removedEntry == null) {
			//
			// Don't make too much fuss about unbound names, just allow to
			// check for it if interested...
			//
			LOG.debug(String.format(
					"Name not bound, cannot unregister %s from URI registry",
					name));
			return false;
		} else {
			LOG.debug(String.format(
			"Unregistered %s from URI registry",
			name));
			return true;
		}

	}

	/* (non-Javadoc)
	 * @see org.cocome.registry.service.IRegistry#lookup(java.lang.String, java.lang.Class)
	 */
	@Override
	public URI lookup(
			final String name) throws NotBoundException {

		URIRegistry.assertStringNotEmpty(name);
		
		URI location = registryMap.get(name);
		
		if (location == null) {
			LOG.error(String.format(
					"URI object with name %s not found",
					name));
			throw new NotBoundException("Object not bound in registry");
		}
		return location;
	}
	//

	private static void assertObjectNotNull(final Object object) {
		assert object != null;
	}

	private static void assertStringNotEmpty(final String string) {
		URIRegistry.assertObjectNotNull(string);
		assert !string.isEmpty();
	}

	@Override
	public Set<Entry<String, URI>> getEntries() {
		return registryMap.entrySet();
	}

}
