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

package org.cocome.tradingsystem.inventory.application.store;

import org.cocome.tradingsystem.inventory.application.enterprise.parameter.CustomProductParameterValueTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a single {@link SaleTO} entry.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "SaleEntryTO",
        namespace = "http://store.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name = "SaleEntryTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleEntryTO implements Serializable {

    private static final long serialVersionUID = -7683436740437770058L;

    @XmlElement(name = "item", required = true)
    private ProductWithItemTO item;
    @XmlElement(name = "parameterValues", required = true)
    private Collection<CustomProductParameterValueTO> parameterValues;

    public SaleEntryTO() {
    }

    public SaleEntryTO(ProductWithItemTO item) {
        this.item = item;
    }

    public SaleEntryTO(ProductWithItemTO item,
                       Collection<CustomProductParameterValueTO> parameterValues) {
        this.item = item;
        this.parameterValues = parameterValues;
    }


    /**
     * @return the plant operation to process
     */
    public ProductWithItemTO getItemInfo() {
        return item;
    }

    /**
     * @param plantOperation the plant operation to process
     */
    public void setItemData(ProductWithItemTO plantOperation) {
        this.item = plantOperation;
    }

    /**
     * @return parameter values
     */
    public Collection<CustomProductParameterValueTO> getParameterValues() {
        return parameterValues;
    }

    /**
     * @param parameterValues parameter values
     */
    public void setParameterValues(final Collection<CustomProductParameterValueTO> parameterValues) {
        this.parameterValues = parameterValues;
    }

}
