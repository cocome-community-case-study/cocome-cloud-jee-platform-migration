package org.cocome.tradingsystem.inventory.application.production;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IStoreManager;
import org.cocome.tradingsystem.inventory.application.production.events.PlantOperationOrderFinishedEvent;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IProductionOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IProductionOrderEntry;
import org.cocome.tradingsystem.inventory.data.store.StoreClientFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.exception.RecipeException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Generates the actual interface classes for access the production unit services
 *
 * @author Rudolf Biczok
 */
@Singleton
@LocalBean
public class ProductionManager {

    private static final Logger LOG = Logger.getLogger(ProductionManager.class);

    @Inject
    private StoreClientFactory storeClientFactory;

    @Inject
    private ProductionJobPool jobPool;

    public void submitOrder(final IProductionOrder order) throws NotInDatabaseException, RecipeException {
        for (final IProductionOrderEntry orderEntry : order.getOrderEntries()) {
            for (int i = 0; i < orderEntry.getAmount(); i++) {
                final IStoreManager storeManager = storeClientFactory.createClient(
                        order.getStore().getId());
                final ProductionJob job = new ProductionJob(storeManager, order, orderEntry);
                jobPool.addJob(job);
                processJob(job);
            }
        }
    }

    public void onPUJobFinish(@Observes PlantOperationOrderFinishedEvent event) {
        /*LOG.info("PUJob Finished: " + event.getJob());
        try {
            processJob(event.getJob());
        } catch (final NotInDatabaseException e) {
            LOG.error("Exception occurred while processing pu job", e);
            throw new IllegalStateException(e);
        }*/
    }

    private void processJob(final ProductionJob job) throws NotInDatabaseException {
        /*if (job.getWorkingPackages().isEmpty()) {
            jobPool.removeJob(job);
            LOG.info("Job finished: " + job.getUUID());

            job.getEnterpriseManager().onPlantOperationFinish(job.getOrderEntry().getId());
            if (!jobPool.hasJobs(job.getOrder(), job.getOrderEntry())) {
                LOG.info("Order entry finished: " + job.getOrderEntry());
                job.getEnterpriseManager().onPlantOperationOrderEntryFinish(job.getOrderEntry().getId());
            }
            if (!jobPool.hasJobs(job.getOrder())) {
                LOG.info("Order finished: " + job.getOrderEntry());
                job.getOrder().setDeliveryDate(new Date());
                persistenceContext.updateEntity(job.getOrder());
                job.getEnterpriseManager().onPlantOperationOrderFinish(job.getOrder().getId());
            }
            return;
        }
        final PUWorkingPackage workingPackage = job.getWorkingPackages().poll();

        //Implicit policy: submit next working package to the production unit with the lowest work load
        final Optional<PUWorker<ProductionJob>> nextWorker =
                StreamUtil.ofNullable(this.workerPool.getWorkers(workingPackage.getPUC()))
                        .min(Comparator.comparingLong(PUWorker::getWorkLoad));
        if (!nextWorker.isPresent()) {
            throw new IllegalStateException("No PU is running for production unit class: "
                    + workingPackage.getPUC().getName());
        }
        nextWorker.get().submitJob(job, workingPackage.getOperations());*/
    }
}
