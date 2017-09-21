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

import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;

import javax.enterprise.context.Dependent;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
@Dependent
public class PlantOperation implements Serializable, IPlantOperation {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private IPlant plant;
    private List<IExpression> expressions;
    private Collection<IEntryPoint> inputEntryPoint;
    private Collection<IEntryPoint> outputEntryPoint;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Collection<IEntryPoint> getInputEntryPoint() {
        return inputEntryPoint;
    }

    @Override
    public void setInputEntryPoint(Collection<IEntryPoint> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    @Override
    public Collection<IEntryPoint> getOutputEntryPoint() {
        return outputEntryPoint;
    }

    @Override
    public void setOutputEntryPoint(Collection<IEntryPoint> outputMaterial) {
        this.outputEntryPoint = outputMaterial;
    }

    @Override
    public IPlant getPlant() {
        return plant;
    }

    @Override
    public void setPlant(IPlant plant) {
        this.plant = plant;
    }

    @Override
    public List<IExpression> getExpressions() {
        return expressions;
    }

    @Override
    public void setExpressions(List<IExpression> expressions) {
        this.expressions = expressions;
    }
}
