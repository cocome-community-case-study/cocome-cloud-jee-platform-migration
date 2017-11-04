package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an expression that can be used for plant-local recipes.
 *
 * @author Rudolf Biczok
 */
public interface IExpression extends IIdentifiable {

    /**
     * @param context the evaluation context used to extract instructions
     * @return the list of instructions extracted from this expression
     */
    List<IPUInstruction> evaluate(final IEvaluationContext context) throws NotInDatabaseException;

    /**
     * @return returns an evaluated list of expressions.
     */
    static List<IPUInstruction> evaluateList(final Collection<IExpression> expressions, final IEvaluationContext context)
            throws NotInDatabaseException {
        final List<IPUInstruction> instructions = new LinkedList<>();
        for (final IExpression expression : expressions) {
            instructions.addAll(expression.evaluate(context));
        }
        return instructions;
    }
}
