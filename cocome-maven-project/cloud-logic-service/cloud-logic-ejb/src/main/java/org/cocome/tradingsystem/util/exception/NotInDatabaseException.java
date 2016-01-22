package org.cocome.tradingsystem.util.exception;


/**
 * Needed because EntityNotFounException resets transaction.
 * 
 * @author Tobias Pöppke
 *
 */

public class NotInDatabaseException extends BaseException {

	public NotInDatabaseException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
