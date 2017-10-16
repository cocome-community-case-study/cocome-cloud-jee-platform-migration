/*
 ***************************************************************************
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

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a single {@link IPlantOperationOrder} entry in the database.
 *
 * @author Rudolf Biczok
 */
@Dependent
public class PlantOperationOrderEntry implements Serializable, IPlantOperationOrderEntry {

    private static final long serialVersionUID = -7683436740437770058L;

    private long id;
    private long amount;
    private Collection<IPlantOperationParameterValue> parameterValues;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(final long amount) {
        this.amount = amount;
    }

    @Override
    public Collection<IPlantOperationParameterValue> getParameterValues() {
        return parameterValues;
    }

    @Override
    public void setParameterValues(Collection<IPlantOperationParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }

}
