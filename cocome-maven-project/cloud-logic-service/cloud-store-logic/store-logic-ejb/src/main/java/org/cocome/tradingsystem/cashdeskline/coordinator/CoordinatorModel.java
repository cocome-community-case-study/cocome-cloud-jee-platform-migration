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

package org.cocome.tradingsystem.cashdeskline.coordinator;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.events.ExpressModeEnabledEvent;
import org.cocome.tradingsystem.util.java.Maps;
import org.cocome.tradingsystem.util.mvc.AbstractModel;

/**
 * Implements the cash desk line coordinator model. The coordinator is
 * responsible for collecting sales information from all cash desks and
 * deciding in which mode (normal or express) they should operate.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Singleton
public class CoordinatorModel extends AbstractModel<CoordinatorModel> implements ICoordinatorModelLocal {

	private static final Logger __log__ =
			Logger.getLogger(CoordinatorModel.class);

	private Map<String, ExpressModeEvaluator> __cashDeskEvaluators;
	
	@Inject
	private Event<ExpressModeEnabledEvent> expressModeEnabledEvent;
	
	@PostConstruct
	public void initModel() {
		__log__.debug("Initializing coordinator model...");
		__cashDeskEvaluators = Maps.newConcurrentHashMap();
	}

	//
	// Coordinator model methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateStatistics(String cashDeskName, Sale sale) {
		final ExpressModeEvaluator evaluator = __getCashDeskEvaluator(cashDeskName);

		evaluator.updateSalesHistory(sale);

		if (evaluator.isExpressModeNeeded()) {
			__switchToExpressMode(cashDeskName);
			__log__.info("Express mode activated for cash desk " + cashDeskName);
		}
	}

	private ExpressModeEvaluator __getCashDeskEvaluator(
			final String cashDeskName
			) {
		ExpressModeEvaluator result = __cashDeskEvaluators.get(cashDeskName);
		if (result == null) {
			result = new ExpressModeEvaluator();
			__cashDeskEvaluators.put(cashDeskName, result);
		}

		return result;
	}

	private void __switchToExpressMode(final String cashDeskName) {
		// The correct CashDeskSessionScope should be active here 
		// because only a cash desk can trigger this action
		expressModeEnabledEvent.fire(new ExpressModeEnabledEvent(cashDeskName));
	}

}
