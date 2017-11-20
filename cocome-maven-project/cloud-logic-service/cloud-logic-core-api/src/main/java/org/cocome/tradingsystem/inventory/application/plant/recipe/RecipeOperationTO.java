package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.INameableTO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Common interface for nestable recipes or atomic plant operations
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "RecipeOperationTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "RecipeOperationTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeOperationTO implements Serializable, INameableTO {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "name", required = true)
    private String name;

    @XmlElement(name = "inputEntryPoint", required = true)
    private Collection<EntryPointTO> inputEntryPoint;
    @XmlElement(name = "outputEntryPoint", required = true)
    private Collection<EntryPointTO> outputEntryPoint;

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

    /**
     * @return all material classes that are required for operation execution
     */
    public Collection<EntryPointTO> getInputEntryPoint() {
        return inputEntryPoint;
    }

    /**
     * @param inputMaterial all material classes that are required for operation execution
     */
    public void setInputEntryPoint(Collection<EntryPointTO> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    /**
     * @return all material classes that results after the operation execution
     */
    public Collection<EntryPointTO> getOutputEntryPoint() {
        return outputEntryPoint;
    }

    /**
     * @param outputMaterial all material classes that results after the operation execution
     */
    public void setOutputEntryPoint(Collection<EntryPointTO> outputMaterial) {
        this.outputEntryPoint = outputMaterial;
    }
}
