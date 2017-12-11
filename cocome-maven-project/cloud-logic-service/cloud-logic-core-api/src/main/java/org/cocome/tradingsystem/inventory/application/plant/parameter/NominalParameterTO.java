/*
 *************************************************************************
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
 *************************************************************************
 */

package org.cocome.tradingsystem.inventory.application.plant.parameter;

import javax.xml.bind.annotation.*;
import java.util.Set;

/**
 * Represents a parameter with multiple values a user can choose from
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "NominalParameterTO",
        namespace = "http://parameter.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "NominalParameterTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class NominalParameterTO extends ParameterTO {
    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "options", required = true)
    private Set<String> options;

    /**
     * @return The possible values this options a user can set for this option
     */
    public Set<String> getOptions() {
        return options;
    }

    /**
     * @param options The possible values this options a user can set for this option
     */
    public void setOptions(final Set<String> options) {
        this.options = options;
    }
}
