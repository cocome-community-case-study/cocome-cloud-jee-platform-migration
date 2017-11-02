package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobFinishedEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobProgressEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobStartedEvent;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

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

    public void submitOrder(final IPlantOperationOrder order) {
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

    public void addPUToWorkerPool(final IProductionUnit unit) throws NotInDatabaseException {
        this.workerPool.addWorker(unit);
    }
}
