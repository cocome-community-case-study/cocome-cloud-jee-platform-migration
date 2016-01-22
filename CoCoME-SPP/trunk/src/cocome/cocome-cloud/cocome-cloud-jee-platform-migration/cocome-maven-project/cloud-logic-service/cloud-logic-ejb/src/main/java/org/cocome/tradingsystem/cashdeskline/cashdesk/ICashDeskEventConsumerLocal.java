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

import javax.ejb.Local;
import javax.enterprise.event.Observes;

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
 * Specifies events consumed by the cash desk component. Each event has to have
 * a handler method with a single parameter of the same type as the consumed
 * event. To ensure implementation of event handlers for all relevant event
 * types, the cash desk component has to implement this interface.
 * 
 * @author Holger Klus
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 */
@Local
public interface ICashDeskEventConsumerLocal {

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 * @throws IllegalCashDeskStateException 
	 */
	void onEvent(@Observes SaleStartedEvent event) throws IllegalCashDeskStateException;

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(@Observes SaleFinishedEvent event) throws IllegalCashDeskStateException;

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(@Observes PaymentModeSelectedEvent event) throws IllegalCashDeskStateException;

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(@Observes ExpressModeDisabledEvent event);

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 * @throws ProductOutOfStockException 
	 */
	void onEvent(@Observes ProductBarcodeScannedEvent event) throws IllegalCashDeskStateException, ProductOutOfStockException;

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(@Observes CashAmountEnteredEvent event) throws IllegalCashDeskStateException;

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(@Observes CashBoxClosedEvent event) throws IllegalCashDeskStateException;

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(@Observes CreditCardScannedEvent event) throws IllegalCashDeskStateException;

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(@Observes CreditCardPinEnteredEvent event) throws IllegalCashDeskStateException;

	/**
	 * Handles the given event.
	 * 
	 * @param event
	 * 		the event to be handled
	 */
	void onEvent(@Observes ExpressModeEnabledEvent event);
}
