/***************************************************************************
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
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.application.plant;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Transfer object for plant and enterprise data.
 *
 * @author Rudolf Biczok
 */
@XmlType(name = "PlantWithEnterpriseTO",
        namespace = "http://plant.application.inventory.tradingsystem.cocome.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantWithEnterpriseTO extends PlantTO {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "enterpriseTO", required = true)
    private EnterpriseTO __enterpriseTO;

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
