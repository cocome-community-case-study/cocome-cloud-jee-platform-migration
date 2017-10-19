package org.cocome.tradingsystem.inventory.application.plant.ppu.doub;

/**
 * An entry for the working queue used by {@link ProductionUnitDouble}.
 *
 * @author Rudolf Biczok
 */
public class QueueEntry {
    private String commandString;
    private boolean batchMode;
    private String executionId;

    public String getCommandString() {
        return commandString;
    }

    public void setCommandString(String commandString) {
        this.commandString = commandString;
    }

    public boolean isBatchMode() {
        return batchMode;
    }

    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
