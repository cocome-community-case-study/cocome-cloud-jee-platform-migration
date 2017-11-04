package org.cocome.tradingsystem.inventory.application.plant.pu.events;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

import java.io.Serializable;
import java.util.UUID;

/**
 * The abstract class for any event time comming from an production unit
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractPUJobEvent implements Serializable {

    private static final long serialVersionUID = -1L;

    private final IProductionUnit productionUnit;
    private final UUID job;
    private final HistoryEntry historyEntry;

    /**
     * Canonical constructor
     *
     * @param productionUnit the production unit associated with this event
     * @param job            the job data used for execution
     * @param historyEntry   the history object causing this event
     */
    AbstractPUJobEvent(IProductionUnit productionUnit,
                       UUID job,
                       HistoryEntry historyEntry) {
        this.productionUnit = productionUnit;
        this.job = job;
        this.historyEntry = historyEntry;
    }

    /**
     * @return the job data
     */
    public UUID getJob() {
        return job;
    }

    /**
     * @return the production unit associated with this event
     */
    public IProductionUnit getProductionUnit() {
        return productionUnit;
    }

    /**
     * @return the history object causing this event
     */
    public HistoryEntry getHistoryEntry() {
        return historyEntry;
    }
}
