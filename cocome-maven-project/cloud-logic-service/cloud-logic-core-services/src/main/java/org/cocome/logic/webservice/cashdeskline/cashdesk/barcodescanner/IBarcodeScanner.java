package org.cocome.logic.webservice.cashdeskline.cashdesk.barcodescanner;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.inventory.application.store.Barcode;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
/**
 * Interface of a barcode scanner.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService
public interface IBarcodeScanner {
	@WebMethod
	Set<Class<?>> sendProductBarcode(@XmlElement(required = true) @WebParam(name = "cashDeskName") String cashDeskName, 
			@XmlElement(required = true) @WebParam(name = "storeID") long storeID,
			@XmlElement(required = true) @WebParam(name = "barcode") final Barcode barcode) 
					throws IllegalCashDeskStateException, NoSuchProductException, 
						UnhandledException, ProductOutOfStockException;
}
