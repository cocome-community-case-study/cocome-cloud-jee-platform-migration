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

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;

import java.util.Collection;
import java.util.List;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
public interface IPlantOperation extends INameable {

    /**
     * @return all material classes that are required for operation execution
     */
    Collection<IEntryPoint> getInputEntryPoint();

    /**
     * @param inputMaterial all material classes that are required for operation execution
     */
    void setInputEntryPoint(Collection<IEntryPoint> inputMaterial);

    /**
     * @return all material classes that results after the operation execution
     */
    Collection<IEntryPoint> getOutputEntryPoint();

    /**
     * @param outputMaterial all material classes that results after the operation execution
     */
    void setOutputEntryPoint(Collection<IEntryPoint> outputMaterial);

    /**
     * @return the plant that owns this production unit
     */
    IPlant getPlant();

    /**
     * @param plant the plant that owns this production unit
     */
    void setPlant(IPlant plant);

    /**
     * @return the list of expressions used to execute this operation
     */
    List<IExpression> getExpressions();

    /**
     * @param expressions the list of expressions used to execute this operation
     */
    void setExpressions(List<IExpression> expressions);
}
