package org.cocome.tradingsystem.inventory.application.plant.pu.events;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.pu.PlantJob;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

/**
 * This event class is issued when a job on a production unit has finished
 *
 * @author Rudolf Biczok
 */
public class PlantJobFinishedEvent extends AbstractPlantJobEvent {

    private static final long serialVersionUID = -1L;

    /**
     * Canonical constructor
     *
     * @param productionUnit the production unit associated with this event
     * @param job            the job data used for execution
     * @param historyEntry   the history object causing this event
     */
    public PlantJobFinishedEvent(IProductionUnit productionUnit, PlantJob job, HistoryEntry historyEntry) {
        super(productionUnit, job, historyEntry);
    }
}
