package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationParameterValue;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link IEvaluationContext}, no equivalent in the database
 *
 * @author Rudolf Biczok
 */
public class EvaluationContext implements IEvaluationContext {

    private final Map<String, String> paramValueMapping = new HashMap<>();

    public EvaluationContext(final Collection<IPlantOperationParameterValue> parameterValueList)
            throws NotInDatabaseException {
        for (final IPlantOperationParameterValue parameterValue : parameterValueList) {
            paramValueMapping.put(parameterValue.getParameter().getName(), parameterValue.getValue());
        }
    }

    @Override
    public String getValueOf(IPlantOperationParameter parameter) {
        return paramValueMapping.get(parameter.getName());
    }
}
