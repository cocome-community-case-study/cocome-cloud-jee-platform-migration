/*
 **************************************************************************
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
 **************************************************************************
 */

package org.cocome.tradingsystem.cashdeskline.cashdesk.configurator;

import org.cocome.tradingsystem.cashdeskline.events.ParameterValuesEnteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.ProductBarcodeScannedEvent;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;

@CashDeskSessionScoped
public class Configurator implements IConfigurator, Serializable {
    private static final long serialVersionUID = -1L;

    @Inject
    private Event<ParameterValuesEnteredEvent> parameterValuesEnteredEventEvent;


    private Collection<IParameter> parameters;

    @Override
    public Collection<IParameter> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(final Collection<IParameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void sendParameterValues(Collection<ParameterValueTO> parameterValues) {
        parameterValuesEnteredEventEvent.fire(new ParameterValuesEnteredEvent(parameterValues));
    }
}
