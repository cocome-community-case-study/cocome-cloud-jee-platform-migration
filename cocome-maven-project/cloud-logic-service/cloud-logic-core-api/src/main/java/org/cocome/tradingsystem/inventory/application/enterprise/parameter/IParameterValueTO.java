package org.cocome.tradingsystem.inventory.application.enterprise.parameter;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import java.io.Serializable;

/**
 * Represents a product customization parameter value (for transfer objects)
 *
 * @author Rudolf Biczok
 */
public interface IParameterValueTO<T extends IParameterTO> extends Serializable, IIdentifiableTO {

    /**
     * @return the parameter for which the value is set for
     */
    T getParameter();

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
