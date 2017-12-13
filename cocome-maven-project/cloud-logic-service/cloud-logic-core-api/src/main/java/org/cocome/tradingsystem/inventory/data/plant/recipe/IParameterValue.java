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

    IParameter getParameter() throws NotInDatabaseException;

    void setParameter(IParameter parameter);

    long getParameterId();

    void setParameterId(long parameterId);

    String getValue();

    void setValue(String value);

    IRecipeOperationOrderEntry getOrderEntry() throws NotInDatabaseException;

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
