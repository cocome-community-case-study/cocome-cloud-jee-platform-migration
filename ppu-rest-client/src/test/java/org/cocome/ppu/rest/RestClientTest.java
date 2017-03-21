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
		Assert.assertEquals("0th result element has valid name", "ACT_DriveFromBaseToRamp1", opts.get(0).getName());
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
	public void testGetCompleteHistory() {
		// When
		final String operationId = "_1_2_1_P2_O1";
		ppuDevice.startOperation(operationId);
		final List<HistoryEntry> hist = ppuDevice.getCompleteHistory();
		// Then
		Assert.assertNotNull("Result is not null", hist);
		System.out.println(hist.get(hist.size() - 1));
	}

	@Test
	public void testGetHistoryByExecutionId() {
		// When
		final String executionId = "d1e027a1-4c44-4d64-9eaf-72b17dc7701f";
		final List<HistoryEntry> hist = ppuDevice.getHistoryByExecutionId(executionId);
		// Then
		Assert.assertNotNull("Result is not null", hist);
	}

}
