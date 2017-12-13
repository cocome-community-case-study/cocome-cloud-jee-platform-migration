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

import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;

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
    private MarkupInfo markupInfo;
    private Collection<IEntryPoint> inputEntryPoint;
    private Collection<IEntryPoint> outputEntryPoint;

    private long plantId;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    private Collection<IParameter> parameters;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        plant = null;
        inputEntryPoint = null;
        outputEntryPoint = null;
        parameters = null;
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
            inputEntryPoint = enterpriseQuery.queryInputEntryPointsByRecipeOperationId(this.id);
        }
        return inputEntryPoint;
    }

    @Override
    public void setInputEntryPoint(Collection<IEntryPoint> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    @Override
    public Collection<IEntryPoint> getOutputEntryPoint() throws NotInDatabaseException {
        if (outputEntryPoint == null) {
            outputEntryPoint = enterpriseQuery.queryOutputEntryPointsByRecipeOperationId(this.id);
        }
        return outputEntryPoint;
    }

    @Override
    public void setOutputEntryPoint(Collection<IEntryPoint> outputEntryPoint) {
        this.outputEntryPoint = outputEntryPoint;
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
    public MarkupInfo getMarkup() {
        return this.markupInfo;
    }

    @Override
    public void setMarkup(MarkupInfo markup) {
        this.markupInfo = markup;
    }

    @Override
    public Collection<IParameter> getParameters() throws NotInDatabaseException {
        if (parameters == null) {
            parameters = enterpriseQuery.queryParametersByRecipeOperationId(this.id);
        }
        return parameters;
    }

    @Override
    public void setParameters(final Collection<IParameter> parameters) {
        this.parameters = parameters;
    }
}
