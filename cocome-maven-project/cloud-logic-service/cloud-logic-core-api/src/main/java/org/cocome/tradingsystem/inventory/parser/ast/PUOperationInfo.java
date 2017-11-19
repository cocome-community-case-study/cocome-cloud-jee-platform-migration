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

package org.cocome.tradingsystem.inventory.parser.ast;

public class PUOperationInfo implements ExpressionInfo {

    private String productionUnitClassName;
    private String operationId;

    /**
     * Canonical constructor
     *
     * @param operationId             the operation id
     * @param productionUnitClassName the name of the associated production unit class
     */
    public PUOperationInfo(final String productionUnitClassName, String operationId) {
        this.productionUnitClassName = productionUnitClassName;
        this.operationId = operationId;
    }

    public PUOperationInfo() {
    }

    public String getProductionUnitClassName() {
        return productionUnitClassName;
    }

    public void setProductionUnitClassName(String productionUnitClassName) {
        this.productionUnitClassName = productionUnitClassName;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
