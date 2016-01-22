package org.cocome.tradingsystem.util.scope;

/**
 * Interface for objects containing information about the current context.
 * 
 * @author Tobias Pöppke
 *
 */
public interface IContextRegistry {

	/**
	 * Returns the name of this specific Context.
	 * This name is used to address a named session scope and manage it.
	 * 
	 * @return the name of this context
	 */
	public String getContextName();
	
	/**
	 * Retrieves a long number that was put into the registry before.
	 * 
	 * @see RegistryKeys
	 * 
	 * @param name
	 * 		the name of this number
	 * 
	 * @return the saved long number or 0 if it's not present
	 */
	public long getLong(RegistryKeys name);
	
	/**
	 * Puts a long number under the specified name into the registry.
	 * 
	 * @see RegistryKeys
	 * 
	 * @param name 
	 * 		the name of this number
	 * @param value
	 * 		the value of this number
	 */
	public void putLong(RegistryKeys name, long value);
	
	/**
	 * Retrieves a String that was put into the registry before.
	 * 
	 * @see RegistryKeys
	 * 
	 * @param name
	 * 		the name of this String
	 * 
	 * @return the saved String or null if it's not present
	 */
	public String getString(RegistryKeys name);
	
	/**
	 * Puts a String under the specified name into the registry.
	 * 
	 * @see RegistryKeys
	 * 
	 * @param name 
	 * 		the name of this String
	 * @param value
	 * 		the value of this String
	 */
	public void putString(RegistryKeys name, String value);
	
	/**
	 * Retrieves an object that was put into the registry before.
	 * 
	 * @see RegistryKeys
	 * 
	 * @param name
	 * 		the name of this object
	 * 
	 * @return the saved object or null if it's not present
	 */
	public Object getObject(RegistryKeys name);
	
	/**
	 * Puts an object under the specified name into the registry.
	 * 
	 * @see RegistryKeys
	 * 
	 * @param name 
	 * 		the name of this object
	 * @param value
	 * 		the value of this object
	 */
	public void putObject(RegistryKeys name, Object value);
	
	/**
	 * Checks for the specified registry key if there is an object stored with this 
	 * key in this registry.
	 * 
	 * @param name
	 * 		the name of the registry key
	 * @return
	 * 		true if an object is stored with this key
	 */
	public boolean hasObject(RegistryKeys name);
	
	/**
	 * Checks for the specified registry key if there is a long value stored with this 
	 * key in this registry.
	 * 
	 * @param name
	 * 		the name of the registry key
	 * @return
	 * 		true if a long value is stored with this key
	 */
	public boolean hasLong(RegistryKeys name);
}