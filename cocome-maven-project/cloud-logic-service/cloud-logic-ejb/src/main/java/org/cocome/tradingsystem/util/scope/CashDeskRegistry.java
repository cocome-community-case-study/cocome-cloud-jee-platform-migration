package org.cocome.tradingsystem.util.scope;

import java.util.HashMap;

/**
 * Contains information on the context in which a cash desk operation is executing.
 * 
 * @author Tobias Pöppke
 *
 */
public class CashDeskRegistry implements IContextRegistry {

	private HashMap<RegistryKeys, Object> attributeMap = new HashMap<RegistryKeys, Object>();

	private String contextName;

	/**
	 * Creates a new registry. 
	 * 
	 * @param contextName
	 * 		the name of the context in which this registry is placed
	 */
	public CashDeskRegistry(String contextName) {
		this.contextName = contextName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContextName() {
		return contextName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLong(RegistryKeys name) {
		Object v = attributeMap.get(name);

		try {
			return (Long) v;
		} catch (ClassCastException e) {
			return 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putLong(RegistryKeys name, long value) {
		attributeMap.put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(RegistryKeys name) {
		Object v = attributeMap.get(name);

		try {
			return (String) v;
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putString(RegistryKeys name, String value) {
		attributeMap.put(name, value);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObject(RegistryKeys name) {
		Object v = attributeMap.get(name);

		try {
			return (Object) v;
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putObject(RegistryKeys name, Object value) {
		attributeMap.put(name, value);
	}

	@Override
	public boolean hasObject(RegistryKeys name) {
		return attributeMap.containsKey(name);
	}

	@Override
	public boolean hasLong(RegistryKeys name) {
		return attributeMap.containsKey(name);
	}

}
