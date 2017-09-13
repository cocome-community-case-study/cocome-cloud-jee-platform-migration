package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.xml.bind.annotation.*;
import java.util.Collection;

/**
 * Represents the top-level recipe for producing a custom product.
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "RecipeTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "RecipeTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeTO implements IIdentifiableTO {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id", required = true)
    private long id;

    // Represent the vertices of the recipe graph
    @XmlElement(name = "operations", required = true)
    private Collection<PlantOperationTO> operations;

    // Represent the edges of the recipe graph
    @XmlElement(name = "parameterInteractions", required = true)
    private Collection<ParameterInteractionTO> parameterInteractions;
    @XmlElement(name = "inputInteractions", required = true)
    private Collection<EntryPointInteractionTO> inputInteractions;
    @XmlElement(name = "outputInteractions", required = true)
    private Collection<EntryPointInteractionTO> outputInteractions;

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
    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the plant operation that is supposed to be executed within this interaction step
     */
    public Collection<PlantOperationTO> getOperations() {
        return operations;
    }

    /**
     * @param operations the plant operation that is supposed to be executed within this interaction step
     */
    public void setOperations(Collection<PlantOperationTO> operations) {
        this.operations = operations;
    }

    /**
     * @return the list of incoming interactions
     */
    public Collection<EntryPointInteractionTO> getInputInteractions() {
        return inputInteractions;
    }

    /**
     * @param inputInteractions the list of incoming interactions
     */
    public void setInputInteractions(Collection<EntryPointInteractionTO> inputInteractions) {
        this.inputInteractions = inputInteractions;
    }

    /**
     * @return the list of outgoing interactions
     */
    public Collection<EntryPointInteractionTO> getOutputInteractions() {
        return outputInteractions;
    }

    /**
     * @param outputInteractions the list of outgoing interaction
     */
    public void setOutputInteractions(Collection<EntryPointInteractionTO> outputInteractions) {
        this.outputInteractions = outputInteractions;
    }

    /**
     * @return the list of parameter bindings
     */
    public Collection<ParameterInteractionTO> getParameterInteractions() {
        return parameterInteractions;
    }

    /**
     * @param parameterInteractions the list of parameter bindings
     */
    public void setParameterInteractions(Collection<ParameterInteractionTO> parameterInteractions) {
        this.parameterInteractions = parameterInteractions;
    }
}
