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

package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;

import java.util.List;

/**
 * Plant-local expression that represents a constant list of operations
 *
 * @author Rudolf Bicozok
 */
public interface IConstExpression extends IExpression {
    /**
     * @return the list of operations that are supposed to be executed
     */
    List<IProductionUnitOperation> getOperations();

    /**
     * @param operations the list of operations that are supposed to be executed
     */
    void setOperations(List<IProductionUnitOperation> operations);
}
