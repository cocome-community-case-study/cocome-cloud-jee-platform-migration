package org.cocome.tradingsystem.inventory.application.plant.expression;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Represents an expression that can be used for plant-local recipes.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ExpressionTO",
        namespace = "http://expression.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ExpressionTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExpressionTO implements Serializable, IIdentifiableTO {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long id;

    /**
     * @return A unique identifier of this Plant.
     */
    @Override
    public long getId() {
        return this.id;
    }

    /**
     * @param id a unique identifier of this Plant
     */
    public void setId(final long id) {
        this.id = id;
    }
}
