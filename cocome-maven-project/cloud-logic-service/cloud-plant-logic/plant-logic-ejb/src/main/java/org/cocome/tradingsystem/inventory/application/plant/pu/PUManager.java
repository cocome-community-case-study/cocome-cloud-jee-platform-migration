package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobFinishedEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobProgressEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobStartedEvent;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    private PUWorkerFactory workerFactory;

    @Inject
    private IEnterpriseQuery enterpriseQuery;

    private Queue<IPlantOperationOrder> orderQueue = new ConcurrentLinkedQueue<>();

    private Map<Long, IPlant> plantPUMap = new HashMap<>();

    private Thread managerThread;

    @PostConstruct
    public void initBackgroundThread() {

    }

    @PreDestroy
    public void waitAndCloseManagerThread() {

    }

    public void submitOrder(final IPlantOperationOrder order) {
        orderQueue.add(order);
    }

    public void onJobStart(@Observes PUJobStartedEvent event) {
        LOG.info(String.format("Job Start: %s", event.getProductionUnit().getId()));
    }

    public void onJobProgress(@Observes PUJobProgressEvent event) {
        LOG.info(String.format("Job Progress: %s", event.getProductionUnit().getId()));
    }

    public void onJobFinish(@Observes PUJobFinishedEvent event) {
        LOG.info(String.format("Job Finish: %s", event.getProductionUnit().getId()));
    }
}
