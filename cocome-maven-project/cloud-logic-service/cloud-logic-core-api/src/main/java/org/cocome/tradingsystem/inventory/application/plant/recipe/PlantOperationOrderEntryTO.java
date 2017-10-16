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

package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Represents a single {@link PlantOperationOrderTO} entry in the database.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "PlantOperationOrderEntryTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "PlantOperationOrderEntryTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantOperationOrderEntryTO implements Serializable, IIdentifiableTO {

    private static final long serialVersionUID = -7683436740437770058L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "amount", required = true)
    private long amount;
    @XmlElement(name = "operation", required = true)
    private PlantOperationTO operation;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return The amount of ordered products
     */
    public long getAmount() {
        return this.amount;
    }

    /**
     * @param amount The amount of ordered products
     */
    public void setAmount(final long amount) {
        this.amount = amount;
    }

    /**
     * @return The product which is ordered
     */
    public PlantOperationTO getOperation() {
        return this.operation;
    }

    /**
     * @param operation The product which is ordered
     */
    public void setOperation(final PlantOperationTO operation) {
        this.operation = operation;
    }
}
