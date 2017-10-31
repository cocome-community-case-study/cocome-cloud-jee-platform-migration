package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates all worker units that belong to one particular {@link IPlant}.
 * The worker units are grouped by their {@link IProductionUnitClass}.
 *
 * @author Rudolf Biczok
 */
public class PUWorkerPool {
    private final IPlant plant;
    private final Map<Long, List<PUWorker>> workers = new HashMap<>();

    /**
     * Canonical constructor
     *
     * @param plant the owning plant
     */
    public PUWorkerPool(IPlant plant) {
        this.plant = plant;
    }

    public IPlant getPlant() {
        return plant;
    }

    public List<PUWorker> getWorkers(final IProductionUnitClass puc) {
        return workers.get(puc.getId());
    }

    public void addWorker(final PUWorker worker) throws NotInDatabaseException {
        if (!workers.containsKey(worker.getProductionUnit().getProductionUnitClass().getId())) {
            workers.put(worker.getProductionUnit().getProductionUnitClass().getId(), new LinkedList<>());
        }
        workers.get(worker.getProductionUnit().getProductionUnitClass().getId()).add(worker);
    }

    public void removePUC(final IProductionUnitClass puc) {
        workers.remove(puc.getId());
    }

}
