package org.cocome.cloud.logic.webservice.cashdeskline.cashdeskservice.barcodescanner;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.cocome.cloud.logic.webservice.AbstractCashDeskAction;
import org.cocome.cloud.logic.webservice.NamedCashDeskService;
import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.logic.webservice.cashdeskline.cashdeskservice.barcodescannerservice.IBarcodeScanner;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.barcodescanner.IBarcodeScannerLocal;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.mvc.IContentChangedListener;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

/**
 * Webservice interface for the barcode scanner of a cash desk.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService(serviceName = "IBarcodeScannerService", 
			name = "IBarcodeScanner", 
			endpointInterface = "org.cocome.logic.webservice.cashdeskline.cashdeskservice.barcodescannerservice.IBarcodeScanner",
			targetNamespace = "http://barcodescanner.cashdesk.cashdeskline.webservice.logic.cocome.org/")
@Stateless
public class BarcodeScanner extends NamedCashDeskService implements IBarcodeScanner {

	@Inject
	private IBarcodeScannerLocal barcodeScanner;
	
	@Inject
	private IContentChangedListener contentChanged;
	
	@Override
	public Set<Class<?>> sendProductBarcode(String cashDeskName, long storeID,
			final long barcode) throws IllegalCashDeskStateException, 
				NoSuchProductException, UnhandledException, ProductOutOfStockException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				barcodeScanner.sendProductBarcode(barcode);
				return contentChanged.getChangedModels();
			}
			
		};
		
		try {
			return this.invokeInContext(context, action);
		} catch (IllegalInputException e) {
			throw new UnhandledException(e);
		}
	}

}
