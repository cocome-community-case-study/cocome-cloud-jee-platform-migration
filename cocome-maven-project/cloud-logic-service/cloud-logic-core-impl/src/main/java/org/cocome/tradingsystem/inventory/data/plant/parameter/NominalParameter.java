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

package org.cocome.tradingsystem.inventory.data.plant.parameter;

import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperation;

import javax.enterprise.context.Dependent;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalParameter} for {@link IPlantOperation}
 *
 * @author Rudolf Biczok
 */
@Dependent
public class NominalParameter extends Parameter implements INominalParameter {
    private static final long serialVersionUID = -2577328715744776645L;

    private Set<String> options;

    /**
     * @return The possible values this options a user can set for this option
     */
    @Override
    public Set<String> getOptions() {
        return options;
    }

    /**
     * @param options The possible values this options a user can set for this option
     */
    @Override
    public void setOptions(final Set<String> options) {
        this.options = options;
    }

    @Override
    public boolean isValidValue(String value) {
        for (final String option : getOptions()) {
            if (Objects.equals(option, value)) {
                return true;
            }
        }
        return false;
    }
}
