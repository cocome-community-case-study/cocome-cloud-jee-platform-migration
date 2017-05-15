package org.cocome.tradingsystem.util.exception;

import javax.xml.ws.WebFault;

/**
 * Needed because EntityNotFounException resets transaction.
 * 
 * @author Tobias Pöppke
 *
 */
@WebFault
public class NotInDatabaseException extends BaseException {

	public NotInDatabaseException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
