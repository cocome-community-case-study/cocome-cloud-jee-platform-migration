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

package org.cocome.tradingsystem.inventory.application.plant.expression;

import org.cocome.tradingsystem.inventory.application.plant.parameter.PlantOperationParameterTO;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Represents a conditional expression.
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ConditionalExpressionTO",
        namespace = "http://expression.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ConditionalExpressionTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionalExpressionTO extends ExpressionTO {
    private static final long serialVersionUID = 1L;

    @XmlElementRef(name = "parameter")
    private PlantOperationParameterTO parameter;
    @XmlElement(name = "parameterValue", required = true)
    private String parameterValue;
    @XmlElement(name = "onTrueExpressions", required = true)
    private List<ExpressionTO> onTrueExpressions;
    @XmlElement(name = "onFalseExpressions", required = true)
    private List<ExpressionTO> onFalseExpressions;

    /**
     * @return the parameter to be tested
     */
    public PlantOperationParameterTO getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to be tested
     */
    public void setParameter(PlantOperationParameterTO parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the expected parameter value
     */
    public String getParameterValue() {
        return parameterValue;
    }

    /**
     * @param parameterValue the expected parameter value
     */
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    public List<ExpressionTO> getOnTrueExpressions() {
        return onTrueExpressions;
    }

    /**
     * @param onTrueExpressions the expression that is supposed to be executed if the condition holds
     */
    public void setOnTrueExpressions(List<ExpressionTO> onTrueExpressions) {
        this.onTrueExpressions = onTrueExpressions;
    }

    /**
     * @return the expression that is supposed to be executed if the condition holds
     */
    public List<ExpressionTO> getOnFalseExpressions() {
        return onFalseExpressions;
    }

    /**
     * @param onFalseExpressions the expression that is supposed to be executed if the condition
     *                          does not hold
     */
    public void setOnFalseExpressions(List<ExpressionTO> onFalseExpressions) {
        this.onFalseExpressions = onFalseExpressions;
    }
}
