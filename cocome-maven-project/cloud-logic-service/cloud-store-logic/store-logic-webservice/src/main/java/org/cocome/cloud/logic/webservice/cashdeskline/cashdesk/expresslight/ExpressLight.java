package org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.expresslight;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.cocome.cloud.logic.webservice.AbstractCashDeskAction;
import org.cocome.cloud.logic.webservice.NamedCashDeskService;
import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.logic.webservice.cashdeskline.cashdesk.expresslight.IExpressLight;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.expresslight.IExpressLightLocal;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.mvc.IContentChangedListener;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

@WebService(serviceName = "IExpressLightService", 
			name = "IExpressLight", 
			endpointInterface = "org.cocome.logic.webservice.cashdeskline.cashdesk.expresslight.IExpressLight",
			targetNamespace = "http://expresslight.cashdesk.cashdeskline.webservice.logic.cocome.org/")
@Stateless
public class ExpressLight extends NamedCashDeskService implements IExpressLight {

	@Inject
	private IExpressLightLocal expressLight;
	
	@Inject
	private IContentChangedListener contentChanged;
	
	
	@Override
	public Set<Class<?>> turnExpressLightOn(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				expressLight.turnExpressLightOn();
				return contentChanged.getChangedModels();
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
	public Set<Class<?>> turnExpressLightOff(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
			@Override
			public Set<Class<?>> checkedExecute() {
				expressLight.turnExpressLightOff();
				return contentChanged.getChangedModels();
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
	public boolean isOn(String cashDeskName, long storeID) throws UnhandledException {
		IContextRegistry context = getContextRegistry(cashDeskName, storeID);
		
		AbstractCashDeskAction<Boolean> action = new AbstractCashDeskAction<Boolean>() {
			@Override
			public Boolean checkedExecute() {
				return expressLight.isOn();
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
