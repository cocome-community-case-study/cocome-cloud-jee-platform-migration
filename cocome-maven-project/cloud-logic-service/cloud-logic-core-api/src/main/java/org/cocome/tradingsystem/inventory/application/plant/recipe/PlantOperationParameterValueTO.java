package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.enterprise.parameter.IParameterValueTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.PlantOperationParameterTO;

import javax.xml.bind.annotation.*;

/**
 * Holds a value for a particular {@link PlantOperationParameterTO}
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "PlantOperationParameterValueTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "PlantOperationParameterValueTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantOperationParameterValueTO implements IParameterValueTO<PlantOperationParameterTO> {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "value", required = true)
    private String value;
    @XmlElement(name = "parameter", required = true)
    private PlantOperationParameterTO parameter;
    @XmlElement(name = "orderEntry", required = true)
    private PlantOperationOrderEntryTO orderEntry;

    /**
     * @return The id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The parameter value
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * @param value The parameter value
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the plant operation parameter
     */
    @Override
    public PlantOperationParameterTO getParameter() {
        return parameter;
    }

    /**
     * @param parameter the plant operation parameter
     */
    @Override
    public void setParameter(PlantOperationParameterTO parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the order entry this parameter setting belongs to
     */
    public PlantOperationOrderEntryTO getOrderEntry() {
        return orderEntry;
    }

    /**
     * @param orderEntry the order entry this parameter setting belongs to
     */
    public void setOrderEntry(PlantOperationOrderEntryTO orderEntry) {
        this.orderEntry = orderEntry;
    }
}
