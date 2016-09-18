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

import javax.ejb.Local;

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

/**
 * Specifies events consumed by the user display component. Each event has to
 * have a handler method with a single parameter of the same type as the
 * consumed event. To ensure implementation of event handlers for all relevant
 * event types, the user display component has to implement this interface.
 * 
 * @author Holger Klus
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Local
interface IUserDisplayEventHandler {

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(SaleStartedEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(RunningTotalChangedEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(InvalidProductBarcodeEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(PaymentModeSelectedEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(PaymentModeRejectedEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(CashBoxNumPadKeypressEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(CashAmountEnteredEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(ChangeAmountCalculatedEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(CreditCardScannedEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(CreditCardScanFailedEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(InvalidCreditCardEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(SaleSuccessEvent event);

	//

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(ExpressModeDisabledEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(ExpressModeEnabledEvent event);
	
	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(InsufficientCashAmountEvent event);

}
