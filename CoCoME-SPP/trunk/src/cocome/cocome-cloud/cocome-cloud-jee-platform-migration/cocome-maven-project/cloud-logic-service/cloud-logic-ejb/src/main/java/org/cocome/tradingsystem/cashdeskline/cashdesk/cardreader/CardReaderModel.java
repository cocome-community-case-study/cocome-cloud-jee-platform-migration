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

package org.cocome.tradingsystem.cashdeskline.cashdesk.cardreader;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cocome.tradingsystem.cashdeskline.events.CreditCardPinEnteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.CreditCardScannedEvent;

/**
 * Implements the cash desk credit card reader model.
 * 
 * TODO The card reader should handle communication with the bank, not the cashdesk?
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Stateless
public class CardReaderModel implements ICardReaderLocal {

	
	@Inject
	private Event<CreditCardScannedEvent> creditCardScannedEvent;
	
	@Inject
	private Event<CreditCardPinEnteredEvent> creditCardPinEnteredEvent;

	//
	// Credit card reader model methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendCreditCardInfo(final String creditInfo) {
		creditCardScannedEvent.fire(new CreditCardScannedEvent(creditInfo));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendCreditCardPin(final int pin) {
		creditCardPinEnteredEvent.fire(new CreditCardPinEnteredEvent(pin));
	}

}
