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

import org.cocome.tradingsystem.inventory.application.INameableTO;

import javax.xml.bind.annotation.*;
import java.util.Objects;

/**
 * Used to connection ports between {@link PlantOperationTO}
 */
@XmlType(
        name = "EntryPointTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "EntryPointTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntryPointTO implements INameableTO {
    @XmlType(
            name = "DirectionTO",
            namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
    @XmlRootElement(name = "DirectionTO")
    @XmlEnum
    public enum DirectionTO {
        INPUT, OUTPUT
    }

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;
    @XmlElementRef(name = "operation")
    private RecipeOperationTO operation;
    @XmlElement(name = "direction", required = true)
    private DirectionTO direction;

    /**
     * @return the database id
     */
    @Override
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
     * Returns the name of the Plant.
     *
     * @return Plant name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name of the Plant
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    public RecipeOperationTO getOperation() {
        return operation;
    }

    public void setOperation(RecipeOperationTO operation) {
        this.operation = operation;
    }

    public DirectionTO getDirection() {
        return direction;
    }

    public void setDirection(DirectionTO direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryPointTO that = (EntryPointTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(operation, that.operation) &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, operation, direction);
    }
}
