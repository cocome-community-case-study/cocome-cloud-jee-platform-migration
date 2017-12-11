package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IParameter;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.io.Serializable;

/**
 * Represents a product customization parameter values
 *
 * @author Rudolf Biczok
 */
public interface IParameterValue extends Serializable, IIdentifiable {

    /**
     * @return the parameter for which the value is set for
     */
    IParameter getParameter() throws NotInDatabaseException;

    /**
     * @param parameter the parameter for which the value is set for
     */
    void setParameter(IParameter parameter);

    /**
     * @return the plant operation parameter id
     */
    long getParameterId();

    /**
     * @param parameterId the plant operation parameter id
     */
    void setParameterId(long parameterId);

    /**
     * @return the parameter value
     */
    String getValue();

    /**
     * @param value the parameter value
     */
    void setValue(String value);

    /**
     * @return the order entry this parameter setting belongs to
     */
     IRecipeOperationOrderEntry getOrderEntry() throws NotInDatabaseException;

    /**
     * @param orderEntry the order entry this parameter setting belongs to
     */
    void setOrderEntry(IRecipeOperationOrderEntry orderEntry);

    long getOrderEntryId();

    void setOrderEntryId(long orderEntryId);

    /**
     * @return {@code true} if this parameter is valid
     */
    default boolean isValid() throws NotInDatabaseException {
        return null != this.getParameter() && this.getParameter().isValidValue(this.getValue());
    }
}
