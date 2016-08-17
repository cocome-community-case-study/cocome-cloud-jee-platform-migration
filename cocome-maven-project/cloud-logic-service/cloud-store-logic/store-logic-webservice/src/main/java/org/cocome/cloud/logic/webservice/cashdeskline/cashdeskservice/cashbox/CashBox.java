package org.cocome.cloud.logic.webservice.cashdeskline.cashdeskservice.cashbox;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.cocome.cloud.logic.webservice.AbstractCashDeskAction;
import org.cocome.cloud.logic.webservice.NamedCashDeskService;
import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.logic.webservice.cashdeskline.cashdeskservice.cashbox.ICashBox;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.ICashBoxLocal;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.cashdeskline.datatypes.ControlKeyStroke;
import org.cocome.tradingsystem.cashdeskline.datatypes.NumPadKeyStroke;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.mvc.IContentChangedListener;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

@WebService(serviceName = "ICashBoxService", 
			name = "ICashBox", 
			endpointInterface = "org.cocome.logic.webservice.cashdeskline.cashdesk.cashbox.ICashBox",
			targetNamespace = "http://cashbox.cashdesk.cashdeskline.webservice.logic.cocome.org/")
@Stateless
public class CashBox extends NamedCashDeskService implements ICashBox {
	
	@Inject
	private ICashBoxLocal cashBox;
	
	@Inject
	private IContentChangedListener contentChanged;

	@Override
	public Set<Class<?>> open(String cashDeskName, long storeID) throws IllegalCashDeskStateException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				cashBox.open();
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException | IllegalInputException e) {
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> close(String cashDeskName, long storeID) throws IllegalCashDeskStateException, 
	UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException {
				cashBox.close();
				return contentChanged.getChangedModels();
			}
		};

		try {
			return invokeInContext(context, action);
		} catch (NoSuchProductException | IllegalInputException | ProductOutOfStockException e) {
			throw new UnhandledException(e);
		}
	}

	@Override
	public boolean isOpen(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Boolean> action = new AbstractCashDeskAction<Boolean>() {
			@Override
			public Boolean checkedExecute() {
				return cashBox.isOpen();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException | IllegalInputException e) {
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> pressControlKey(String cashDeskName, long storeID,
			String keystroke) throws IllegalCashDeskStateException, UnhandledException, IllegalInputException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		final ControlKeyStroke key = ControlKeyStroke.valueOf(keystroke);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException, IllegalInputException {
				cashBox.pressControlKey(key);
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException e) {
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> pressNumpadKey(String cashDeskName, long storeID,
			String key) throws IllegalCashDeskStateException, UnhandledException, IllegalInputException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		final NumPadKeyStroke keystroke = NumPadKeyStroke.valueOf(key);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException, IllegalInputException {
				cashBox.pressNumpadKey(keystroke);
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException e) {
			throw new UnhandledException(e);
		}
	}

	@Override
	public void closeSilently(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Void> action = new AbstractCashDeskAction<Void>() {
			@Override
			public Void checkedExecute() {
				cashBox.closeSilently();
				return null;
			}
			
		};
		try {
			invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException | IllegalInputException e) {
			throw new UnhandledException(e);
		}
	}

}
