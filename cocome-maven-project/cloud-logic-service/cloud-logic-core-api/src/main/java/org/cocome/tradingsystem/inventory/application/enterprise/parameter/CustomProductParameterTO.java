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

package org.cocome.tradingsystem.inventory.application.enterprise.parameter;

import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;

import javax.xml.bind.annotation.*;

/**
 * Represents a product customization parameter.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "CustomProductParameterTO",
        namespace = "http://parameter.enterprise.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "CustomProductParameterTO")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class CustomProductParameterTO implements IParameterTO {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "category", required = true)
    private String category;
    @XmlElement(name = "customProduct", required = true)
    private CustomProductTO customProduct;

    /**
     * @return The databaseid.
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The parameter name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name the parameter name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the category
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category
     */
    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return returns the associated product
     */
    public CustomProductTO getCustomProduct() {
        return customProduct;
    }

    /**
     * @param customProduct the associated product
     */
    public void setCustomProduct(CustomProductTO customProduct) {
        this.customProduct = customProduct;
    }

}