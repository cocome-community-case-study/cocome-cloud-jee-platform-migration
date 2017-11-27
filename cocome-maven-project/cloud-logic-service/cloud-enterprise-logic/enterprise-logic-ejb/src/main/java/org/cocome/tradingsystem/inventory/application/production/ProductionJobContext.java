package org.cocome.tradingsystem.inventory.application.production;

import org.cocome.tradingsystem.inventory.data.plant.recipe.exec.RecipeExecutionGraphNode;

import java.util.List;

/**
 * Holds the data needed to proceed with the execution of a specific production job
 *
 * @author Rudolf Biczok
 */
public class ProductionJobContext {
    private final ProductionJob job;
    private final List<RecipeExecutionGraphNode> associatedNodes;

    public ProductionJobContext(final ProductionJob job, final List<RecipeExecutionGraphNode> associatedNodes) {
        this.job = job;
        this.associatedNodes = associatedNodes;
    }

    public ProductionJob getJob() {
        return job;
    }

    public List<RecipeExecutionGraphNode> getAssociatedNodes() {
        return associatedNodes;
    }
}
