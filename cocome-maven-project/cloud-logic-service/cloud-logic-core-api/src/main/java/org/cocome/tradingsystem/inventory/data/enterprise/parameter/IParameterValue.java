package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.io.Serializable;

/**
 * Represents a product customization parameter values
 *
 * @author Rudolf Biczok
 */
public interface IParameterValue<T extends IParameter> extends Serializable, IIdentifiable {

    /**
     * @return the parameter for which the value is set for
     */
    T getParameter() throws NotInDatabaseException;

    /**
     * @param parameter the parameter for which the value is set for
     */
    void setParameter(T parameter);

    /**
     * @return the parameter value
     */
    String getValue();

    /**
     * @param value the parameter value
     */
    void setValue(String value);
}
