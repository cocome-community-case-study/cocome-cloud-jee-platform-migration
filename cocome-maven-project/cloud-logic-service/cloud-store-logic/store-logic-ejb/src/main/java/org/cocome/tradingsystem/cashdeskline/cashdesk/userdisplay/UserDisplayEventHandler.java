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

package org.cocome.tradingsystem.cashdeskline.cashdesk.userdisplay;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.datatypes.NumPadKeyStroke;
import org.cocome.tradingsystem.cashdeskline.datatypes.PaymentMode;
import org.cocome.tradingsystem.cashdeskline.events.CashAmountEnteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.CashBoxNumPadKeypressEvent;
import org.cocome.tradingsystem.cashdeskline.events.ChangeAmountCalculatedEvent;
import org.cocome.tradingsystem.cashdeskline.events.CreditCardScanFailedEvent;
import org.cocome.tradingsystem.cashdeskline.events.CreditCardScannedEvent;
import org.cocome.tradingsystem.cashdeskline.events.ExpressModeDisabledEvent;
import org.cocome.tradingsystem.cashdeskline.events.ExpressModeEnabledEvent;
import org.cocome.tradingsystem.cashdeskline.events.InsufficientCashAmountEvent;
import org.cocome.tradingsystem.cashdeskline.events.InvalidCreditCardEvent;
import org.cocome.tradingsystem.cashdeskline.events.InvalidProductBarcodeEvent;
import org.cocome.tradingsystem.cashdeskline.events.PaymentModeRejectedEvent;
import org.cocome.tradingsystem.cashdeskline.events.PaymentModeSelectedEvent;
import org.cocome.tradingsystem.cashdeskline.events.RunningTotalChangedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleStartedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleSuccessEvent;
import org.cocome.tradingsystem.inventory.application.store.Barcode;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;

/**
 * Implements the cash desk event handler for the user display model. The event
 * handler is similar to a controller in that it converts incoming cash desk
 * messages to actions on the user display model. However, there is no view
 * associated with the controller.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@CashDeskSessionScoped
class UserDisplayEventHandler implements IUserDisplayEventConsumerLocal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 94000048623886936L;

	private static final Logger LOG =
			Logger.getLogger(UserDisplayEventHandler.class);

	//

	@Inject
	private IUserDisplayLocal display;

	private final StringBuilder cashAmountInput = new StringBuilder();

	private boolean enteringCashAmount;

	private double cashAmount = 1.0;
	
	private boolean paymentModeRejected = false;

	//
	// Event handler methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes SaleStartedEvent event) {
		LOG.debug("Sale started event received, updating display content");
		this.display.setContent(MessageKind.SPECIAL, "New sale");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes RunningTotalChangedEvent event) {
		final double runningTotal = event.getRunningTotal();
		LOG.debug("\trunningTotal: " + runningTotal);

		//

		// Use english locale to not get a comma as decimal point
		this.display.setContent(
				MessageKind.NORMAL, String.format(Locale.ENGLISH, 
						"%s: %.2f\nRunning total: %.2f",
						event.getProductName(), event.getProductPrice(), runningTotal
						)
				);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes InvalidProductBarcodeEvent event) {
		final Barcode barcode = event.getBarcode();
		LOG.debug("\tbarcode: " + barcode);

		//

		this.display.setContent(
				MessageKind.WARNING, "No product for barcode " + barcode + "!"
				);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes PaymentModeSelectedEvent event) {
		if (this.paymentModeRejected) {
			// Prevent payment mode selected event to override a rejection.
			// Necessary because it is possible for the selected event to
			// fire, then to fire a rejected event in the cash desk and
			// be executed at the user display after the rejected event.
			this.paymentModeRejected = false;
			return;
		}
		
		final PaymentMode mode = event.getMode();
		LOG.debug("\tpaymentMode: " + mode);

		//

		if (mode == PaymentMode.CASH) {
			this.clearAmountInput();
			this.display.setContent(MessageKind.SPECIAL, "Paying by cash\n");
			this.enteringCashAmount = true;
		} else {
			this.display.setContent(MessageKind.SPECIAL, "Paying by credit card\n");
			this.enteringCashAmount = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes PaymentModeRejectedEvent event) {
		final PaymentMode mode = event.getMode();
		LOG.debug("\tpaymentMode: " + mode);

		final String reason = event.getReason();
		LOG.debug("\treason: " + reason);

		this.display.setContent(MessageKind.WARNING, reason);
		this.paymentModeRejected = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes CashBoxNumPadKeypressEvent event) {
		final NumPadKeyStroke keyStroke = event.getKeyStroke();
		LOG.debug("\tkeyStroke: " + keyStroke);

		//
		// When in cash payment mode, collect key strokes into a buffer and
		// display the cash amount being entered.
		//
		if (this.enteringCashAmount && (keyStroke != NumPadKeyStroke.ENTER)) {
			this.cashAmountInput.append(keyStroke.label());
			this.display.setContent(
					MessageKind.SPECIAL,
					"Paying by cash\nAmount received: " + this.cashAmountInput.toString()
					);
		} else {
			this.clearAmountInput();
		}
	}

	private void clearAmountInput() {
		this.cashAmountInput.setLength(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes CashAmountEnteredEvent event) {
		final double cashAmount = event.getCashAmount(); // NOCS
		LOG.debug("\tcashAmount: " + cashAmount);

		//

		this.cashAmount = cashAmount;
		this.enteringCashAmount = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes ChangeAmountCalculatedEvent event) {
		final double changeAmount = event.getChangeAmount();
		LOG.debug("\tchangeAmount: " + changeAmount);

		//

		this.display.setContent(
				MessageKind.NORMAL,
				"Cash received: " + this.cashAmount + "\n"
						+ "Change amount: " + changeAmount
				);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes CreditCardScannedEvent event) {
		this.display.setContent(
				MessageKind.SPECIAL, "Credit card scanned.\nPlease enter your PIN..."
				);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes CreditCardScanFailedEvent event) {
		this.display.setContent(
				MessageKind.WARNING, "Failed to scan card.\nPlease try again..."
				);
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public void onEvent(@Observes InvalidCreditCardEvent event) {
		this.display.setContent(
				MessageKind.WARNING, "Invalid credit card information.\nPlease try again..."
				);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes SaleSuccessEvent event) {
		this.display.setContent(
				MessageKind.SPECIAL,
				"Thank you for shopping!\nHave a nice day."
				);
	}

	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes ExpressModeDisabledEvent event) {
		// This overwrites the change amount message and is not absolutely necessary anyway
//		this.display.setContent(
//				MessageKind.SPECIAL, "Express mode disabled."
//				);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes ExpressModeEnabledEvent event) {
//		this.display.setContent(
//				MessageKind.SPECIAL, "Express mode enabled."
//				);
	}

	@Override
	public void onEvent(InsufficientCashAmountEvent event) {
		this.display.setContent(MessageKind.WARNING, "Not enough cash. Please try again...");
	}

}
