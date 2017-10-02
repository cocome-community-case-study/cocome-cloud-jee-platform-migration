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
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.List;

/**
 * Represents a conditional expression.
 *
 * @author Rudolf Biczok
 */
public interface IConditionalExpression extends IExpression {

    /**
     * @return the parameter to be tested
     */
    IPlantOperationParameter getParameter() throws NotInDatabaseException;

    /**
     * @param parameter the parameter to be tested
     */
    void setParameter(IPlantOperationParameter parameter);

    /**
     * @return the id of the referencing parameter
     */
    long getParameterId();

    /**
     * @param parameterId the id of the referencing parameter
     */
    void setParameterId(long parameterId);

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
    List<IExpression> getOnTrueExpressions() throws NotInDatabaseException;

    /**
     * @param onTrueExpressions the expression that is supposed to be executed if the condition holds
     */
    void setOnTrueExpressions(List<IExpression> onTrueExpressions);

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    List<IExpression> getOnFalseExpressions() throws NotInDatabaseException;

    /**
     * @param onFalseExpressions the expression that is supposed to be executed if the condition
     *                           does not hold
     */
    void setOnFalseExpressions(List<IExpression> onFalseExpressions);

    /**
     * @return the ids of the expressions for the true case
     */
    List<Long> getOnTrueExpressionIds();

    /**
     * @param onTrueExpressionIds the ids of the expressions for the true case
     */
    void setOnTrueExpressionIds(List<Long> onTrueExpressionIds);

    /**
     * @return the ids of the expressions for the false case
     */
    List<Long> getOnFalseExpressionIds();

    /**
     * @param onFalseExpressionIds the ids of the expressions for the false case
     */
    void setOnFalseExpressionIds(List<Long> onFalseExpressionIds);
}
