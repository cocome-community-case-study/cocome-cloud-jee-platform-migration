package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobFinishedEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobProgressEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobStartedEvent;
import org.cocome.tradingsystem.inventory.data.plant.expression.EvaluationContext;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IPUInstruction;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Generates the actual interface classes for access the production unit services
 *
 * @author Rudolf Biczok
 */
@Singleton
@LocalBean
public class PUManager {

    private static final Logger LOG = Logger.getLogger(PUManager.class);

    @Inject
    private PUWorkerPool workerPool;

    private final Map<Long, IPlantOperationOrder> orderMap = new Hashtable<>();

    private final Map<Long, IPlantOperationOrderEntry> orderEntryMap = new Hashtable<>();

    public void submitOrder(final IPlantOperationOrder order) throws NotInDatabaseException {
        for (final IPlantOperationOrderEntry orderEntry : order.getOrderEntries()) {
            for (int i = 0; i < orderEntry.getAmount(); i++) {
                final Queue<List<IPUInstruction>> groupedInstructions = extractWorkingPackage(orderEntry);
            }
        }
    }

    public void onJobStart(@Observes PUJobStartedEvent event) {
        LOG.info("Job Start: " + event.getJob());
    }

    public void onJobProgress(@Observes PUJobProgressEvent event) {
        LOG.info("Job Progress: " + event.getJob());
    }

    public void onJobFinish(@Observes PUJobFinishedEvent event) {
        LOG.info("Job Finish: %d" + event.getJob());
    }

    public void addPUToWorkerPool(final IProductionUnit unit) throws NotInDatabaseException {
        this.workerPool.addWorker(unit);
    }

    private Queue<List<IPUInstruction>> extractWorkingPackage(IPlantOperationOrderEntry orderEntry)
            throws NotInDatabaseException {
        return null;
    }

}
