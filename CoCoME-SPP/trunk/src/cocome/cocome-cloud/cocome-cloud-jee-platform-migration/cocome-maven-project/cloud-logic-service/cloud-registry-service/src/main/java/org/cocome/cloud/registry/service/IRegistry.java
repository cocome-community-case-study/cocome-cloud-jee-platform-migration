package org.cocome.cloud.registry.service;

import java.net.URI;
import java.rmi.NotBoundException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Local;

@Local
public interface IRegistry<T> {

	/**
	 * Replaces the binding for the specified name in the application-wide RMI
	 * registry with the supplied remote reference. If there is an existing
	 * binding for the specified name, it is discarded.
	 * 
	 * @param name
	 *            the name to associate with the remote reference
	 * @param remote
	 *            a reference to a remote object (usually a stub)
	 * @throws RemoteException
	 *             if remote communication with the registry failed
	 */
	void rebind(String name, T entry);

	/**
	 * Removes the binding for the specified {@code name} from the
	 * application-wide RMI registry.
	 * 
	 * @param name
	 *            the name of the binding to remove
	 * @throws RemoteException
	 *             if remote communication with the registry failed
	 * @return
	 *         {@code true} if the binding has been removed, {@code false} if the name was not bound
	 */
	boolean unbind(String name);

	/**
	 * Returns the remote reference bound to the specified name in the
	 * application-wide RMI registry.
	 * 
	 * @param <T>
	 *            generic type of the remote reference; must extend {@link Remote}
	 * @param name
	 *            the name for the remote reference to look up
	 * @return
	 *         remote reference cast to the desired type
	 * @throws NotBoundException
	 *             if {@code name} is not currently bound
	 */
	T lookup(String name) throws NotBoundException;
	
	/**
	 * Retrieves all entries from this registry.
	 * 
	 * @return all entries currently in the registry.
	 */
	Set<Entry<String, URI>> getEntries();

}