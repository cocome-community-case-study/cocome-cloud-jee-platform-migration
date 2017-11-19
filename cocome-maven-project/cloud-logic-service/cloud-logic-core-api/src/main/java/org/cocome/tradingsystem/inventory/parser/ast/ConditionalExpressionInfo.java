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

import java.util.List;

/**
 * Conditional expression, modelling an if-then-else construct in the abstract syntax tree
 *
 * @author Rudolf Biczok
 */
public class ConditionalExpressionInfo implements ExpressionInfo {

    private String parameterName;

    private String testValue;

    private List<ExpressionInfo> onTrueExpressions;

    private List<ExpressionInfo> onFalseExpressions;

    /**
     * Canonical constructor
     *
     * @param parameterName      the name of the parameter
     * @param testValue          the expected test value
     * @param onTrueExpressions  the list of expressions which are supposed to be executed if the value of
     *                           the parameter with name {@code parameterName} matches {@code testValue}
     * @param onFalseExpressions the list of expressions which are supposed to be executed if the value of
     *                           the parameter with name {@code parameterName} don't matches {@code testValue}
     */
    public ConditionalExpressionInfo(final String parameterName,
                                     final String testValue,
                                     final List<ExpressionInfo> onTrueExpressions,
                                     final List<ExpressionInfo> onFalseExpressions) {
        this.parameterName = parameterName;
        this.testValue = testValue;
        this.onTrueExpressions = onTrueExpressions;
        this.onFalseExpressions = onFalseExpressions;
    }

    public ConditionalExpressionInfo() {
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

    public List<ExpressionInfo> getOnTrueExpressions() {
        return onTrueExpressions;
    }

    public void setOnTrueExpressions(List<ExpressionInfo> onTrueExpressions) {
        this.onTrueExpressions = onTrueExpressions;
    }

    public List<ExpressionInfo> getOnFalseExpressions() {
        return onFalseExpressions;
    }

    public void setOnFalseExpressions(List<ExpressionInfo> onFalseExpressions) {
        this.onFalseExpressions = onFalseExpressions;
    }
}
