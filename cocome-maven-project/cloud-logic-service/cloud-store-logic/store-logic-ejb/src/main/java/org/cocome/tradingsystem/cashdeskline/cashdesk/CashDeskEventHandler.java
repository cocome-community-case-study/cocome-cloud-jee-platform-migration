/***************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.cocome.tradingsystem.cashdeskline.cashdesk;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.events.CashAmountEnteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.CashBoxClosedEvent;
import org.cocome.tradingsystem.cashdeskline.events.CreditCardPinEnteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.CreditCardScannedEvent;
import org.cocome.tradingsystem.cashdeskline.events.ExpressModeDisabledEvent;
import org.cocome.tradingsystem.cashdeskline.events.ExpressModeEnabledEvent;
import org.cocome.tradingsystem.cashdeskline.events.PaymentModeSelectedEvent;
import org.cocome.tradingsystem.cashdeskline.events.ProductBarcodeScannedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleFinishedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleStartedEvent;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;


/**
 * Implements the business logic of the cash desk. The controller handles events
 * received by the cash desk and translates them to invocations on the cash desk
 * model. The cash desk model is stateful, so most methods can only be called in
 * certain state. If an event is received that results in illegal method
 * invocation on the model, the event is logged and ignored.
 * <p>
 * The state is kept in the model to enforce sequencing, because the model will emit events in response to method invocations. This allows to control the model from
 * outside.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */

@Dependent
class CashDeskEventHandler implements ICashDeskEventHandler, Serializable {

	private static final long serialVersionUID = -113764312663218734L;

	private static final Logger LOG = Logger.getLogger(CashDeskEventHandler.class);
	
	@Inject
	private ICashDeskModel cashDesk;
	
	//
	// Event handler methods
	//

	public void onEvent(@Observes SaleStartedEvent event) throws IllegalCashDeskStateException {
		// These ifs are necessary to avoid reentrant errors when event is sent from the cash desk model
		if (this.cashDesk.getState() == CashDeskState.EXPECTING_ITEMS) {
			LOG.debug("Already expecting items, ignoring event...");
		} else {
			this.cashDesk.startSale();
		}
	}

	public void onEvent(@Observes SaleFinishedEvent event) throws IllegalCashDeskStateException {
		if (this.cashDesk.getState() == CashDeskState.EXPECTING_PAYMENT) {
			LOG.debug("Already expecting payment, ignoring event...");
		} else {
			this.cashDesk.finishSale();
		}
	}
	
	public void onEvent(@Observes PaymentModeSelectedEvent event) throws IllegalCashDeskStateException {
		if (this.cashDesk.getState() == CashDeskState.PAYING_BY_CASH || 
				this.cashDesk.getState() == CashDeskState.EXPECTING_CARD_INFO) {
			LOG.debug("Payment mode already selected, ignoring event...");
		} else {
			this.cashDesk.selectPaymentMode(event.getMode());
		}
	}

	public void onEvent(@Observes ExpressModeDisabledEvent event) {
		if (this.cashDesk.isInExpressMode()) {
			this.cashDesk.disableExpressMode();
		} else {
			LOG.debug("Express mode already active, ignoring event...");
		}
	}

	public void onEvent(@Observes ProductBarcodeScannedEvent event) throws IllegalCashDeskStateException, ProductOutOfStockException {
		final long barcode = event.getBarcode();
		LOG.debug("\tbarcode: " + barcode);

		//

		this.cashDesk.addItemToSale(barcode);
	}

	public void onEvent(@Observes CashAmountEnteredEvent event) throws IllegalCashDeskStateException {
		if (this.cashDesk.getState() == CashDeskState.PAID_BY_CASH) {
			LOG.debug("Already paid by cash, ignoring event...");
		} else {
			final double cashAmount = event.getCashAmount();
			LOG.debug("\tcashAmount: " + cashAmount);

			this.cashDesk.startCashPayment(cashAmount);
		}
	}

	public void onEvent(@Observes CashBoxClosedEvent event) throws IllegalCashDeskStateException {
		this.cashDesk.finishCashPayment();
	}

	public void onEvent(@Observes CreditCardScannedEvent event) throws IllegalCashDeskStateException {
		final String cardInfo = event.getCreditCardInformation();
		LOG.debug("\tcreditCardInformation: " + cardInfo);

		this.cashDesk.startCreditCardPayment(cardInfo);
	}

	public void onEvent(@Observes CreditCardPinEnteredEvent event) throws IllegalCashDeskStateException {
		final int pin = event.getPIN();
		LOG.debug("\tPIN: " + pin);

		this.cashDesk.finishCreditCardPayment(pin);
	}

	/*
	 * If the event targets this cash desk, switch the cash desk to express
	 * mode. If the cash desk state actually changed, republish the event on the
	 * cash-desk local channel.
	 */
	public void onEvent(@Observes ExpressModeEnabledEvent event) {
		if (this.cashDesk.isInExpressMode()) {
			LOG.debug("Already in express mode, ignoring event...");
		} else {
			this.cashDesk.enableExpressMode();
		}
	}
}
