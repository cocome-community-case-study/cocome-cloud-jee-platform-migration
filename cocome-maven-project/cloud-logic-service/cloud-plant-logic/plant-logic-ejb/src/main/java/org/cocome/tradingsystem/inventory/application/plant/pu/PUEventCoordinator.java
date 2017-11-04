package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobFinishedEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobProgressEvent;
import org.cocome.tradingsystem.inventory.application.plant.pu.events.PUJobStartedEvent;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;

import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.UUID;

/**
 * This class is used to generate CDI-based events out of incoming production unit callback calls
 *
 * @author Rudolf Biczok
 */
@Singleton
public class PUEventCoordinator implements IPUCallback<UUID> {

    @Inject
    private Event<PUJobStartedEvent> jobStartedEvent;

    @Inject
    private Event<PUJobProgressEvent> jobProgressEvent;

    @Inject
    private Event<PUJobFinishedEvent> jobFinishedEvent;

    @Override
    public void onStart(IProductionUnit unit, UUID id, HistoryEntry historyEntry) {
        jobStartedEvent.fire(new PUJobStartedEvent(unit, id, historyEntry));
    }

    @Override
    public void onProgress(IProductionUnit unit, UUID id, HistoryEntry historyEntry) {
        jobProgressEvent.fire(new PUJobProgressEvent(unit, id, historyEntry));
    }

    @Override
    public void onFinish(IProductionUnit unit, UUID id, HistoryEntry historyEntry) {
        jobFinishedEvent.fire(new PUJobFinishedEvent(unit, id, historyEntry));
    }
}
