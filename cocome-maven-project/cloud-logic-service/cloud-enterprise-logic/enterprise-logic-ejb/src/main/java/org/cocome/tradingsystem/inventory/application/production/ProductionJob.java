package org.cocome.tradingsystem.inventory.application.production;

import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IProductionOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IProductionOrderEntry;
import org.cocome.tradingsystem.inventory.data.plant.recipe.exec.RecipeExecutionGraph;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.exception.RecipeException;

import java.util.UUID;

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

    public ProductionJob(final IStoreManager storeManager,
                         final IProductionOrder order,
                         final IProductionOrderEntry orderEntry) throws NotInDatabaseException, RecipeException {
        this.uuid = UUID.randomUUID();
        this.storeManager = storeManager;
        this.order = order;
        this.orderEntry = orderEntry;
        this.executionGraph = new RecipeExecutionGraph(orderEntry.getRecipe());
    }


    public UUID getUUID() {
        return uuid;
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

    public RecipeExecutionGraph getExecutionGraph() {
        return executionGraph;
    }

}
