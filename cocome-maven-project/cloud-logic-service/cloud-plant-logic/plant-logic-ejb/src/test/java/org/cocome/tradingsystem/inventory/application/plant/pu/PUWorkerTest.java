package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryAction;
import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.XPPU;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.XPPUDouble;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnit;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PUWorkerTest {

    private static class DataCollectionCallback implements IPUCallback<UUID> {

        private Map<Long, List<HistoryEntry>> eventList = new HashMap<>();

        @Override
        public void onStart(IProductionUnit unit, UUID id, HistoryEntry historyEntry) {
            pushEvent(unit, id, historyEntry);
        }

        @Override
        public void onProgress(IProductionUnit unit, UUID id, HistoryEntry historyEntry) {
            pushEvent(unit, id, historyEntry);
        }

        @Override
        public void onFinish(IProductionUnit unit, UUID id, HistoryEntry historyEntry) {
            pushEvent(unit, id, historyEntry);
        }

        private void pushEvent(IProductionUnit unit, UUID id, HistoryEntry historyEntry) {
            if (!eventList.containsKey(unit.getId())) {
                eventList.put(unit.getId(), new LinkedList<>());
            }
            eventList.get(unit.getId()).add(historyEntry);
        }
    }

    private DataCollectionCallback callback = new DataCollectionCallback();

    private static final int testId = 42;

    @Test
    public void submitJob() throws Exception {
        final IProductionUnit unit = new ProductionUnit();
        unit.setId(testId);
        final IPUInterface iface = new XPPUDouble(1000);
        final PUWorker<UUID> worker = new PUWorker<>(unit, iface, callback);
        worker.submitJob(UUID.randomUUID(), Arrays.asList(
                XPPU.Crane_ACT_Init.getOperationId()
        ));
        worker.submitJob(UUID.randomUUID(), Arrays.asList(
                XPPU.Crane_ACT_Init.getOperationId()
        ));
        Assert.assertTrue(worker.getWorkLoad() >= 2 && worker.getWorkLoad() <= 4);
        //Does also join to the worker thread
        //The worker thread stops after its working queue has been depleted
        worker.close();
        worker.awaitTermination(20, TimeUnit.SECONDS);

        List<String> observedOperations = callback.eventList
                .get(unit.getId())
                .stream()
                .map(HistoryEntry::getOperationId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<HistoryAction> observedActions = callback.eventList
                .get(unit.getId())
                .stream()
                .map(HistoryEntry::getAction)
                .collect(Collectors.toList());

        Assert.assertEquals(
                Arrays.asList(
                        XPPU.Crane_ACT_Init.getOperationId(),
                        XPPU.Crane_ACT_Init.getOperationId()),
                observedOperations
        );

        Assert.assertEquals(
                Arrays.asList(
                        HistoryAction.BATCH_START,
                        HistoryAction.COMPLETE,
                        HistoryAction.COMPLETE,
                        HistoryAction.BATCH_COMPLETE,
                        HistoryAction.BATCH_START,
                        HistoryAction.COMPLETE,
                        HistoryAction.COMPLETE,
                        HistoryAction.BATCH_COMPLETE),
                observedActions);
    }
}