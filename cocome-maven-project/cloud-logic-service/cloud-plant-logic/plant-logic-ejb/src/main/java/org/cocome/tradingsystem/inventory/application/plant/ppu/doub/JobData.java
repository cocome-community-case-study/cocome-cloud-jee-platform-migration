package org.cocome.tradingsystem.inventory.application.plant.ppu.doub;

/**
 * An entry for the working queue used by {@link PUDouble}.
 *
 * @author Rudolf Biczok
 */
class JobData {
    private String commandString;
    private boolean batchMode;
    private String executionId;

    String getCommandString() {
        return commandString;
    }

    void setCommandString(String commandString) {
        this.commandString = commandString;
    }

    String getExecutionId() {
        return executionId;
    }

    void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
