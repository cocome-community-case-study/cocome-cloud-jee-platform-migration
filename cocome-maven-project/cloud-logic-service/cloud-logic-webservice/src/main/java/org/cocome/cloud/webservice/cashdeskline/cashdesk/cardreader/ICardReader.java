package org.cocome.cloud.webservice.cashdeskline.cashdesk.cardreader;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.cloud.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;

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
