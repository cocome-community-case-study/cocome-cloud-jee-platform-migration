package org.cocome.cloud.logic.webservice;

import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

/**
 * Base class for a named webservice which requires the activation of 
 * a context before executing an action.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 *
 */
public abstract class AbstractNamedService {

	/**
	 * Abstract class constructor. Doesn't actually instantiate anything.
	 */
	public AbstractNamedService() {
		super();
	}

	/**
	 * Deletes the currently active session. After this the session 
	 * will not be available any longer.
	 */
	public abstract void deleteCurrentSession();

	/**
	 * Deactivates the currently active session. The session is 
	 * still in memory but not active after calling this method.
	 */
	public abstract void deactivateCurrentSession();

	/**
	 * Activates a named session with the given context registry.
	 * During this session it is possible for any object to inject the
	 * registry and get the information stored there.
	 * 
	 * @param context
	 * 			the context with additional information
	 */
	public abstract void activateNamedSession(IContextRegistry context);
	
	public <T> T invokeInContext(IContextRegistry context, IContextAction<T> action) throws UnhandledException, 
			IllegalCashDeskStateException, ProductOutOfStockException, NoSuchProductException,
			IllegalInputException {
		activateNamedSession(context);
		T result = null;
		try {
			result = action.execute();
		} finally {
			deactivateCurrentSession();
		}
		return result;
	}
	
	public <T> T invokeAndDeleteContext(IContextRegistry context, IContextAction<T> action) throws UnhandledException, 
			IllegalCashDeskStateException, ProductOutOfStockException, NoSuchProductException, 
			IllegalInputException {
		activateNamedSession(context);
		T result = null;
		try {
			result = action.execute();
		} finally {
			deleteCurrentSession();
		}
		return result;
	}

}