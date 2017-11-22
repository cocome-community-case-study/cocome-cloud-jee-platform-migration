package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Holds a value for a particular {@link ICustomProductParameter}
 *
 * @author Rudolf Biczok
 */
public interface ICustomProductParameterValue extends IParameterValue<ICustomProductParameter> {

    String getValue();

    void setValue(String value);

    ICustomProductParameter getParameter() throws NotInDatabaseException;

    void setParameter(ICustomProductParameter parameter);

    /**
     * @return the plant operation parameter id
     */
    long getParameterId();

    /**
     * @param parameterId the plant operation parameter id
     */
    void setParameterId(long parameterId);
}
