package org.cocome.cloud.webservice;

import org.cocome.cloud.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;

/**
 * Interface representing an action to be executed while a specific context is active.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 *
 * @param <T> the return type of this action
 */
public interface IContextAction<T> {
	/**
	 * Executes the specified action.
	 * 
	 * @return the result of this action
	 * @throws UnhandledException 
	 */
	public T execute() throws IllegalCashDeskStateException, 
				NoSuchProductException, ProductOutOfStockException, UnhandledException;
}
