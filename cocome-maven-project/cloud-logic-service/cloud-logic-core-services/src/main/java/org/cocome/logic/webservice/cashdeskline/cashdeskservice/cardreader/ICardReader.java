package org.cocome.logic.webservice.cashdeskline.cashdeskservice.cardreader;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;

/**
 * Webservice interface of a card reader at a cash desk.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService
public interface ICardReader {
	@WebMethod
	public Set<Class<?>> sendCreditCardInfo(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "creditInfo") String creditInfo) 
					throws IllegalCashDeskStateException, UnhandledException;
	
	@WebMethod
	public Set<Class<?>> sendCreditCardPin(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "pin") int pin) throws IllegalCashDeskStateException, UnhandledException;	
}
