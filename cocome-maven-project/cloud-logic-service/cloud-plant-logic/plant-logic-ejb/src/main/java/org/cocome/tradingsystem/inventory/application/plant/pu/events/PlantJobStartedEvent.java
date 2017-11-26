package org.cocome.tradingsystem.inventory.application.plant.pu.events;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.pu.PlantJob;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

/**
 * This event class is issued when a job on a production unit has started
 *
 * @author Rudolf Biczok
 */
public class PlantJobStartedEvent extends AbstractPlantJobEvent {

    private static final long serialVersionUID = -1L;

    /**
     * Canonical constructor
     *
     * @param productionUnit the production unit associated with this event
     * @param job            the job
     * @param historyEntry   the history object causing this event
     */
    public PlantJobStartedEvent(IProductionUnit productionUnit, PlantJob job, HistoryEntry historyEntry) {
        super(productionUnit, job, historyEntry);
    }
}
