package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PlantJobFinishedEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PlantJobProgressEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PlantJobStartedEvent;
import org.cocome.tradingsystem.inventory.data.enterprise.EnterpriseClientFactory;
import org.cocome.tradingsystem.inventory.data.persistence.CloudPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
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
    private IPersistenceContext persistenceContext;

    @Inject
    private EnterpriseClientFactory enterpriseClientFactory;

    @Inject
    private PUWorkerPool workerPool;

    @Inject
    private PlantJobPool jobPool;

    @Inject
    private IPlantQuery plantQuery;

    public void submitOrder(final IPlantOperationOrder order) throws NotInDatabaseException, UpdateException {
        final Collection<IProductionUnitClass> pucList =
                plantQuery.queryProductionUnitClassesByPlantId(order.getPlant().getId());
        for (final IPlantOperationOrderEntry orderEntry : order.getOrderEntries()) {
            for (int i = 0; i < orderEntry.getAmount(); i++) {
                final IEnterpriseManager enterpriseManager = enterpriseClientFactory.createClient(
                        order.getEnterprise().getId());
                final PlantJob job = new PlantJob(enterpriseManager, pucList, order, orderEntry);
                jobPool.addJob(job);
                processJob(job);
            }
        }
    }

    public void onPUJobStart(@Observes PlantJobStartedEvent event) {
        LOG.info("PUJob Start: " + event.getJob());
    }

    public void onPUJobProgress(@Observes PlantJobProgressEvent event) {
        LOG.info("PUJob Progress: " + event.getJob());
    }

    public void onPUJobFinish(@Observes PlantJobFinishedEvent event) {
        LOG.info("PUJob Finished: " + event.getJob());
        try {
            processJob(event.getJob());
        } catch (final UpdateException | NotInDatabaseException e) {
            LOG.error("Exception occurred while processing pu job", e);
            throw new IllegalStateException(e);
        }
    }

    public void addPUToWorkerPool(final IProductionUnit unit) throws NotInDatabaseException {
        this.workerPool.addWorker(unit);
    }

    private void processJob(final PlantJob job) throws NotInDatabaseException, UpdateException {
        if (job.getWorkingPackages().isEmpty()) {
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
