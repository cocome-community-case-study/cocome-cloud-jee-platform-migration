package org.cocome.cloud.logic.webservice.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@WebFault
public class UnhandledException extends Exception {

	private Throwable cause;
	
	public UnhandledException(Exception cause) {
		super("Unhandled exception encountered!", cause);
		this.cause = cause;
	}

	public UnhandledException() {
		super("Unhandled exception encountered!");
		this.cause = null;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Throwable getCause() {
		return cause;
	}
	
	@Override
	public Throwable initCause(Throwable cause) {
		this.cause = cause;
		return super.initCause(cause);
	}
}
