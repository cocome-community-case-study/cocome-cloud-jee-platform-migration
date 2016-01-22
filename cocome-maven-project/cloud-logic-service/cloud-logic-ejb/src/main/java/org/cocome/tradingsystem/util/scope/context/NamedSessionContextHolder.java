package org.cocome.tradingsystem.util.scope.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

/**
 * 
 * 
 * @author Tobias Pöppke
 *
 */
public class NamedSessionContextHolder implements Serializable {
	
	private static final long serialVersionUID = -1161964983685220604L;
	
	private static Map<String, NamedSessionContextHolder> INSTANCES;
	
	private Map<Class<?>, NamedSessionInstance<?>> beans;
	
	private String contextName;
	
	/**
	 * 
	 * 
	 */
	private NamedSessionContextHolder() {
		this.beans = Collections.synchronizedMap(new HashMap<Class<?>, NamedSessionInstance<?>>());
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public synchronized static Map<String, NamedSessionContextHolder> getInstances() {
		return INSTANCES;
	}
	
	/**
	 * 
	 * 
	 * @param name
	 * @return
	 */
	public synchronized static NamedSessionContextHolder getInstance(String name) {
		if (INSTANCES == null) {
			INSTANCES = Collections.synchronizedMap(new HashMap<String, NamedSessionContextHolder>());
		}
		
		NamedSessionContextHolder contextHolder = INSTANCES.get(name);
		
		if(contextHolder == null) {
			contextHolder = new NamedSessionContextHolder();
			INSTANCES.put(name, contextHolder);
		}
		contextHolder.setContextName(name);
		
		return contextHolder;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
    public Map<Class<?>, NamedSessionInstance<?>> getBeans() {
        return beans;
    }
 
    /**
     * 
     * 
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> NamedSessionInstance<T> getBean(Class<T> type) {
    	// Cast is no problem because the stored instance is always of type T
        return (NamedSessionInstance<T>) getBeans().get(type);
    }
 
    /**
     * 
     * 
     * @param cashDeskInstance
     */
    public <T> void putBean(NamedSessionInstance<T> cashDeskInstance) {
        getBeans().put(cashDeskInstance.bean.getBeanClass(), cashDeskInstance);
    }
 
    /**
     * 
     * 
     * @param cashDeskInstance
     */
    public <T> void destroyBean(NamedSessionInstance<T> cashDeskInstance) {
        getBeans().remove(cashDeskInstance.bean.getBeanClass());
        destroyConcreteBean(cashDeskInstance);
    }
    
    /**
     * 
     * 
     */
    public synchronized void destroyAllBeans() {
    	Set<Entry<Class<?>, NamedSessionInstance<?>>> entrySet = getBeans().entrySet();
    	
    	Iterator<Entry<Class<?>, NamedSessionInstance<?>>> entryIt = entrySet.iterator();
    	
    	ArrayList<NamedSessionInstance<?>> toDelete = new ArrayList<NamedSessionInstance<?>>(entrySet.size());
    	
    	while (entryIt.hasNext()) {
    		Entry<Class<?>, NamedSessionInstance<?>> entry = entryIt.next();
    		toDelete.add(entry.getValue());
    		entryIt.remove();
    	}
    	
    	for (NamedSessionInstance<?> instance : toDelete) {
    		destroyConcreteBean(instance);
    	}
    }
    
    private <T> void destroyConcreteBean(NamedSessionInstance<T> cashDeskInstance) {
        cashDeskInstance.bean.destroy(cashDeskInstance.instance, cashDeskInstance.ctx);
    }
    
    private void setContextName(String name) {
    	this.contextName = name;
    }
    
    /**
     * 
     * 
     * @return
     */
    public String getContextName() {
    	return this.contextName;
    }
	
    /**
     * wrap necessary properties so we can destroy the bean later:
     **/
    public static class NamedSessionInstance<T> {
 
        Bean<T> bean;
        CreationalContext<T> ctx;
        T instance;
    }

}
