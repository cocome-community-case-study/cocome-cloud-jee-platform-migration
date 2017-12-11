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

import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Used to connect parameters from {@link CustomProductTO} and {@link PlantOperationTO}.
 * Other subsystems are supposed to copy the customer's parameter values to the plant
 * operation based on this mapping
 *
 * @author Rudolf Biczok
 */
@Dependent
public class ParameterInteraction extends InteractionEntity<IParameter>
        implements IParameterInteraction {
    private static final long serialVersionUID = 1L;

    private IParameter from;
    private IParameter to;
    private IRecipe recipe;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        from = null;
        to = null;
    }

    @Override
    public IParameter getFrom() throws NotInDatabaseException {
        if (from == null) {
            from = enterpriseQuery.queryParameterById(fromId);
        }
        return from;
    }

    @Override
    public void setFrom(IParameter from) {
        this.from = from;
    }

    @Override
    public IParameter getTo() throws NotInDatabaseException {
        if (to == null) {
            to = enterpriseQuery.queryParameterById(toId);
        }
        return to;
    }

    @Override
    public void setTo(IParameter to) {
        this.to = to;
    }

    @Override
    public IRecipe getRecipe() throws NotInDatabaseException {
        if (recipe == null) {
            recipe = enterpriseQuery.queryRecipeByID(recipeId);
        }
        return this.recipe;
    }

    @Override
    public void setRecipe(IRecipe recipe) {
        this.recipe = recipe;
    }
}
