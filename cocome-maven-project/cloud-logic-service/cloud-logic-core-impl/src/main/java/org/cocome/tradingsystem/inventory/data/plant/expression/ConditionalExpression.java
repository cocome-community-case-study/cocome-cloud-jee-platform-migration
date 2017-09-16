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
 *
 * @author Rudolf Biczok
 */
public class ConditionalExpression extends Expression implements IConditionalExpression {
    private static final long serialVersionUID = 1L;

    private IPlantOperationParameter parameter;
    private String parameterValue;
    private List<IExpression> onTrueExpressions;
    private List<IExpression> onFalseExpressions;

    @Override
    public IPlantOperationParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(IPlantOperationParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getParameterValue() {
        return parameterValue;
    }

    @Override
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Override
    public List<IExpression> getOnTrueExpressions() {
        return onTrueExpressions;
    }

    @Override
    public void setOnTrueExpressions(List<IExpression> onTrueExpressions) {
        this.onTrueExpressions = onTrueExpressions;
    }

    @Override
    public List<IExpression> getOnFalseExpressions() {
        return onFalseExpressions;
    }

    @Override
    public void setOnFalseExpressions(List<IExpression> onFalseExpressions) {
        this.onFalseExpressions = onFalseExpressions;
    }
}
