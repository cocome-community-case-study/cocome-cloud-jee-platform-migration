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
    @XmlElementRef(name = "parameter")
    private PlantOperationParameterTO parameter;

    /**
     * Minimal constructor
     */
    public PlantOperationParameterValueTO() {
    }

    /**
     * @param value     the parameter value
     * @param parameter the parameter
     */
    public PlantOperationParameterValueTO(final String value, final PlantOperationParameterTO parameter) {
        this.value = value;
        this.parameter = parameter;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public PlantOperationParameterTO getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(PlantOperationParameterTO parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "PlantOperationParameterValueTO{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}
