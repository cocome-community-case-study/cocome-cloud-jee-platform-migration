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

package org.cocome.tradingsystem.inventory.application.plant.productionunit;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Represents a class of production unity utilizing a specific set of {@link ProductionUnitOperationTO}
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ProductionUnitClassTO",
        namespace = "http://productionunit.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ProductionUnitClassTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductionUnitClassTO implements Serializable, IIdentifiableTO {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElement(name = "enterprise", required = true)
    private EnterpriseTO enterprise;

    /**
     * @return The id.
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            Identifier value.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The name of the product
     */
    @Basic
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The enterprise which the Plant belongs to
     */
    public EnterpriseTO getEnterprise() {
        return this.enterprise;
    }

    /**
     * @param enterprise
     *            The enterprise which the Plant belongs to
     */
    public void setEnterprise(final EnterpriseTO enterprise) {
        this.enterprise = enterprise;
    }
}
