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
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

import javax.xml.bind.annotation.*;
import java.util.Objects;

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

    @XmlElement(name = "enterprise", required = true)
    private EnterpriseTO enterprise;

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

    public EnterpriseTO getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseTO enterprise) {
        this.enterprise = enterprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeTO recipeTO = (RecipeTO) o;
        return Objects.equals(enterprise, recipeTO.enterprise) &&
                Objects.equals(getName(), recipeTO.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(enterprise, getName());
    }

}
