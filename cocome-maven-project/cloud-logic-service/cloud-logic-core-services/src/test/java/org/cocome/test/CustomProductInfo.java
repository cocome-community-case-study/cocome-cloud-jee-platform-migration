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

package org.cocome.test;

import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.CustomProductParameterTO;

import java.util.List;

/**
 * Encapsulates a custom product and its parameters
 *
 * @author Rudolf Biczok
 */
public class CustomProductInfo {

    private final CustomProductTO customProduct;
    private final List<CustomProductParameterTO> parameters;

    /**
     * Canonical constructor
     *
     * @param customProduct the custom product
     * @param parameters        the parameters of the custom product
     */
    public CustomProductInfo(final CustomProductTO customProduct,
                             final List<CustomProductParameterTO> parameters) {
        this.customProduct = customProduct;
        this.parameters = parameters;
    }

    public CustomProductTO getCustomProduct() {
        return customProduct;
    }

    public List<CustomProductParameterTO> getParameters() {
        return parameters;
    }

}
