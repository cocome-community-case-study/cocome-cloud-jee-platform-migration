package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates all worker units managed by an plant server instance {@link IPlant}.
 * The worker units are grouped by their {@link IPlant} instanced and then by
 * {@link IProductionUnitClass}.
 *
 * @author Rudolf Biczok
 */
@Singleton
@LocalBean
public class PUWorkerPool {

    private static final Logger LOG = Logger.getLogger(PUWorkerPool.class);

    @Inject
    private IPlantQuery plantQuery;

    @Inject
    private PUWorkerFactory workerFactory;

    private final Map<Long, Map<Long, List<PUWorker>>> workers = new HashMap<>();

    @PreDestroy
    public void shutdownWorkers() {
        for (final Map<Long, List<PUWorker>> plantPool : workers.values()) {
            for (final List<PUWorker> localWorkerPool : plantPool.values()) {
                for (final PUWorker worker : localWorkerPool) {
                    worker.close();
                    try {
                        worker.getThread().join();
                    } catch (InterruptedException e) {
                        LOG.error("Unable to wait for worker termination", e);
                    }
                    LOG.debug("Create worker for unit " + worker.getProductionUnit());
                }
            }
        }
    }

    public List<PUWorker> getWorkers(final IProductionUnitClass puc)
            throws NotInDatabaseException {
        checkAndInitPlantWorkers(puc.getPlant());
        return workers.get(puc.getPlant().getId()).get(puc.getId());
    }

    public void addWorker(final IProductionUnit unit) throws NotInDatabaseException {
        checkAndInitPlantWorkers(unit.getPlant());
        final Map<Long, List<PUWorker>> plantWorkers = workers.get(unit.getPlant().getId());
        if (!plantWorkers.containsKey(unit.getProductionUnitClass().getId())) {
            plantWorkers.put(unit.getProductionUnitClass().getId(), new LinkedList<>());
        }
        LOG.debug("Create worker for unit " + unit);
        plantWorkers.get(unit.getProductionUnitClass().getId()).add(workerFactory.createWorker(unit));
    }

    /**
     * Used to initialize pre-existing plant production units on demand
     *
     * @param plant the target plant
     */
    private void checkAndInitPlantWorkers(IPlant plant) throws NotInDatabaseException {
        if (workers.containsKey(plant.getId())) {
            return;
        }
        LOG.debug("Lazy worker pool initialization for plant with id=" + plant.getId());
        workers.put(plant.getId(), new HashMap<>());

        for (final IProductionUnit unit : plantQuery.queryProductionUnits(plant.getId())) {
            this.addWorker(unit);
        }
    }
}
