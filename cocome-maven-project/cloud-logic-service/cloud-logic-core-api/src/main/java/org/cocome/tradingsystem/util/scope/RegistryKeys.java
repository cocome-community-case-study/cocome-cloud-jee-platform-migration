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
	PLANT_ID("plant_id"),
	ENTERPRISE_ID("enterprise_id"),
	STORE_SERVICE_WSDL_LOCATION("store_service_wsdl_location");
	
	private final String name;
	
    RegistryKeys(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
