package org.cocome.tradingsystem.util.exception;


public class BaseException extends Exception {

	public BaseException(String message) {
		super(message);
	}

	public BaseException() {
		super();
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
