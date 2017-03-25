package org.cocome.ppu.rest;

import java.util.Collections;
import java.util.List;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Integration test for {@link IDevice}
 * 
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
public class RestClientTest {

	// Given
	public static IDevice ppuDevice = JAXRSClientFactory.create("http://129.187.88.30:4567/", IDevice.class,
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

//	@Test
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

	@Test
	public void testHaltOperation() throws InterruptedException {
		// When
		final String operationId = "_1_2_1_P2_O1";
		final HistoryEntry ret = ppuDevice.startOperation(operationId);
		final HistoryEntry haltRet = ppuDevice.haltOperation(ret.getExecutionId());
		final HistoryEntry abortRet = ppuDevice.abortOperation(ret.getExecutionId());
		Thread.sleep(1000);
		// Then
		Assert.assertNotNull("Halt result is not null", haltRet);
		Assert.assertEquals("Halt result should have HALT state", HistoryAction.ABORT, haltRet.getAction());
		Assert.assertNotNull("Abort result is not null", abortRet);
		Assert.assertEquals("Abort result should have ABORT state", HistoryAction.ABORT, abortRet.getAction());
	}
}
