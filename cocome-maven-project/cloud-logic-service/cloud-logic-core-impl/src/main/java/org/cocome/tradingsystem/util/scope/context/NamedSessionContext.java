package org.cocome.tradingsystem.util.scope.context;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.context.NamedSessionContextHolder.NamedSessionInstance;

/**
 * Implements a named session context with a {@link ThreadLocal} context holder.
 * 
 * @author Tobias Pöppke
 *
 */
public class NamedSessionContext implements Serializable, INamedSessionContext {

	private static final long serialVersionUID = -4691689443732680725L;
	
	/*
	 * ThreadLocals are necessary here to prevent that two different contexts get activated
	 * and the one last activated receives all the calls. With ThreadLocals this won't happen
	 * and there will be no ContextNotActiveExceptions.
	 */
	
	private ThreadLocal<Boolean> isActive;
	
	private ThreadLocal<Class<? extends Annotation>> providedScope;
	
	private static final Logger LOG = Logger.getLogger(NamedSessionContext.class);
	
	private ThreadLocal<IContextRegistry> context;
	
	private ThreadLocal<NamedSessionContextHolder> contextHolder;
	
	public NamedSessionContext(Class<? extends Annotation> providedScope) {
		isActive = new ThreadLocal<>();
		this.providedScope = new ThreadLocal<>();
		context = new ThreadLocal<>();
		contextHolder = new ThreadLocal<>();
		
		isActive.set(false);
		context.set(null);
		contextHolder.set(null);
		this.providedScope.set(providedScope);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends Annotation> getScope() {
		return providedScope.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Contextual<T> contextual,
			CreationalContext<T> creationalContext) {
		Bean<T> bean = (Bean<T>) contextual;
		NamedSessionContextHolder contextHolder = getActiveHolder();
		LOG.debug("get with contextual " + contextual + " and creationalContext " + creationalContext + " with context holder " + contextHolder);
		if (contextHolder == null) {
			throw new ContextNotActiveException("No context holder currently active.");
		}
		
        if (contextHolder.getBeans().containsKey(bean.getBeanClass())) {
        	// Instance class should always be of type T so casting it is ok
        	T instance = (T) contextHolder.getBean(bean.getBeanClass()).instance;
        	LOG.debug("returning Bean instance " + instance.toString() + " from holder");
            return instance;
        } else {
        	if (creationalContext == null) {
        		return null;
        	}
        	
            T t = bean.create(creationalContext);
            NamedSessionInstance<T> customInstance = new NamedSessionInstance<>();
            customInstance.bean = bean;
            customInstance.ctx = creationalContext;
            customInstance.instance = t;
            contextHolder.putBean(customInstance);
            LOG.debug("returning newly created bean " + t.toString());
            return t;
        }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(Contextual<T> contextual) {
		return this.get(contextual, null);
	}
	
	private NamedSessionContextHolder getActiveHolder() {		
		return contextHolder.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		// TODO: cannot inject and activate context with real status...
		// return this.isActive;
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean activate(IContextRegistry context) {
		if (context != null && !context.getContextName().equals("")) {
			this.context.set(context);
			isActive.set(true);
			NamedSessionContextHolder holder = NamedSessionContextHolder.getInstance(getName());
			this.contextHolder.set(holder);			
		} else {
			isActive.set(false);
		}
		LOG.debug("activating context: " + (isActive.get() ? getName(): "No context found!"));
		return isActive.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deactivateCurrentContext() {
		LOG.debug("deactivating context " + getName());
		context.remove();
		contextHolder.remove();
		isActive.remove();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return context.get().getContextName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteCurrentContext() {
		String contextName = getName();
		getActiveHolder().destroyAllBeans();
		deactivateCurrentContext();
		NamedSessionContextHolder.getInstances().remove(contextName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IContextRegistry getActiveContext() {
		if (this.context.get() == null) {
			throw new ContextNotActiveException("Could not return context object because no context is currently active.");
		} else {
			return this.context.get();
		}
	}

}
