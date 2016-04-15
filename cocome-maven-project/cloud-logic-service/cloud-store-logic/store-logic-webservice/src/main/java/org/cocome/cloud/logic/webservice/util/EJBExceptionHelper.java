package org.cocome.cloud.logic.webservice.util;

public class EJBExceptionHelper {
	public static Throwable getRootCause(Throwable e) {
		Throwable cause = e;
		
		while(cause.getCause() != null) {
			cause = cause.getCause();
		}
		return cause;
	}
}
