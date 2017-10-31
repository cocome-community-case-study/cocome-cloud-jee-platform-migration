package org.cocome.tradingsystem.inventory.application.plant.pu.events;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

import java.io.Serializable;

/**
 * The abstract class for any event time comming from an production unit
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractPUJobEvent implements Serializable {

    private static final long serialVersionUID = -1L;

    private final IProductionUnit productionUnit;
    private final HistoryEntry historyEntry;

    /**
     * Canonical constructor
     *
     * @param productionUnit the production unit associated with this event
     * @param historyEntry   the history object causing this event
     */
    AbstractPUJobEvent(IProductionUnit productionUnit, HistoryEntry historyEntry) {
        this.productionUnit = productionUnit;
        this.historyEntry = historyEntry;
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
