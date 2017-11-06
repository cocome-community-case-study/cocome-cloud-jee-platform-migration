package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobFinishedEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobProgressEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobStartedEvent;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

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

    @Inject
    private PUJobPool jobPool;

    public void submitOrder(final IPlantOperationOrder order) throws NotInDatabaseException {
        for (final IPlantOperationOrderEntry orderEntry : order.getOrderEntries()) {
            for (int i = 0; i < orderEntry.getAmount(); i++) {
                final PlantJob job = new PlantJob(order, orderEntry);
                jobPool.addJob(job);
                processJob(job);
            }
        }
    }

    public void onPUJobStart(@Observes PUJobStartedEvent event) {
        LOG.info("PUJob Start: " + event.getJob());
    }

    public void onPUJobProgress(@Observes PUJobProgressEvent event) {
        LOG.info("PUJob Progress: " + event.getJob());
    }

    public void onPUJobFinish(@Observes PUJobFinishedEvent event) {
        LOG.info("PUJob Finished: " + event.getJob());
        try {
            processJob(event.getJob());
        } catch (final NotInDatabaseException e) {
            LOG.error("Exception occurred while processing pu job", e);
            throw new IllegalStateException(e);
        }
    }

    public void addPUToWorkerPool(final IProductionUnit unit) throws NotInDatabaseException {
        this.workerPool.addWorker(unit);
    }

    private void processJob(final PlantJob job) throws NotInDatabaseException {
        if (job.getWorkingPackages().isEmpty()) {
            jobPool.removeJob(job);
            LOG.info("Job finished: " + job.getUUID());
            //TODO: Signal finished job
            if (!jobPool.hasJobs(job.getOrder(), job.getOrderEntry())) {
                LOG.info("Order entry finished: " + job.getOrderEntry());
                //TODO: Signal finished order entry
            }
            if (!jobPool.hasJobs(job.getOrder())) {
                LOG.info("Order finished: " + job.getOrderEntry());
                //TODO: Signal finished order
            }
            return;
        }
        final PUWorkingPackage workingPackage = job.getWorkingPackages().poll();

        //Implicit policy: submit next working package to the production unit with the lowest work load
        final Optional<PUWorker<PlantJob>> nextWorker =
                StreamUtil.ofNullable(this.workerPool.getWorkers(workingPackage.getPUC()))
                        .min(Comparator.comparingLong(PUWorker::getWorkLoad));
        if (!nextWorker.isPresent()) {
            throw new IllegalStateException("No PU is running for production unit class: "
                    + workingPackage.getPUC().getName());
        }
        nextWorker.get().submitJob(job, workingPackage.getOperations());
    }
}
