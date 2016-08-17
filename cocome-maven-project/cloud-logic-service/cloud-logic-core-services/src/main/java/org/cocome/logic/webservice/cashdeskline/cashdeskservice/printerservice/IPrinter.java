package org.cocome.logic.webservice.cashdeskline.cashdeskservice.printerservice;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.cloud.logic.webservice.exception.UnhandledException;


@WebService(targetNamespace = "http://printer.cashdesk.cashdeskline.webservice.logic.cocome.org/")
public interface IPrinter {
	@WebMethod
	public Set<Class<?>> tearOffPrintout(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;

	@WebMethod
	public Set<Class<?>> printText(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "text") String text) throws UnhandledException;

	@WebMethod
	public String getCurrentPrintout(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName,
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID) throws UnhandledException;
	
}
