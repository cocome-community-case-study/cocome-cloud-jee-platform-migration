package org.cocome.tradingsystem.inventory.application.plant.ppu.iface;

/**
 * Represents an entry in the history list.
 * 
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
public class HistoryEntry {
	private String operationId;
	private String executionId;
	private String resolvedOperationPath;
	private String timestamp;
	private HistoryAction action;
	private int resultCode;

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getResolvedOperationPath() {
		return resolvedOperationPath;
	}

	public void setResolvedOperationPath(String resolvedOperationPath) {
		this.resolvedOperationPath = resolvedOperationPath;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public HistoryAction getAction() {
		return action;
	}

	public void setAction(HistoryAction action) {
		this.action = action;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
}
