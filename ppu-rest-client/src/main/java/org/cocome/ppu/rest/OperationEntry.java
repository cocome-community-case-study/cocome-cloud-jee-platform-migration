package org.cocome.ppu.rest;

/**
 * Represents a resolved operation that is available on the PLC.
 * 
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
public class OperationEntry {
	private String operationId;
	private String path;
	private String name;
	private String resolvedPath;

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResolvedPath() {
		return resolvedPath;
	}

	public void setResolvedPath(String resolvedPath) {
		this.resolvedPath = resolvedPath;
	}
}
