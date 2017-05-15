package org.cocome.tradingsystem.inventory.application.store;

import javax.xml.ws.WebFault;

import org.cocome.tradingsystem.util.exception.BaseException;


@WebFault
public class NotImplementedException extends BaseException {

	public NotImplementedException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
