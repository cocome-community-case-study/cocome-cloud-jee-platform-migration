package org.cocome.tradingsystem.inventory.application.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationParameterValue;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to extract the concrete list of instructions supposed to be send to production units
 *
 * @author Rudolf Biczok
 */
public class EvaluationContext {

    private final Map<String, String> paramValueMapping = new HashMap<>();

    public EvaluationContext(final Collection<IPlantOperationParameterValue> parameterValueList)
            throws NotInDatabaseException {
        for (final IPlantOperationParameterValue parameterValue : parameterValueList) {
            paramValueMapping.put(parameterValue.getParameter().getName(), parameterValue.getValue());
        }
    }

    public String getValueOf(final String parameterName) {
        return paramValueMapping.get(parameterName);
    }
}
