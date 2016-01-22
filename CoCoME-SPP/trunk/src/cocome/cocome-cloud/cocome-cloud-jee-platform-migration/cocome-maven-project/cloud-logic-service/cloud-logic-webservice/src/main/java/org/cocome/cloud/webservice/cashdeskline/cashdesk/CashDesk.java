package org.cocome.cloud.webservice.cashdeskline.cashdesk;

import java.io.Serializable;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.cocome.cloud.webservice.AbstractCashDeskAction;
import org.cocome.cloud.webservice.NamedCashDeskService;
import org.cocome.cloud.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.ICashDeskModelLocal;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.cashdeskline.datatypes.PaymentMode;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.mvc.IContentChangedListener;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

/**
 * 
 * @author Tobias Pöppke
 *
 */
@WebService(endpointInterface=
		"org.cocome.cloud.webservice.cashdeskline.cashdesk.ICashDesk")
@Stateless
public class CashDesk extends NamedCashDeskService implements ICashDesk, Serializable {
	
	private static final Logger LOG = Logger.getLogger(CashDesk.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7718533004116788186L;
	
	@Inject
	private ICashDeskModelLocal cashDeskModel;

	@Inject
	private IContentChangedListener contentChanged;
	
	@Override
	public boolean isInExpressMode(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Boolean> action = new AbstractCashDeskAction<Boolean>() {
			@Override
			public Boolean checkedExecute() {
				return cashDeskModel.isInExpressMode();
			}
		};
		boolean expressMode;
		try {
			expressMode = invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
		return expressMode;
	}

	@Override
	public Set<Class<?>> startSale(String cashDeskName, long storeID) throws IllegalCashDeskStateException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException {
				cashDeskModel.startSale();
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> addItemToSale(String cashDeskName, long storeID,
			final long barcode) throws IllegalCashDeskStateException, 
				NoSuchProductException, UnhandledException, ProductOutOfStockException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException, ProductOutOfStockException {
				cashDeskModel.addItemToSale(barcode);
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (UnhandledException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> finishSale(String cashDeskName, long storeID) throws IllegalCashDeskStateException, 
			ProductOutOfStockException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException {
				cashDeskModel.finishSale();
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> selectPaymentMode(String cashDeskName, long storeID,
			String mode) throws IllegalCashDeskStateException, UnhandledException, IllegalInputException {
		final PaymentMode finalMode = PaymentMode.valueOf(mode);
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException {
				cashDeskModel.selectPaymentMode(finalMode);
				return contentChanged.getChangedModels();
			}
		};
		try {
			return invokeInContext(context, action);
		} catch (ProductOutOfStockException | UnhandledException
				| NoSuchProductException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> startCashPayment(String cashDeskName, long storeID,
			final double amount) throws IllegalCashDeskStateException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException {
				cashDeskModel.startCashPayment(amount);
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> finishCashPayment(String cashDeskName, long storeID) throws IllegalCashDeskStateException,
			ProductOutOfStockException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException {
				cashDeskModel.finishCashPayment();
				return contentChanged.getChangedModels();
			}
			
		};
		
		try {
			return invokeInContext(context, action);
		} catch (NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> startCreditCardPayment(String cashDeskName, long storeID,
			final String cardInfo) throws IllegalCashDeskStateException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException {
				cashDeskModel.startCreditCardPayment(cardInfo);
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> finishCreditCardPayment(String cashDeskName, long storeID,
			final int pin) throws IllegalCashDeskStateException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() throws IllegalCashDeskStateException {
				cashDeskModel.finishCreditCardPayment(pin);
				return contentChanged.getChangedModels();
			}
			
		};

		try {
			return invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> enableExpressMode(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				LOG.error("Injected inner listener: " + contentChanged.toString());
				cashDeskModel.enableExpressMode();
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> disableExpressMode(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				cashDeskModel.disableExpressMode();
				return contentChanged.getChangedModels();
			}
			
		};
		try {
			return invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException | IllegalInputException e) {
			// None of these should happen here
			throw new UnhandledException(e);
		}
	}

}
