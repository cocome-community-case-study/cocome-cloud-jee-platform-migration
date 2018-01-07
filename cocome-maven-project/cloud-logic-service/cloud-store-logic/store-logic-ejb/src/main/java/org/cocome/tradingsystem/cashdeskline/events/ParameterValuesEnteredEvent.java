package org.cocome.tradingsystem.cashdeskline.events;

import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IParameterValue;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Used as a signal when product parameters have been specified by the customer
 *
 * @author Rudolf Biczok
 */
public class ParameterValuesEnteredEvent implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Collection<ParameterValueTO> parameterValues;

    public ParameterValuesEnteredEvent(Collection<ParameterValueTO> parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
     * @return the specified parameters for recently scanned product
     */
    public Collection<ParameterValueTO> getParameterValues() {
        return parameterValues;
    }
}
