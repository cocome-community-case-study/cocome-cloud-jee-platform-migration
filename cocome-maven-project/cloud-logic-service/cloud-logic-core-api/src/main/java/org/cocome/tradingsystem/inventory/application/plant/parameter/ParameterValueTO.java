package org.cocome.tradingsystem.inventory.application.plant.parameter;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Holds a value for a particular {@link ParameterTO}
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ParameterValueTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ParameterValueTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterValueTO implements Serializable, IIdentifiableTO {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "value", required = true)
    private String value;
    @XmlElementRef(name = "parameter")
    private ParameterTO parameter;

    /**
     * Minimal constructor
     */
    public ParameterValueTO() {
    }

    /**
     * @param value     the parameter value
     * @param parameter the parameter
     */
    public ParameterValueTO(final String value, final ParameterTO parameter) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ParameterTO getParameter() {
        return parameter;
    }

    public void setParameter(ParameterTO parameter) {
        this.parameter = parameter;
    }

    public String toString() {
        return "ParameterValueTO{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}
