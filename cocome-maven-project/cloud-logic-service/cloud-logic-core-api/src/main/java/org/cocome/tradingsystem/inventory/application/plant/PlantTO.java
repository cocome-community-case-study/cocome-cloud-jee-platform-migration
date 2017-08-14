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

package org.cocome.tradingsystem.inventory.application.plant;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Transfer object for plant and enterprise data.
 *
 * @author Rudolf Biczok
 */

@XmlType(name = "PlantTO",
        namespace = "http://plant.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name = "PlantTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long __id;

    @XmlElement(name = "name", required = true)
    private String __name;

    @XmlElement(name = "location", required = true)
    private String __location;

    @XmlElement(name = "enterpriseTO", required = true)
    private EnterpriseTO __enterpriseTO;

    /**
     * Gets the entity identifier of this store.
     *
     * @return Store entity identifier.
     */
    public long getId() {
        return __id;
    }

    /**
     * Sets the entity identifier of this store.
     *
     * @param id store entity identifier
     */
    public void setId(final long id) {
        __id = id;
    }

    /**
     * Gets the name of this store.
     *
     * @return Store name.
     */
    public String getName() {
        return __name;
    }

    /**
     * Sets the name of this store.
     *
     * @param name store name
     */
    public void setName(final String name) {
        __name = name;
    }

    /**
     * Gets the location of this store.
     *
     * @return Store location.
     */
    public String getLocation() {
        return __location;
    }

    /**
     * Sets the location of this store.
     *
     * @param location store location
     */
    public void setLocation(final String location) {
        __location = location;
    }

    /**
     * Gets the enterprise the store belongs to.
     *
     * @return Enterprise TO this store belongs to.
     */
    public EnterpriseTO getEnterpriseTO() {
        return __enterpriseTO;
    }

    /**
     * Sets the enterprise the store belongs to.
     *
     * @param enterpriseTO transfer object of the corresponding enterprise
     */
    public void setEnterpriseTO(final EnterpriseTO enterpriseTO) {
        __enterpriseTO = enterpriseTO;
    }

}
