/*
 ***************************************************************************
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
 ***************************************************************************
 */

package org.cocome.tradingsystem.cashdeskline.cashdesk;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

import org.cocome.tradingsystem.cashdeskline.events.*;
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
 * @author Robert Heinrich
 */
@Local
public interface ICashDeskEventHandler {

    void onEvent(@Observes SaleStartedEvent event) throws IllegalCashDeskStateException;

    void onEvent(@Observes SaleFinishedEvent event) throws IllegalCashDeskStateException;

    void onEvent(@Observes PaymentModeSelectedEvent event) throws IllegalCashDeskStateException;

    void onEvent(@Observes ExpressModeDisabledEvent event);

    void onEvent(@Observes ProductBarcodeScannedEvent event) throws IllegalCashDeskStateException, ProductOutOfStockException;

    void onEvent(@Observes CashAmountEnteredEvent event) throws IllegalCashDeskStateException;

    void onEvent(@Observes CashBoxClosedEvent event) throws IllegalCashDeskStateException;

    void onEvent(@Observes CreditCardScannedEvent event) throws IllegalCashDeskStateException;

    void onEvent(@Observes CreditCardPinEnteredEvent event) throws IllegalCashDeskStateException;

    void onEvent(@Observes ParameterValuesEnteredEvent event) throws IllegalCashDeskStateException;

    void onEvent(@Observes ExpressModeEnabledEvent event);
}
