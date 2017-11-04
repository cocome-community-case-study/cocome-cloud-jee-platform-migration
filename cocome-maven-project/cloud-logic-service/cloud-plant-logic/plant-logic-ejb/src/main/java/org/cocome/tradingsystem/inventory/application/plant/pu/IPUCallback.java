package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

/**
 * The callback interface for receiving events.
 *
 * @author Rudolf Biczok
 */
public interface IPUCallback {
    /**
     * Called when a job has started
     *
     * @param historyEntry the history entry generated from the interface
     */
    void onStart(final IProductionUnit unit, final PUJob job, final HistoryEntry historyEntry);

    /**
     * Called every time a single operation within a job has finished.
     *
     * @param historyEntry the history entry generated from the interface
     */
    void onProgress(final IProductionUnit unit, final PUJob job, final HistoryEntry historyEntry);

    /**
     * Called when plant operation job has finished, i.e., when the last operation
     * has finished.
     *
     * @param historyEntry the history entry generated from the interface
     */
    void onFinish(final IProductionUnit unit, final PUJob job, final HistoryEntry historyEntry);
}
