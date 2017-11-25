package org.cocome.tradingsystem.inventory.application.enterprise.parameter;

import javax.xml.bind.annotation.*;

/**
 * Holds a value for a particular {@link CustomProductParameterTO}
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "CustomProductParameterValueTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "CustomProductParameterValueTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomProductParameterValueTO implements IParameterValueTO<CustomProductParameterTO> {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "value", required = true)
    private String value;
    @XmlElementRef(name = "parameter")
    private CustomProductParameterTO parameter;

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
    public CustomProductParameterTO getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(CustomProductParameterTO parameter) {
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
