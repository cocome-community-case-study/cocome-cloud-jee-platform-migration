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

package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import javax.enterprise.context.Dependent;
import java.util.Set;

/**
 * Gives the user a finite number of options to customize a product
 *
 * @author Rudolf Biczok
 */
@Dependent
public class NorminalCustomProductParameter extends CustomProductParameter
        implements INorminalCustomProductParameter {
    private static final long serialVersionUID = -2577328715744776645L;

    private Set<String> options;

    @Override
    public Set<String> getOptions() {
        return options;
    }

    @Override
    public void setOptions(final Set<String> options) {
        this.options = options;
    }
}
