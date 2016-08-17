package org.cocome.logic.webservice.cashdeskline.cashdeskservice.cashboxservice;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;

@WebService(targetNamespace = "http://cashbox.cashdesk.cashdeskline.webservice.logic.cocome.org/")
public interface ICashBox {
	@WebMethod
	public Set<Class<?>> open(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws IllegalCashDeskStateException, 
			UnhandledException;

	@WebMethod
	public Set<Class<?>> close(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws IllegalCashDeskStateException, 
			UnhandledException;

	@WebMethod
	public boolean isOpen(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;

	@WebMethod
	public Set<Class<?>> pressControlKey(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID, 
			@XmlElement(required = true) @WebParam(name = "keystroke")String keystroke) throws IllegalCashDeskStateException, 
			UnhandledException, IllegalInputException;

	@WebMethod
	public Set<Class<?>> pressNumpadKey(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "key") String key) throws IllegalCashDeskStateException, UnhandledException, IllegalInputException;

	@WebMethod
	public void closeSilently(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;
}
