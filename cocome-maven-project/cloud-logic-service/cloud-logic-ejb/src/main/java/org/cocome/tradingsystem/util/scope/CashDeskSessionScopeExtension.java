package org.cocome.tradingsystem.util.scope;

import java.io.Serializable;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.cocome.tradingsystem.util.scope.context.NamedSessionContext;

/**
 * Class implementing the extension to register the {@link CashDeskSessionScoped}
 * scope.
 * 
 * @author Tobias Pöppke
 *
 */
public class CashDeskSessionScopeExtension implements Extension, Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Adds the {@link CashDeskSessionScoped} scope before the bean discovery 
	 * is performed, so beans with this scope get recognized correctly.
	 * 
	 * @param event
	 * 		the event that triggers the registration
	 */
	public void addScope(@Observes final BeforeBeanDiscovery event) {
        event.addScope(CashDeskSessionScoped.class, true, false);
    }
	
	/**
	 * Registers the context for the {@link CashDeskSessionScoped} scope that
	 * contains the bean store for this scope.
	 * 
	 * @param event
	 * 		the event that triggers the registration of the context
	 * 
	 * @param manager
	 * 		the bean manager used
	 */
    public void registerContext(@Observes final AfterBeanDiscovery event, BeanManager manager) {
    	NamedSessionContext context = new NamedSessionContext(CashDeskSessionScoped.class);
        event.addContext(context);
    }
}
