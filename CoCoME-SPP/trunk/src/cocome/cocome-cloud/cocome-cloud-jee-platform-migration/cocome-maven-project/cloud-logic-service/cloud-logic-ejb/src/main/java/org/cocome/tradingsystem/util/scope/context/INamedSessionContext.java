package org.cocome.tradingsystem.util.scope.context;

import javax.enterprise.context.spi.Context;

import org.cocome.tradingsystem.util.scope.IContextRegistry;

/**
 * Interface for classes that manage the context of a named session. 
 * A named session context is accessed via its name and stores the beans
 * in a context holder until they are deleted.
 * 
 * @author Tobias Pöppke
 *
 */
public interface INamedSessionContext extends Context {

	/**
	 * 
	 * @param context
	 * @return
	 */
	public boolean activate(IContextRegistry context);

	/**
	 * 
	 * 
	 */
	public void deactivateCurrentContext();
	
	/**
	 * 
	 * 
	 */
	public void deleteCurrentContext();
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * 
	 * 
	 * @return
	 */
	public IContextRegistry getActiveContext();
}