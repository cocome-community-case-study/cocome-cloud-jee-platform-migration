package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IPlantOperationParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Holds a value for a particular {@link IPlantOperationParameter}
 *
 * @author Rudolf Biczok
 */
public interface IPlantOperationParameterValue extends IParameterValue<IPlantOperationParameter> {

    String getValue();

    void setValue(String value);

    IPlantOperationParameter getParameter() throws NotInDatabaseException;

    void setParameter(IPlantOperationParameter parameter);

    /**
     * @return the plant operation parameter id
     */
    long getParameterId();

    /**
     * @param parameterId the plant operation parameter id
     */
    void setParameterId(long parameterId);
}
