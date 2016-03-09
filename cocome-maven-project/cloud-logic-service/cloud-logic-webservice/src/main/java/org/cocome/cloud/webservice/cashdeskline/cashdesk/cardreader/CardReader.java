package org.cocome.cloud.webservice.cashdeskline.cashdesk.cardreader;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.cocome.cloud.webservice.AbstractCashDeskAction;
import org.cocome.cloud.webservice.NamedCashDeskService;
import org.cocome.cloud.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cardreader.ICardReaderLocal;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.mvc.ContentChangedListener;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

/**
 * Webservice interface of a card reader at a cash desk.
 * 
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@WebService(endpointInterface=
		"org.cocome.cloud.webservice.cashdeskline.cashdesk.cardreader.ICardReader")
@Stateless
public class CardReader extends NamedCashDeskService implements ICardReader {
	@Inject
	private ICardReaderLocal cardReader;
	
	@Inject
	private ContentChangedListener contentChanged;

	@Override
	public Set<Class<?>> sendCreditCardInfo(String cashDeskName, long storeID,
			final String cardInfo) throws IllegalCashDeskStateException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				cardReader.sendCreditCardInfo(cardInfo);
				return contentChanged.getChangedModels();
			}
			
		};
		
		try {
			return this.invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException | IllegalInputException e) {
			throw new UnhandledException(e);
		}
	}

	@Override
	public Set<Class<?>> sendCreditCardPin(String cashDeskName, long storeID,
			final int pin) throws IllegalCashDeskStateException, UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				cardReader.sendCreditCardPin(pin);
				return contentChanged.getChangedModels();
			}
			
		};
		
		Set<Class<?>> changedModels;
		try {
			changedModels = invokeInContext(context, action);
		} catch (ProductOutOfStockException | NoSuchProductException | IllegalInputException e) {
			throw new UnhandledException(e);
		}
		
		return changedModels;
	}

}
