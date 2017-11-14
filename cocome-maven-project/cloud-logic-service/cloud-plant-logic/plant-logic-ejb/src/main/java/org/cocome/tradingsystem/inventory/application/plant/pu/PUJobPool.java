package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * This data structure is used to provide easy and fast access to all processing {@link PlantJob}.
 *
 * @author Rudolf Biczok
 */
@Singleton
@LocalBean
public class PUJobPool {

    private final Map<Long, Map<Long, Map<UUID, PlantJob>>> jobPool = new Hashtable<>();

    /**
     * Adds a new job into the job pool
     *
     * @param job the job to add
     */
    public void addJob(final PlantJob job) {
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
    public void removeJob(final PlantJob job) {
        final Map<Long, Map<UUID, PlantJob>> perOrderJobs = jobPool.get(job.getOrder().getId());
        final Map<UUID, PlantJob> perOrderEntryJobs = perOrderJobs.get(job.getOrderEntry().getId());

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
