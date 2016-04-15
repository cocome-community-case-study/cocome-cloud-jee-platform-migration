package org.cocome.tradingsystem.util.scope;

import javax.ejb.Local;
import javax.enterprise.context.ContextNotActiveException;

/**
 * Factory for {@link IContextRegistry} objects to be used when injecting
 * those objects into other objects. 
 * 
 * @author Tobias Pöppke
 *
 */
@Local
public interface ICashDeskRegistryFactory {

	/**
	 * Set a context registry which contains information about the current store.
	 * If no store information can be extracted from the current context the 
	 * context that was set with this method is used for injection.
	 * 
	 * @param storeContext
	 * 		the context information to use as store context if no other 
	 * 		context information is available
	 */
	public void setStoreContext(IContextRegistry storeContext);
	
	/**
	 * Set a context registry which contains information about the current enterprise.
	 * If no enterprise information can be extracted from the current context the 
	 * context that was set with this method is used for injection.
	 * 
	 * @param enterpriseContext
	 * 		the context information to use as store context if no other 
	 * 		context information is available
	 */
	public void setEnterpriseContext(IContextRegistry enterpriseContext);

	/**
	 * Get a context with information on the currently active cash desk.
	 * This context will contain information on the cash desks name and the 
	 * store in which it is located. 
	 * 
	 * @throws ContextNotActiveException in case no cash desk context is active
	 * 
	 * @return
	 * 		the current cash desk context
	 */
	public IContextRegistry getCashDeskContextRegistry()  throws ContextNotActiveException;

	/**
	 * Produces an {@link IContextRegistry} object that contains the current context's information.
	 * If a {@link CashDeskSessionScoped} session is active, the context information is taken from 
	 * the session. Otherwise it will return a stored store context that can be set through 
	 * {@code setStoreContext()}.
	 * 
	 * @throws ContextNotActiveException in case no context information was found.
	 * 
	 * @return
	 * 		the current context with store information
	 */
	public IContextRegistry getStoreContextRegistry()
			throws ContextNotActiveException;
	
	/**
	 * Produces an {@link IContextRegistry} object that contains the current context's information.
	 * This will return any context information set via {@code setEnterpriseContext()}.
	 * 
	 * @throws ContextNotActiveException in case no context information was found.
	 * 
	 * @return
	 * 		the current context with enterprise information
	 */
	public IContextRegistry getEnterpriseContextRegistry() 
			throws ContextNotActiveException;

}