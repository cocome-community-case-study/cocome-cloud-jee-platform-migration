package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;

/**
 * This class holds all concrete data needed to evaluate expressions
 *
 * @author Rudolf Biczok
 */
public interface IEvaluationContext {

    String getValueOf(final IPlantOperationParameter parameter);
}
