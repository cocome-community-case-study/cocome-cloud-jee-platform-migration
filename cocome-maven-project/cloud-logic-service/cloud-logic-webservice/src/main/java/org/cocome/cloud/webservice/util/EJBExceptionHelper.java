package org.cocome.cloud.webservice.util;

public class EJBExceptionHelper {
	public static Throwable getRootCause(Throwable e) {
		Throwable cause = e;
		
		while(cause.getCause() != null) {
			cause = cause.getCause();
		}
		return cause;
	}
}
