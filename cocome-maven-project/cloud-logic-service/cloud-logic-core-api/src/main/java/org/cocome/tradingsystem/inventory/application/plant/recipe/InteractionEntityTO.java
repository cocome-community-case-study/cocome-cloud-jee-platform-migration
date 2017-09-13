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

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.INameableTO;

import javax.xml.bind.annotation.*;

/**
 * Used as common interface for classes who connect two other entity types with each other
 *
 * @param <FromType> the type of the first interaction partner
 * @param <ToType>   the type of the second interaction partner
 * @author Rudolf Biczok
 */
@XmlType(
        name = "IIdentifiableTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "IIdentifiableTO")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class InteractionEntityTO<
        FromType extends INameableTO,
        ToType extends INameableTO>
        implements IIdentifiableTO {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "from", required = true)
    private FromType from;
    @XmlElement(name = "to", required = true)
    private ToType to;

    /**
     * @return the database id
     */
    public long getId() {
        return this.id;
    }

    /**
     * @param id the database id
     */
    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the first / source instance
     */
    public FromType getFrom() {
        return from;
    }

    /**
     * @param from the first / source instance
     */
    public void setFrom(FromType from) {
        this.from = from;
    }

    /**
     * @return the second / destination instance
     */
    public ToType getTo() {
        return to;
    }

    /**
     * @param to the second / destination instance
     */
    public void setTo(ToType to) {
        this.to = to;
    }
}
