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

package org.cocome.tradingsystem.cashdeskline.cashdesk.expresslight;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.util.mvc.AbstractModel;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;

/**
 * Implements the cash desk express light model.
 * 
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 */

@CashDeskSessionScoped
public class ExpressLightModel
		extends AbstractModel<ExpressLightModel> implements IExpressLightLocal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2307796233994362841L;

	private static final Logger LOG =
			Logger.getLogger(ExpressLightModel.class);

	//

	/** Enumerates the possible states of an express light. */
	private enum ExpressLightState {
		ON, OFF;
	}

	private ExpressLightState state;

	//

	//
	// Express light model methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnExpressLightOn() {
		LOG.error("ExpressLight turned on for " + this.toString());
		this.setState(ExpressLightState.ON);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@PostConstruct
	public void turnExpressLightOff() {
		LOG.error("ExpressLight turned off for " + this.toString());
		this.setState(ExpressLightState.OFF);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOn() {
		return this.getState() == ExpressLightState.ON;
	}

	//

	private void setState(final ExpressLightState newState) {
		if (this.state != newState) {
			LOG.debug("setting new expresslight state...");
			this.state = newState;
			this.changedContent();
		}
	}

	private ExpressLightState getState() {
		return this.state;
	}

}
