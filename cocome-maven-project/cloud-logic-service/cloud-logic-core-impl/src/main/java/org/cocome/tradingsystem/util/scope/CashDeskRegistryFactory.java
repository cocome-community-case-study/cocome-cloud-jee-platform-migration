package org.cocome.tradingsystem.util.scope;

import java.io.Serializable;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.cocome.tradingsystem.util.qualifier.CashDeskRequired;
import org.cocome.tradingsystem.util.qualifier.EnterpriseRequired;
import org.cocome.tradingsystem.util.qualifier.StoreRequired;
import org.cocome.tradingsystem.util.scope.context.INamedSessionContext;

/**
 * Contains information about the currently active CashDeskRegistry and
 * produces the correct registry for injection via {@code @Inject} into the 
 * current request.
 * 
 * @see IContextRegistry
 * 
 * @author Tobias Pöppke
 *
 */
@RequestScoped
public class CashDeskRegistryFactory implements ICashDeskRegistryFactory, Serializable {

	private static final long serialVersionUID = 3603943841760599147L;

	@Inject
	private BeanManager manager;
	
	// TODO Change this to a map or something, so it is easier to extend
	private IContextRegistry storeContext = null;
	
	private IContextRegistry enterpriseContext = null;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStoreContext(IContextRegistry storeContext) {
		this.storeContext = storeContext;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Produces @CashDeskSessionScoped @CashDeskRequired
	public IContextRegistry getCashDeskContextRegistry() throws ContextNotActiveException {
		INamedSessionContext sessionContext = (INamedSessionContext) manager.getContext(CashDeskSessionScoped.class);

		return sessionContext.getActiveContext();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Produces @StoreRequired
	public IContextRegistry getStoreContextRegistry() throws ContextNotActiveException {
		IContextRegistry context;
		try {
			context = getCashDeskContextRegistry();
		} catch (ContextNotActiveException e) {
			context = storeContext;
		}
		
		if (context == null) {
			throw new ContextNotActiveException("Could not access store context information to create IContextRegistry!");
		}
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnterpriseContext(IContextRegistry enterpriseContext) {
		this.enterpriseContext = enterpriseContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Produces @EnterpriseRequired
	public IContextRegistry getEnterpriseContextRegistry()
			throws ContextNotActiveException {
		if (enterpriseContext != null) {
			return enterpriseContext;
		} else if (storeContext != null && storeContext.hasLong(RegistryKeys.ENTERPRISE_ID)){
			return storeContext;
		}
		throw new ContextNotActiveException("Could not access enterprise information to create IContextRegistry!");
	}
	
}
