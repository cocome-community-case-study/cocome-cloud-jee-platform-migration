package org.cocome.tradingsystem.inventory.data.persistence;

public class UpdateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpdateException(String string, Throwable cause) {
		super(string, cause);
	}

	public UpdateException(String string) {
		super(string);
	}
	
}
