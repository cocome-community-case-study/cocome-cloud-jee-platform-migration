package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryAction;
import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.XPPU;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.XPPUDouble;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnit;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnit;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class PUWorkerTest {

    private static class DataCollectionCallback implements IPUCallback {

        private Map<Long, List<HistoryEntry>> eventList = new HashMap<>();

        @Override
        public void onStart(IProductionUnit unit, HistoryEntry historyEntry) {
            pushEvent(unit, historyEntry);
        }

        @Override
        public void onProgress(IProductionUnit unit, HistoryEntry historyEntry) {
            pushEvent(unit, historyEntry);
        }

        @Override
        public void onFinish(IProductionUnit unit, HistoryEntry historyEntry) {
            pushEvent(unit, historyEntry);
        }

        private void pushEvent(IProductionUnit unit, HistoryEntry historyEntry) {
            if (!eventList.containsKey(unit.getId())) {
                eventList.put(unit.getId(), new LinkedList<>());
            }
            eventList.get(unit.getId()).add(historyEntry);
        }
    }

    private PUWorker worker;

    private DataCollectionCallback callback = new DataCollectionCallback();

    private static final int testId = 42;

    @Test
    public void submitJob() throws Exception {
        final IProductionUnit unit = new ProductionUnit();
        unit.setId(testId);
        final IPUInterface iface = new XPPUDouble(1000);
        worker = new PUWorker(unit, iface, callback);
        worker.submitJob(Arrays.asList(
                XPPU.Crane_ACT_Init.getOperationId(),
                XPPU.ACT_PushToRamp1.getOperationId()));
        worker.submitJob(Arrays.asList(
                XPPU.Crane_ACT_Init.getOperationId(),
                XPPU.ACT_PushToRamp1.getOperationId()));
        //Does also join to the worker thread
        //The worker thread stops after its working queue has been depleted
        this.worker.close();

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
                observedOperations,
                Arrays.asList(
                        XPPU.Crane_ACT_Init.getOperationId(),
                        XPPU.ACT_PushToRamp1.getOperationId(),
                        XPPU.Crane_ACT_Init.getOperationId(),
                        XPPU.ACT_PushToRamp1.getOperationId()));

        Assert.assertEquals(
                observedActions,
                Arrays.asList(
                        HistoryAction.BATCH_START,
                        HistoryAction.COMPLETE,
                        HistoryAction.COMPLETE,
                        HistoryAction.BATCH_COMPLETE,
                        HistoryAction.BATCH_START,
                        HistoryAction.COMPLETE,
                        HistoryAction.COMPLETE,
                        HistoryAction.BATCH_COMPLETE));
    }
}