package org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox;

import org.cocome.tradingsystem.util.exception.BaseException;

/**
 * Signals an illegal input. 
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public class IllegalInputException extends BaseException {

	public IllegalInputException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
