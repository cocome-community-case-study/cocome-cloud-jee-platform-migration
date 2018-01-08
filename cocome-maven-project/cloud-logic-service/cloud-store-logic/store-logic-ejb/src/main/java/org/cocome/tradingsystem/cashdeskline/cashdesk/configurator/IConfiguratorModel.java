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

import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;

import java.util.Collection;

/**
 * Represents an configurator touch screen for configuring a custom product
 *
 * @author Rudolf Biczok
 */
public interface IConfiguratorModel {
    /**
     * @return the parameters of the previously selected product
     */
    Collection<IParameter> getParameters();

    /**
     * @param parameters the parameters of the previously selected product
     */
    void setParameters(final Collection<IParameter> parameters);

    /**
     *
     * @param parameterValues
     */
    void sendParameterValues(final Collection<ParameterValueTO> parameterValues);
}
