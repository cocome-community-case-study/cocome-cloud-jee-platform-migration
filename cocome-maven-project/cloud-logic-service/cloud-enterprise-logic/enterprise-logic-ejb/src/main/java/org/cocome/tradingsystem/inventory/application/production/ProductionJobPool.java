package org.cocome.tradingsystem.inventory.application.production;

import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * This data structure is used to provide easy and fast access to all processing {@link ProductionJob}.
 *
 * @author Rudolf Biczok
 */
//TODO Much duplicated code from PlantJobPool (!)
@Singleton
@LocalBean
public class ProductionJobPool {

    private final Map<Long, Map<Long, Map<UUID, ProductionJob>>> jobPool = new Hashtable<>();

    /**
     * Adds a new job into the job pool
     *
     * @param job the job to add
     */
    public void addJob(final ProductionJob job) {
        if (!jobPool.containsKey(job.getOrder().getId())) {
            jobPool.put(job.getOrder().getId(), new Hashtable<>());
        }
        if (!jobPool.get(job.getOrder().getId())
                .containsKey(job.getOrderEntry().getId())) {
            jobPool.get(job.getOrder().getId())
                    .put(job.getOrderEntry().getId(), new Hashtable<>());
        }
        jobPool.get(job.getOrder().getId())
                .get(job.getOrderEntry().getId())
                .put(job.getUUID(), job);
    }

    /**
     * Removes the given job from the jon pool
     *
     * @param job the job to remove
     */
    public void removeJob(final ProductionJob job) {
        final Map<Long, Map<UUID, ProductionJob>> perOrderJobs = jobPool.get(job.getOrder().getId());
        final Map<UUID, ProductionJob> perOrderEntryJobs = perOrderJobs.get(job.getOrderEntry().getId());

        perOrderEntryJobs.remove(job.getUUID());

        if (perOrderEntryJobs.isEmpty()) {
            perOrderJobs.remove(job.getOrderEntry().getId());
        }
        if (perOrderJobs.isEmpty()) {
            jobPool.remove(job.getOrder().getId());
        }
    }

    /**
     * @param order      the target order
     * @param orderEntry the target order entry
     * @return {@code true} if the order entry has unfinished jobs in the job pool
     */
    public boolean hasJobs(final IPlantOperationOrder order,
                           final IPlantOperationOrderEntry orderEntry) {
        return jobPool.containsKey(order.getId())
                && jobPool.get(order.getId()).containsKey(orderEntry.getId());
    }

    /**
     * @param order the target order
     * @return {@code true} if the order has unfinished jobs in the job pool.
     */
    public boolean hasJobs(final IPlantOperationOrder order) {
        return jobPool.containsKey(order.getId());
    }
}
