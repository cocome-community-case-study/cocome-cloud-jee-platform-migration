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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an expression in the abstract syntax tree
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ExpressionInfo",
        namespace = "http://expression.plant.application.inventory.tradingsystem.cocome.org")
@XmlSeeAlso({PUOperationInfo.class, ConditionalExpressionInfo.class})
@XmlRootElement(name = "ExpressionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PUOperationInfo.class, name = "operation"),
        @JsonSubTypes.Type(value = ConditionalExpressionInfo.class, name = "ifThenElse")
})
public abstract class ExpressionInfo implements Serializable {

    /**
     * @param context the evaluation context used to extract instructions
     * @return the list of instructions extracted from this expression
     */
    public abstract List<PUInstruction> evaluate(final EvaluationContext context) throws NotInDatabaseException;

    /**
     * @return returns an evaluated list of expressions.
     */
    static List<PUInstruction> evaluateList(final List<ExpressionInfo> expressions,
                                            final EvaluationContext context)
            throws NotInDatabaseException {
        final List<PUInstruction> instructions = new LinkedList<>();
        for (final ExpressionInfo expression : expressions) {
            instructions.addAll(expression.evaluate(context));
        }
        return instructions;
    }
}
