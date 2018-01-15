package org.cocome.cloud.logic.webservice.cashdeskline.cashdeskservice.printer;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.cocome.cloud.logic.webservice.AbstractCashDeskAction;
import org.cocome.cloud.logic.webservice.NamedCashDeskService;
import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.logic.webservice.cashdeskline.cashdeskservice.printerservice.IPrinter;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.printer.IPrinterModel;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.mvc.IContentChangedListener;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

@WebService(serviceName = "IPrinterService", 
			name = "IPrinter", 
			endpointInterface = "org.cocome.logic.webservice.cashdeskline.cashdeskservice.printerservice.IPrinter",
			targetNamespace = "http://printer.cashdesk.cashdeskline.webservice.logic.cocome.org/")
@Stateless
public class Printer extends NamedCashDeskService implements IPrinter {

	@Inject
	private IPrinterModel printer;
	
	@Inject
	private IContentChangedListener contentChanged;
	
	@Override
	public Set<Class<?>> tearOffPrintout(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				printer.tearOffPrintout();
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException e) {
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> printText(String cashDeskName, long storeID,
			final String text) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				printer.printText(text);
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException e) {
			throw new UnhandledException(e);
		}
	}

	@Override
	public String getCurrentPrintout(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<String> action = new AbstractCashDeskAction<String>() {
			@Override
			public String checkedExecute() {
				return printer.getCurrentPrintout();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException e) {
			throw new UnhandledException(e);
		}
	}

}
