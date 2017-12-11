package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.INameableTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Common interface for nestable recipes or atomic plant operations
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "RecipeOperationTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "RecipeOperationTO")
@XmlSeeAlso({RecipeTO.class, PlantOperationTO.class})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class RecipeOperationTO implements Serializable, INameableTO {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }
}
