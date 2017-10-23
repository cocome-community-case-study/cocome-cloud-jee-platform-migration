package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryAction;
import org.cocome.tradingsystem.inventory.application.plant.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.cocome.tradingsystem.inventory.application.plant.iface.OperationEntry;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class for interface tests
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractPUTestCase {

    // Given
    protected static IPUInterface ppuDevice;

    @Test
    public void testGetInstance() {
        // When
        final Document instance = ppuDevice.getInstance();
        // Then
        Assert.assertNotNull("Result is not null", instance);
        Assert.assertEquals("Result contains ISA88 xml data", "isa88:Enterprise",
                instance.getDocumentElement().getNodeName());
    }

    @Test
    public void testGetOperations() {
        // When
        final List<OperationEntry> opts = ppuDevice.getOperations();
        // Then
        Assert.assertNotNull("Result is not null", opts);
        Assert.assertEquals("Result has N elements", 16, opts.size());
        Assert.assertTrue("0th result element has valid name", opts.get(0).getName().contains("ACT"));
    }

    @Test
    public void testGetOperation() {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final OperationEntry opt = ppuDevice.getOperation(operationId);
        // Then
        Assert.assertNotNull("Result is not null", opt);
        Assert.assertEquals("Result has valid id", operationId, opt.getOperationId());
        Assert.assertEquals("Result has valid name", "Crane_ACT_Init", opt.getName());
    }

    @Test
    public void testStartOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        // Then
        Assert.assertNotNull("Result is not null", ret);
        Assert.assertEquals("Result should have START state", HistoryAction.START, ret.getAction());

        waitForFinish(ret);
    }

    @Test
    public void testGetCompleteHistory() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry entry = ppuDevice.startOperation(operationId);
        final List<HistoryEntry> hist = ppuDevice.getCompleteHistory();
        // Then
        Assert.assertNotNull("Result is not null", hist);
        Assert.assertEquals("Last entry should contain last issued operation", operationId,
                hist.get(hist.size() - 1).getOperationId());

        waitForFinish(entry);
    }

    @Test
    public void testGetHistoryByExecutionId() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry entry = ppuDevice.startOperation(operationId);
        final List<HistoryEntry> hist = ppuDevice.getHistoryByExecutionId(entry.getExecutionId());
        // Then
        Assert.assertNotNull("Result is not null", hist);
        Assert.assertEquals("At least first entry should contain same execution id", operationId,
                hist.get(0).getOperationId());

        waitForFinish(entry);
    }

    @Test
    public void testGetHistoryByModuleName() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry entry = ppuDevice.startOperation(operationId);
        final List<HistoryEntry> hist = ppuDevice.getHistoryByModuleName("Crane");
        // Then
        Assert.assertNotNull("Result is not null", hist);
        for (int i = 0; i < hist.size(); i++) {
            Assert.assertTrue(i + "th entry should contain same module",
                    hist.get(i).getResolvedOperationPath().contains("Crane"));
        }

        waitForFinish(entry);
    }

    @Test
    public void testGetHistoryByOperationId() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry entry = ppuDevice.startOperation(operationId);
        final List<HistoryEntry> hist = ppuDevice.getHistoryByOperationId(operationId);
        // Then
        Assert.assertNotNull("Result is not null", hist);
        Assert.assertEquals("At least first entry should contain same execution id", operationId,
                hist.get(0).getOperationId());

        waitForFinish(entry);
    }

    @Test
    public void testGetHistoryByTimeStamp() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry entry = ppuDevice.startOperation(operationId);
        final List<HistoryEntry> hist = ppuDevice.getHistoryByTimeStemp(entry.getTimestamp());
        // Then
        Assert.assertNotNull("Result is not null", hist);
        Assert.assertEquals("At least first entry should contain same execution id", entry.getTimestamp(),
                hist.get(0).getTimestamp());

        waitForFinish(entry);
    }

    @Test
    public void testAbortOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        final HistoryEntry abortRet = ppuDevice.abortOperation(ret.getExecutionId());
        // Then
        Assert.assertNotNull("Abort result is not null", abortRet);
        Assert.assertEquals("Abort result should have ABORT state", HistoryAction.ABORT, abortRet.getAction());
    }

    @Test
    public void testHoldOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O4";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        final HistoryEntry haltRet = ppuDevice.holdOperation(ret.getExecutionId());
        final HistoryEntry abortRet = ppuDevice.abortOperation(ret.getExecutionId());
        // Then
        Assert.assertNotNull("Halt result is not null", haltRet);
        Assert.assertEquals("Halt result should have HOLD state", HistoryAction.HOLD, haltRet.getAction());
        Assert.assertNotNull("Abort result is not null", abortRet);
        Assert.assertEquals("Abort result should have ABORT state", HistoryAction.ABORT, abortRet.getAction());
    }

    @Test
    public void testHaltOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O4";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        final HistoryEntry haltRet = ppuDevice.holdOperation(ret.getExecutionId());
        final HistoryEntry abortRet = ppuDevice.abortOperation(ret.getExecutionId());
        // Then
        Assert.assertNotNull("Hold result is not null", haltRet);
        Assert.assertEquals("Hold result should have HOLD state", HistoryAction.HOLD, haltRet.getAction());
        Assert.assertNotNull("Abort result is not null", abortRet);
        Assert.assertEquals("Abort result should have ABORT state", HistoryAction.ABORT, abortRet.getAction());
    }

    @Test
    public void testRestartOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O4";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        final HistoryEntry haltRet = ppuDevice.holdOperation(ret.getExecutionId());
        final HistoryEntry restartRet = ppuDevice.restartOperation(ret.getExecutionId());
        // Then
        Assert.assertNotNull("Restart result is not null", restartRet);
        Assert.assertEquals("Restart result should have RESTART state", HistoryAction.RESTART, restartRet.getAction());
        Assert.assertNotNull("Hold result is not null", haltRet);
        Assert.assertEquals("Hold result should have HOLD state", HistoryAction.HOLD, haltRet.getAction());
        waitForFinish(restartRet);
    }

    @Test
    public void testSwitchModes() throws InterruptedException {
        // When
        final HistoryEntry retAuto = ppuDevice.switchToAutomaticMode();
        final HistoryEntry retManu = ppuDevice.switchToManualMode();
        // Then
        Assert.assertNotNull("Automatic mode switch result is not null", retAuto);
        Assert.assertEquals("Automatic mode switch result should have SET_AUTOMATIC_MODE state",
                HistoryAction.SET_AUTOMATIC_MODE, retAuto.getAction());
        Assert.assertNotNull("Manual mode switch result is not null", retManu);
        Assert.assertEquals("Manual mode switch result should have SET_MANUAL_MODE state",
                HistoryAction.SET_MANUAL_MODE, retManu.getAction());
    }

    @Test
    public void testBatchExecution() throws InterruptedException {
        // When
        final String[] opts = {
                "_1_2_1_P2_O1",
                "_1_2_1_P4_O2",
                "_1_2_1_P2_O2",
                "_1_2_1_P2_O6",
                "_1_2_1_P2_O3",
                "_1_2_1_P3_O2",
                "_1_2_1_P2_O2",
                "_1_2_1_P2_O4",
                "_1_2_1_P2_O3",
                "_1_2_1_P1_O3",
                "_1_2_1_P1_O7"};
        ppuDevice.switchToAutomaticMode();
        final HistoryEntry batchRet = ppuDevice.startOperationsInBatch(String.join(";", opts));
        ppuDevice.abortOperation(batchRet.getExecutionId());
        final List<HistoryEntry> batchHistory = ppuDevice.getHistoryByExecutionId(batchRet.getExecutionId());
        // Then
        Assert.assertNotNull("Batch execution result is not null", batchRet);
        Assert.assertEquals("Batch execution result should have BATCH_START state", HistoryAction.BATCH_START,
                batchRet.getAction());

        Assert.assertEquals("Last entry of history of batch execution should have ABORT state",
                HistoryAction.ABORT, batchHistory.get(batchHistory.size() - 1).getAction());
        waitForFinish(batchRet);
        ppuDevice.switchToManualMode();
    }

    @Test
    public void complexTest1() throws InterruptedException {
        // When
        final String[] opts = {
                "_1_2_1_P2_O1",
                "_1_2_1_P4_O2",
                "_1_2_1_P2_O2",
                "_1_2_1_P2_O6",
                "_1_2_1_P2_O3",
                "_1_2_1_P3_O2",
                "_1_2_1_P2_O2",
                "_1_2_1_P2_O4",
                "_1_2_1_P2_O3",
                "_1_2_1_P1_O3",
                "_1_2_1_P1_O7"};
        ppuDevice.switchToAutomaticMode();
        final HistoryEntry batchRet = ppuDevice.startOperationsInBatch(String.join(";", opts));

        int count = 0;
        while (count < 10) {
            ppuDevice.getHistoryByExecutionId(batchRet.getExecutionId());
            count++;
        }
        waitForFinish(batchRet);
        ppuDevice.switchToManualMode();
    }

    private void waitForFinish(final HistoryEntry entry) throws InterruptedException {
        while (!containsTerminationElement(
                Instant.parse(entry.getTimestamp()),
                entry.getExecutionId(),
                entry.getAction(),
                ppuDevice.getCompleteHistory())) {
            Thread.sleep(500);
        }
    }


    private boolean containsTerminationElement(final Instant startTime,
                                               final String executionId,
                                               final HistoryAction startingAction,
                                               final List<HistoryEntry> history) {

        final List<HistoryEntry> filteredHistory = filterFirstUnrelatedElements(startTime, executionId, startingAction, history);

        return filteredHistory.stream().filter(e -> {
            //Emergency events
            if (e.getAction() == HistoryAction.RESET
                    || e.getAction() == HistoryAction.STOP) {
                return true;
            }
            if (startingAction == HistoryAction.BATCH_START) {
                return e.getOperationId() == null
                        && e.getExecutionId() == null
                        && e.getAction() == HistoryAction.BATCH_COMPLETE
                        || e.getExecutionId() != null
                        && e.getExecutionId().equals(executionId)
                        && (e.getAction() == HistoryAction.ABORT
                        || e.getAction() == HistoryAction.HOLD);
            }
            return e.getOperationId() != null
                    && e.getExecutionId() != null
                    && e.getExecutionId().equals(executionId)
                    && (e.getAction() == HistoryAction.ABORT
                    || e.getAction() == HistoryAction.HOLD
                    || e.getAction() == HistoryAction.COMPLETE);
        }).count() > 0;
    }

    private List<HistoryEntry> filterFirstUnrelatedElements(final Instant startTime,
                                                            final String executionId,
                                                            final HistoryAction startingAction,
                                                            final List<HistoryEntry> history) {

        for (int i = 0; i < history.size(); i++) {
            final HistoryEntry entry = history.get(i);
            final Instant timestemp = Instant.parse(entry.getTimestamp());
            if (!timestemp.isBefore(startTime)
                    && entry.getExecutionId() != null
                    && entry.getExecutionId().equals(executionId)
                    && (entry.getAction() == startingAction)) {
                return history.subList(i, history.size());
            }
        }
        return Collections.emptyList();
    }

    private void printHistory() {
        this.printHistory(ppuDevice.getCompleteHistory());
    }

    private void printHistory(final List<HistoryEntry> history) {
        System.out.println("===========");
        for (final HistoryEntry entry : history) {
            System.out.println("  Execution-ID:" + entry.getExecutionId());
            System.out.println("  Operation-ID:" + entry.getOperationId());
            System.out.println("  Time Stamp:" + entry.getTimestamp());
            System.out.println("  Action:" + entry.getAction());
            System.out.println();
        }
        System.out.println("===========");
    }
}