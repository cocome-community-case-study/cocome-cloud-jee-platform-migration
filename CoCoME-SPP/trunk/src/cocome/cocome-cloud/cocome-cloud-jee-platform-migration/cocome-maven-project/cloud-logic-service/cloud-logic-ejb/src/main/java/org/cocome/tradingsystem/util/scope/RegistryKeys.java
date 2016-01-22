package org.cocome.tradingsystem.util.scope;

/**
 * Enum for the keys to retrieve information from an {@link IContextRegistry} interface.
 * 
 * @author Tobias Pöppke
 *
 */
public enum RegistryKeys {
	CASH_DESK_NAME("cash_desk_name"),
	STORE_ID("store_id"),
	ENTERPRISE_ID("enterprise_id");
	
	private final String name;
	
	private RegistryKeys(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
