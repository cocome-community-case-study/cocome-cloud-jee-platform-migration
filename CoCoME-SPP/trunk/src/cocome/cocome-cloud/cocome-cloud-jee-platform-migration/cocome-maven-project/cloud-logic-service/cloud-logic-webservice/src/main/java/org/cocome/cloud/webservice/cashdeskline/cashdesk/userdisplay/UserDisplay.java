package org.cocome.cloud.webservice.cashdeskline.cashdesk.userdisplay;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.cocome.cloud.webservice.AbstractCashDeskAction;
import org.cocome.cloud.webservice.NamedCashDeskService;
import org.cocome.cloud.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.userdisplay.IUserDisplayLocal;
import org.cocome.tradingsystem.cashdeskline.cashdesk.userdisplay.MessageKind;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

@WebService(endpointInterface=
		"org.cocome.cloud.webservice.cashdeskline.cashdesk.userdisplay.IUserDisplay")
@Stateless
public class UserDisplay extends NamedCashDeskService implements IUserDisplay {
	
	@Inject
	private IUserDisplayLocal userDisplay;

	@Override
	public String getMessage(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<String> action = new AbstractCashDeskAction<String>() {
			@Override
			public String checkedExecute() {
				return userDisplay.getMessage();
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
	public MessageKind getMessageKind(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<MessageKind> action = new AbstractCashDeskAction<MessageKind>() {
			@Override
			public MessageKind checkedExecute() {
				return userDisplay.getMessageKind();
			}
		};
		try {
			return invokeInContext(context, action);
		} catch (IllegalCashDeskStateException | ProductOutOfStockException
				| NoSuchProductException | IllegalInputException e) {
			throw new UnhandledException(e);
		}
	}

}
