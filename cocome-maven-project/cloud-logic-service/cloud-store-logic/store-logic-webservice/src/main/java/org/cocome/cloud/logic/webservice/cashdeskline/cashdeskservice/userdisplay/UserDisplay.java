package org.cocome.cloud.logic.webservice.cashdeskline.cashdeskservice.userdisplay;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.cocome.cloud.logic.webservice.AbstractCashDeskAction;
import org.cocome.cloud.logic.webservice.NamedCashDeskService;
import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.logic.webservice.cashdeskline.cashdeskservice.userdisplay.IUserDisplay;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.userdisplay.IUserDisplayLocal;
import org.cocome.tradingsystem.cashdeskline.cashdesk.userdisplay.MessageKind;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

@WebService(serviceName = "IUserDisplayService", 
			name = "IUserDisplay", 
			endpointInterface = "org.cocome.logic.webservice.cashdeskline.cashdesk.userdisplay.IUserDisplay",
			targetNamespace = "http://userdisplay.cashdesk.cashdeskline.webservice.logic.cocome.org/")
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
