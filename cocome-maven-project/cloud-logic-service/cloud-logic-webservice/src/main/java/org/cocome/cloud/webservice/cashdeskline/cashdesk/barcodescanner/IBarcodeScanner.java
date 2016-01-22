package org.cocome.cloud.webservice.cashdeskline.cashdesk.barcodescanner;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.cloud.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;

@WebService
public interface IBarcodeScanner {
	@WebMethod
	Set<Class<?>> sendProductBarcode(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "barcode") final long barcode) 
					throws IllegalCashDeskStateException, NoSuchProductException, 
						UnhandledException, ProductOutOfStockException;
}
