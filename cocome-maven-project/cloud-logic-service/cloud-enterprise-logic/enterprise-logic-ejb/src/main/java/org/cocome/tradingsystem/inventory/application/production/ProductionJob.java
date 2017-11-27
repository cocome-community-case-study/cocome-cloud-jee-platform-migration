package org.cocome.tradingsystem.inventory.application.production;

import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IProductionOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IProductionOrderEntry;
import org.cocome.tradingsystem.inventory.data.plant.recipe.exec.RecipeExecutionGraph;
import org.cocome.tradingsystem.inventory.data.plant.recipe.exec.RecipeExecutionGraphEdge;
import org.cocome.tradingsystem.inventory.data.plant.recipe.exec.RecipeExecutionGraphNode;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.exception.RecipeException;

import java.util.*;

/**
 * The job data for one specific order
 *
 * @author Rudolf Biczok
 */
public class ProductionJob {

    private final UUID uuid;

    private final IStoreManager storeManager;
    private final IProductionOrder order;

    private final IProductionOrderEntry orderEntry;
    private final RecipeExecutionGraph executionGraph;

    private final Map<RecipeExecutionGraphNode, Long> perNodeDependencyCount = new HashMap<>();
    private long finishCount;

    public ProductionJob(final IStoreManager storeManager,
                         final IProductionOrder order,
                         final IProductionOrderEntry orderEntry) throws NotInDatabaseException, RecipeException {
        this.uuid = UUID.randomUUID();
        this.storeManager = storeManager;
        this.order = order;
        this.orderEntry = orderEntry;
        this.executionGraph = new RecipeExecutionGraph(orderEntry.getRecipe());

        for (final RecipeExecutionGraphNode node : this.executionGraph.getStartNodes()) {
            scanNodeDependancies(node);
        }
    }

    private void scanNodeDependancies(RecipeExecutionGraphNode node) {
        if (node.getEdges().isEmpty()) {
            this.finishCount++;
            return;
        }

        for (final RecipeExecutionGraphEdge edge : node.getEdges()) {
            this.perNodeDependencyCount.putIfAbsent(edge.getNext(), 0L);
            this.perNodeDependencyCount.put(
                    edge.getNext(),
                    this.perNodeDependencyCount.getOrDefault(edge.getNext(), 0L) + 1);
        }
    }

    public synchronized List<RecipeExecutionGraphNode> getNextNodes(final List<RecipeExecutionGraphNode> finishedNodes) {
        final List<RecipeExecutionGraphNode> nextNodes = new LinkedList<>();
        for (final RecipeExecutionGraphNode finishedNode : finishedNodes) {
            if (finishedNode.getEdges().isEmpty()) {
                finishCount--;
            }
            for (final RecipeExecutionGraphEdge edge : finishedNode.getEdges()) {
                this.perNodeDependencyCount.put(edge.getNext(), this.perNodeDependencyCount.get(edge.getNext()));
                if (this.perNodeDependencyCount.get(edge.getNext()) <= 0) {
                    nextNodes.add(edge.getNext());
                }
            }
        }
        return nextNodes;
    }

    public UUID getUUID() {
        return uuid;
    }

    public RecipeExecutionGraph getExecutionGraph() {
        return executionGraph;
    }

    public IStoreManager getStoreManager() {
        return storeManager;
    }

    public IProductionOrder getOrder() {
        return order;
    }

    public IProductionOrderEntry getOrderEntry() {
        return orderEntry;
    }

    public synchronized boolean hasFinished() {
        return finishCount <= 0;
    }
}
