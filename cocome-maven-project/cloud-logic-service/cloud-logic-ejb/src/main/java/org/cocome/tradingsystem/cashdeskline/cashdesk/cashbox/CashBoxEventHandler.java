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

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.events.CashBoxClosedEvent;
import org.cocome.tradingsystem.cashdeskline.events.ChangeAmountCalculatedEvent;

/**
 * Implements the cash desk event handler for the cash box model. The event
 * handler is similar to a controller in that it converts incoming cash desk
 * messages to actions on the cash box model. However, there is no view
 * associated with the controller.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 */

@Dependent
public class CashBoxEventHandler implements ICashBoxEventConsumerLocal {

	private static final Logger LOG =
			Logger.getLogger(CashBoxEventHandler.class);

	//

	@Inject
	private ICashBoxLocal cashBox;

	//
	// Event handler methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes ChangeAmountCalculatedEvent event) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("\tchangeAmount: " + event.getChangeAmount());
		}

		this.cashBox.open();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes CashBoxClosedEvent event) {
		this.cashBox.closeSilently();
	}

}
