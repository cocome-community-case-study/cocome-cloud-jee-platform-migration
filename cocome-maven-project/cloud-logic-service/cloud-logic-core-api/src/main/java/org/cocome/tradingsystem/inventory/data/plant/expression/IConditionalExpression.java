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

import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;

import java.util.List;

/**
 * Represents a conditional expression.
 * @author Rudolf Biczok
 */
public interface IConditionalExpression extends IExpression {

    /**
     * @return the parameter to be tested
     */
    IPlantOperationParameter getParameter();

    /**
     * @param parameter the parameter to be tested
     */
    void setParameter(IPlantOperationParameter parameter);

    /**
     * @return the expected parameter value
     */
    String getParameterValue();

    /**
     * @param parameterValue the expected parameter value
     */
    void setParameterValue(String parameterValue);

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    List<IExpression> getOnTrueExpressions();

    /**
     * @param onTrueExpressions the expression that is supposed to be executed if the condition holds
     */
    void setOnTrueExpressions(List<IExpression> onTrueExpressions);

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    List<IExpression> getOnFalseExpressions();

    /**
     * @param onFalseExpressions the expression that is supposed to be executed if the condition
     *                          does not hold
     */
    void setOnFalseExpressions(List<IExpression> onFalseExpressions);
}
