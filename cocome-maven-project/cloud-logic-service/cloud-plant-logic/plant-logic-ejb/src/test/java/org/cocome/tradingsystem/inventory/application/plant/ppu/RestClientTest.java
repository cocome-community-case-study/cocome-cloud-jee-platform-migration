package org.cocome.tradingsystem.inventory.application.plant.ppu;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.HistoryAction;
import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.HistoryEntry;
import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.IPickAndPlaceUnit;
import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.OperationEntry;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.Collections;
import java.util.List;

/**
 * Integration test for {@link IPickAndPlaceUnit}
 *
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
@Ignore
public class RestClientTest {

    // Given
    private static IPickAndPlaceUnit ppuDevice = JAXRSClientFactory.create("http://129.187.88.30:4567/", IPickAndPlaceUnit.class,
            Collections.singletonList(new JacksonJsonProvider()));

    @BeforeClass
    public static void ensureManualMode() {
        ppuDevice.switchToManualMode();
    }

    // @Test
    public void testGetInstance() {
        // When
        final Document instance = ppuDevice.getInstance();
        // Then
        Assert.assertNotNull("Result is not null", instance);
        Assert.assertEquals("Result contains ISA88 xml data", "isa88:Enterprise",
                instance.getDocumentElement().getNodeName());
    }

    // @Test
    public void testGetOperations() {
        // When
        final List<OperationEntry> opts = ppuDevice.getOperations();
        // Then
        Assert.assertNotNull("Result is not null", opts);
        Assert.assertEquals("Result has N elements", 16, opts.size());
        Assert.assertEquals("0th result element has valid name", "ACT_DriveFromBaseToRamp1", opts.get(0).getName());
    }

    // @Test
    public void testGetOperation() {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final OperationEntry opt = ppuDevice.getOperation(operationId);
        // Then
        Assert.assertNotNull("Result is not null", opt);
        Assert.assertEquals("Result has valid id", operationId, opt.getOperationId());
        Assert.assertEquals("Result has valid name", "Crane_ACT_Init", opt.getName());
    }

    // @Test
    public void testStartOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        Thread.sleep(1000);
        // Then
        Assert.assertNotNull("Result is not null", ret);
        Assert.assertEquals("Result should have START state", HistoryAction.START, ret.getAction());
    }

    // @Test
    public void testGetCompleteHistory() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        ppuDevice.startOperation(operationId);
        final List<HistoryEntry> hist = ppuDevice.getCompleteHistory();
        Thread.sleep(1000);
        // Then
        Assert.assertNotNull("Result is not null", hist);
        Assert.assertEquals("Last entry should contain last issued operation", operationId,
                hist.get(hist.size() - 1).getOperationId());
    }

    // @Test
    public void testGetHistoryByExecutionId() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry entry = ppuDevice.startOperation(operationId);
        Thread.sleep(1000);
        final List<HistoryEntry> hist = ppuDevice.getHistoryByExecutionId(entry.getExecutionId());
        // Then
        Assert.assertNotNull("Result is not null", hist);
        Assert.assertEquals("At least first entry should contain same execution id", operationId,
                hist.get(0).getOperationId());
    }

    // @Test
    public void testGetHistoryByModuleName() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        ppuDevice.startOperation(operationId);
        Thread.sleep(1000);
        final List<HistoryEntry> hist = ppuDevice.getHistoryByModuleName("Crane");
        // Then
        Assert.assertNotNull("Result is not null", hist);
        for (int i = 0; i < hist.size(); i++) {
            Assert.assertTrue(i + "th entry should contain same module",
                    hist.get(i).getResolvedOperationPath().contains("Crane"));
        }
    }

    // @Test
    public void testGetHistoryByOperationId() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        ppuDevice.startOperation(operationId);
        Thread.sleep(1000);
        final List<HistoryEntry> hist = ppuDevice.getHistoryByOperationId(operationId);
        // Then
        Assert.assertNotNull("Result is not null", hist);
        Assert.assertEquals("At least first entry should contain same execution id", operationId,
                hist.get(0).getOperationId());
    }

    // @Test
    public void testGetHistoryByTimeStamp() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry entry = ppuDevice.startOperation(operationId);
        Thread.sleep(1000);
        final List<HistoryEntry> hist = ppuDevice.getHistoryByTimeStemp(entry.getTimestamp());
        // Then
        Assert.assertNotNull("Result is not null", hist);
        Assert.assertEquals("At least first entry should contain same execution id", entry.getTimestamp(),
                hist.get(0).getTimestamp());
    }

    // @Test
    public void testAbortOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O1";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        final HistoryEntry abortRet = ppuDevice.abortOperation(ret.getExecutionId());
        Thread.sleep(1000);
        // Then
        Assert.assertNotNull("Abort result is not null", abortRet);
        Assert.assertEquals("Abort result should have ABORT state", HistoryAction.ABORT, abortRet.getAction());
    }

    // @Test
    public void testHoldOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O4";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        final HistoryEntry haltRet = ppuDevice.holdOperation(ret.getExecutionId());
        final HistoryEntry abortRet = ppuDevice.abortOperation(ret.getExecutionId());
        Thread.sleep(1000);
        // Then
        Assert.assertNotNull("Halt result is not null", haltRet);
        Assert.assertEquals("Halt result should have HOLD state", HistoryAction.HOLD, haltRet.getAction());
        Assert.assertNotNull("Abort result is not null", abortRet);
        Assert.assertEquals("Abort result should have ABORT state", HistoryAction.ABORT, abortRet.getAction());
    }

    // @Test
    public void testHaltOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O4";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        final HistoryEntry haltRet = ppuDevice.holdOperation(ret.getExecutionId());
        final HistoryEntry abortRet = ppuDevice.abortOperation(ret.getExecutionId());
        Thread.sleep(1000);
        // Then
        Assert.assertNotNull("Hold result is not null", haltRet);
        Assert.assertEquals("Hold result should have HOLD state", HistoryAction.HOLD, haltRet.getAction());
        Assert.assertNotNull("Abort result is not null", abortRet);
        Assert.assertEquals("Abort result should have ABORT state", HistoryAction.ABORT, abortRet.getAction());
    }

    // @Test
    public void testRestartOperation() throws InterruptedException {
        // When
        final String operationId = "_1_2_1_P2_O4";
        final HistoryEntry ret = ppuDevice.startOperation(operationId);
        final HistoryEntry haltRet = ppuDevice.holdOperation(ret.getExecutionId());
        final HistoryEntry restartRet = ppuDevice.restartOperation(ret.getExecutionId());
        ppuDevice.abortOperation(ret.getExecutionId());
        Thread.sleep(1000);
        // Then
        Assert.assertNotNull("Restart result is not null", restartRet);
        Assert.assertEquals("Restart result should have RESTART state", HistoryAction.RESTART, restartRet.getAction());
        Assert.assertNotNull("Hold result is not null", haltRet);
        Assert.assertEquals("Hold result should have HOLD state", HistoryAction.HOLD, haltRet.getAction());
    }

    // @Test
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
        //ppuDevice.switchToManualMode();
    }

    //@Test
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
            Thread.sleep(1000 * 5);
            System.err.println(ppuDevice.getHistoryByExecutionId(batchRet.getExecutionId()));
            count++;
        }
        ppuDevice.switchToManualMode();
    }

    // @Test
    public void complexTest2() throws InterruptedException {
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
        System.err.println(ppuDevice.startOperationsInBatch(String.join(";", opts)));
        System.err.println(ppuDevice.startOperationsInBatch(String.join(";", opts)));
        //System.err.println(ppuDevice.startOperationsInBatch(String.join(";", opts)));
        //System.err.println(ppuDevice.startOperationsInBatch(String.join(";", opts)));

        //ppuDevice.switchToManualMode();
    }
}
