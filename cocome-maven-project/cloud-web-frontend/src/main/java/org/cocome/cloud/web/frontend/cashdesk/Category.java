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

package org.cocome.cloud.web.frontend.cashdesk;

import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The used to group parameters based on their category
 *
 * @author Rudolf Biczok
 */
public class Category {

    private final String name;
    private final List<ParameterValueTO> parameters;

    public Category(String name, List<ParameterTO> parameters) {
        this.name = name;
        this.parameters = StreamUtil.ofNullable(parameters)
                .map(param -> new ParameterValueTO(null, param))
                .collect(Collectors.toList());
    }

    public List<ParameterValueTO> getParameterValues() {
        return parameters;
    }

    public String getName() {
        return name;
    }
}
