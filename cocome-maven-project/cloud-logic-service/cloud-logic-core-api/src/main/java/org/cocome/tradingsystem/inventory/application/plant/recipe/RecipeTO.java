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

package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;

import javax.xml.bind.annotation.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "RecipeTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "RecipeTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeTO extends RecipeOperationTO {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "customProduct", required = true)
    private CustomProductTO customProduct;

    // Represent the vertices of the recipe graph
    @XmlElement(name = "operations", required = true)
    private Collection<PlantOperationTO> operations;

    // Represent the edges of the recipe graph
    @XmlElement(name = "parameterInteractions", required = true)
    private Collection<ParameterInteractionTO> parameterInteractions;
    @XmlElement(name = "entryPointInteractions", required = true)
    private Collection<EntryPointInteractionTO> entryPointInteractions;

    /**
     * @return the custom product for which this recipe provides production information
     */
    public CustomProductTO getCustomProduct() {
        return customProduct;
    }

    /**
     * @param customProduct the custom product for which this recipe provides production information
     */
    public void setCustomProduct(CustomProductTO customProduct) {
        this.customProduct = customProduct;
    }

    /**
     * @return the plant operation that is supposed to be executed within this interaction step
     */
    public Collection<PlantOperationTO> getOperations() {
        return operations;
    }

    /**
     * @param operations the plant operation that is supposed to be executed within this interaction step
     */
    public void setOperations(Collection<PlantOperationTO> operations) {
        this.operations = operations;
    }

    /**
     * @return the list of interactions
     */
    public Collection<EntryPointInteractionTO> getEntryPointInteractions() {
        return entryPointInteractions;
    }

    /**
     * @param entryPointInteractions the list of interaction
     */
    public void setEntryPointInteractions(Collection<EntryPointInteractionTO> entryPointInteractions) {
        this.entryPointInteractions = entryPointInteractions;
    }

    /**
     * @return the list of parameter bindings
     */
    public Collection<ParameterInteractionTO> getParameterInteractions() {
        return parameterInteractions;
    }

    /**
     * @param parameterInteractions the list of parameter bindings
     */
    public void setParameterInteractions(Collection<ParameterInteractionTO> parameterInteractions) {
        this.parameterInteractions = parameterInteractions;
    }
}
