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

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
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

    private long plantId;
    private List<Long> expressionIds;
    private List<Long> inputEntryPointIds;
    private List<Long> outputEntryPointIds;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @Inject
    private Instance<IPlantQuery> plantQueryInstance;

    private IPlantQuery plantQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        plantQuery = plantQueryInstance.get();
        plant = null;
        expressions = null;
        inputEntryPoint = null;
        outputEntryPoint = null;
    }

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
    public Collection<IEntryPoint> getInputEntryPoint() throws NotInDatabaseException {
        if (inputEntryPoint == null) {
            inputEntryPoint = enterpriseQuery.queryEntryPoints(inputEntryPointIds);
        }
        return inputEntryPoint;
    }

    @Override
    public void setInputEntryPoint(Collection<IEntryPoint> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    @Override
    public List<Long> getInputEntryPointIds() {
        return inputEntryPointIds;
    }

    @Override
    public void setInputEntryPointIds(List<Long> inputEntryPointIds) {
        this.inputEntryPointIds = inputEntryPointIds;
    }

    @Override
    public Collection<IEntryPoint> getOutputEntryPoint() throws NotInDatabaseException {
        if (outputEntryPoint == null) {
            outputEntryPoint = enterpriseQuery.queryEntryPoints(outputEntryPointIds);
        }
        return outputEntryPoint;
    }

    @Override
    public void setOutputEntryPoint(Collection<IEntryPoint> outputEntryPoint) {
        this.outputEntryPoint = outputEntryPoint;
    }

    @Override
    public List<Long> getOutputEntryPointIds() {
        return outputEntryPointIds;
    }

    @Override
    public void setOutputEntryPointIds(List<Long> outputEntryPointIds) {
        this.outputEntryPointIds = outputEntryPointIds;
    }

    @Override
    public IPlant getPlant() throws NotInDatabaseException {
        if (plant == null) {
            plant = enterpriseQuery.queryPlant(plantId);
        }
        return plant;
    }

    @Override
    public void setPlant(IPlant plant) {
        this.plant = plant;
    }

    @Override
    public long getPlantId() {
        return plantId;
    }

    @Override
    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    @Override
    public List<IExpression> getExpressions() throws NotInDatabaseException {
        if (this.expressions == null) {
            this.expressions = this.plantQuery.queryExpressionsByIdList(this.expressionIds);
        }
        return expressions;
    }

    @Override
    public void setExpressions(List<IExpression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public List<Long> getExpressionIds() {
        return expressionIds;
    }

    @Override
    public void setExpressionIds(List<Long> expressionIds) {
        this.expressionIds = expressionIds;
    }
}
