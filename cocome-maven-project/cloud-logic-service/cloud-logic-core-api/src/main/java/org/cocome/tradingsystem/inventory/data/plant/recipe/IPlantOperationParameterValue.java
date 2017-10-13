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

    /**
     * @return The parameter value
     */
    String getValue();

    /**
     * @param value The parameter value
     */
    void setValue(String value);

    /**
     * @return the plant operation parameter
     */
    IPlantOperationParameter getParameter() throws NotInDatabaseException;

    /**
     * @param parameter the plant operation parameter
     */
    void setParameter(IPlantOperationParameter parameter);

    /**
     * @return the plant operation parameter id
     */
    long getParameterId();

    /**
     * @param parameterId the plant operation parameter id
     */
    void setParameterId(long parameterId);

    /**
     * @return the order entry this parameter setting belongs to
     */
    IPlantOperationOrderEntry getOrderEntry() throws NotInDatabaseException;

    /**
     * @param orderEntry the order entry this parameter setting belongs to
     */
    void setOrderEntry(IPlantOperationOrderEntry orderEntry);

    /**
     * @return the id of the order entry this parameter setting belongs to
     */
    long getOrderEntryId();

    /**
     * @param orderEntryId the id of the order entry this parameter setting belongs to
     */
    void setOrderEntryId(long orderEntryId);
}
