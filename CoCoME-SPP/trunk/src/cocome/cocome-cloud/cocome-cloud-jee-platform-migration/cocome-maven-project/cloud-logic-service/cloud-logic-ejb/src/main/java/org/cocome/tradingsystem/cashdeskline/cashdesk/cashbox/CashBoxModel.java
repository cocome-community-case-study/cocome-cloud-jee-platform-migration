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

package org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.datatypes.ControlKeyStroke;
import org.cocome.tradingsystem.cashdeskline.datatypes.NumPadKeyStroke;
import org.cocome.tradingsystem.cashdeskline.datatypes.PaymentMode;
import org.cocome.tradingsystem.cashdeskline.events.CashAmountEnteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.CashBoxClosedEvent;
import org.cocome.tradingsystem.cashdeskline.events.CashBoxNumPadKeypressEvent;
import org.cocome.tradingsystem.cashdeskline.events.CashBoxOpenedEvent;
import org.cocome.tradingsystem.cashdeskline.events.ExpressModeDisabledEvent;
import org.cocome.tradingsystem.cashdeskline.events.PaymentModeSelectedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleFinishedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleStartedEvent;
import org.cocome.tradingsystem.util.mvc.AbstractModel;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;

/**
 * Implements the cash desk cash box model.
 * <p>
 * TODO Consider using transacted session so that sent messages are in the same transaction as the received messages.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */

@CashDeskSessionScoped
public class CashBoxModel extends AbstractModel<CashBoxModel> implements ICashBoxLocal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1030786921661071934L;

	private static final Logger LOG =
			Logger.getLogger(CashBoxModel.class);

	private final StringBuilder amountInput = new StringBuilder();

	
	@Inject
	private Event<CashBoxOpenedEvent> cashBoxOpenedEvent;
	
	@Inject
	private Event<CashBoxClosedEvent> cashBoxClosedEvent;
	
	@Inject
	private Event<PaymentModeSelectedEvent> paymentModeSelectedEvent;
	
	@Inject
	private Event<SaleFinishedEvent> saleFinishedEvent;
	
	@Inject
	private Event<SaleStartedEvent> saleStartedEvent;
	
	@Inject
	private Event<ExpressModeDisabledEvent> expressModeDisabledEvent;
	
	@Inject
	private Event<CashBoxNumPadKeypressEvent> cashBoxNumPadKeypressEvent;
	
	@Inject
	private Event<CashAmountEnteredEvent> cashAmountEnteredEvent;

	/** Enumerates the possible cash box states. */
	private enum CashBoxState {
		OPEN, CLOSED
	}

	private CashBoxState state;

	//

	@PostConstruct
	private void initCashBox() {
		this.setState(CashBoxState.CLOSED);
	}

	//
	// Cash box model methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() {
		if (this.setState(CashBoxState.OPEN)) {
			cashBoxOpenedEvent.fire(new CashBoxOpenedEvent());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		if (this.setState(CashBoxState.CLOSED)) {
			cashBoxClosedEvent.fire(new CashBoxClosedEvent());
		}
	}

	/**
	 * Closes the cash box without sending a CashBoxClosedEvent. This is needed
	 * for test scenarios, where the event is generated by the test code.
	 * <p>
	 * TODO The tests should use this implementation through RMI to avoid such hacks. --LB
	 */
	public void closeSilently() {
		this.setState(CashBoxState.CLOSED);
	}

	@Override
	public boolean isOpen() {
		return this.getState() == CashBoxState.OPEN;
	}

	//

	private synchronized boolean setState(final CashBoxState newState) {
		final boolean changed = this.state != newState;
		if (changed) {
			this.state = newState;
			this.changedContent();
		}

		return changed;
	}

	private synchronized CashBoxState getState() {
		return this.state;
	}

	//

	@Override
	public void pressControlKey(ControlKeyStroke keyStroke) throws IllegalCashDeskStateException, IllegalInputException {
		switch (keyStroke) {
		case CASH_PAYMENT:
			paymentModeSelectedEvent.fire(new PaymentModeSelectedEvent(PaymentMode.CASH));
			break;
		case CREDIT_CARD_PAYMENT:
			paymentModeSelectedEvent.fire(new PaymentModeSelectedEvent(PaymentMode.CREDIT_CARD));
			break;
		case DISABLE_EXPRESS_MODE:
			expressModeDisabledEvent.fire(new ExpressModeDisabledEvent());
			break;
		case FINISH_SALE:
			saleFinishedEvent.fire(new SaleFinishedEvent());
			break;
		case START_SALE:
			saleStartedEvent.fire(new SaleStartedEvent());
			break;
		default:
			throw new IllegalInputException("Wrong key stroke " + keyStroke);
		}
	}

	@Override
	public void pressNumpadKey(final NumPadKeyStroke keyStroke) throws IllegalCashDeskStateException, IllegalInputException {
		//
		// Send out the keystroke to other cash desk components.
		//
		cashBoxNumPadKeypressEvent.fire(new CashBoxNumPadKeypressEvent(keyStroke));

		//
		// Collect key strokes from the cash box. When the cashier presses the
		// ENTER key, convert the sequence to number and send out an event with
		// the amount entered.
		//
		if (keyStroke != NumPadKeyStroke.ENTER) {
			this.amountInput.append(keyStroke.label());

		} else {
			try {
				final double amount = Double.parseDouble(this.amountInput.toString());
				this.sendCashAmountEnteredEvent(amount);

			} catch (final NumberFormatException nfe) {
				throw new IllegalInputException("Invalid cash amount: " + nfe.getMessage());
			}

			//
			// Clear the buffer for next (or repeated entry).
			//
			this.amountInput.setLength(0);
		}
	}

	private void sendCashAmountEnteredEvent(final double amount) {
		cashAmountEnteredEvent.fire(new CashAmountEnteredEvent(amount));
	}
}
