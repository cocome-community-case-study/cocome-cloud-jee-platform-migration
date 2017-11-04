package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

/**
 * The callback interface for receiving events.
 *
 * @param <T> the type of payload data associated with an particular job execution
 * @author Rudolf Biczok
 */
public interface IPUCallback<T> {
    /**
     * Called when a job has started
     *
     * @param payload      the payload data associated with an particular job execution
     * @param historyEntry the history entry generated from the interface
     */
    void onStart(final IProductionUnit unit, final T payload, final HistoryEntry historyEntry);

    /**
     * Called every time a single operation within a job has finished.
     *
     * @param payload      the payload data associated with an particular job execution
     * @param historyEntry the history entry generated from the interface
     */
    void onProgress(final IProductionUnit unit, final T payload, final HistoryEntry historyEntry);

    /**
     * Called when plant operation job has finished, i.e., when the last operation
     * has finished.
     *
     * @param payload      the payload data associated with an particular job execution
     * @param historyEntry the history entry generated from the interface
     */
    void onFinish(final IProductionUnit unit, final T payload, final HistoryEntry historyEntry);
}
