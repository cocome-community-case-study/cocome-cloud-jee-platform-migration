package org.cocome.cloud.webservice;

import javax.ejb.EJBException;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.cocome.tradingsystem.util.scope.CashDeskRegistry;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;
import org.cocome.tradingsystem.util.scope.context.INamedSessionContext;

/**
 * Class representing a named service for a specific cash desk.
 * That means this service provides the means for activating, deactivating 
 * and deleting a cash desk session context and makes sure that all 
 * necessary information is included as an {@code IContextRegistry}.
 * 
 * @author Tobias Pöppke
 *
 */

// TODO Perhaps use interceptors on the methods to activate the context?

public abstract class NamedCashDeskService extends AbstractNamedService {
	@Inject 
	private BeanManager manager;
	
	private INamedSessionContext cashDeskSessionContext;
	
	/**
	 * Creates a new {@code CashDeskRegistry} with the given cash desk information.
	 * This registry can be injected by the cash desk components to obtain 
	 * needed information on the active cash desk.
	 * 
	 * @param cashDeskName 
	 * 			the name of the active cash desk
	 * 
	 * @param storeID
	 * 			the id of the store in which the cash desk is located
	 * 
	 * @return the context registry with the given information
	 */
	protected IContextRegistry getContextRegistry(String cashDeskName, long storeID) {
		CashDeskRegistry registry = new CashDeskRegistry(cashDeskName + "#" + storeID);
		registry.putString(RegistryKeys.CASH_DESK_NAME, cashDeskName);
		registry.putLong(RegistryKeys.STORE_ID, storeID);
		return registry;
	}

	// Retrieves the CashDeskSessionContext from the BeanManager
	private void populateContext() {
		cashDeskSessionContext = (INamedSessionContext) manager.getContext(CashDeskSessionScoped.class);
 	}
	
	@Override
	public void activateNamedSession(IContextRegistry context) {
		populateContext();
		cashDeskSessionContext.activate(context);
	}
	
	@Override
	public void deactivateCurrentSession() {
		populateContext();
		cashDeskSessionContext.deactivateCurrentContext();
	}
	
	@Override
	public void deleteCurrentSession() {
		populateContext();
		cashDeskSessionContext.deleteCurrentContext();
	}

	protected void handleException(Exception e) throws Exception {
		if (e instanceof EJBException) {
			
		}
	}
	
}
