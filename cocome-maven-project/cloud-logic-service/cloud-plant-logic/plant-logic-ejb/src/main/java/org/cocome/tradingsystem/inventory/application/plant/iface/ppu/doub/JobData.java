package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

/**
 * An entry for the working queue used by {@link PUDouble}.
 *
 * @author Rudolf Biczok
 */
class JobData {
    private String operationId;
    private String executionId;
    private long progressInMillis;

    String getOperationId() {
        return operationId;
    }

    void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    String getExecutionId() {
        return executionId;
    }

    void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    long getProgressInMillis() {
        return progressInMillis;
    }

    void setProgressInMillis(long progressInMillis) {
        this.progressInMillis = progressInMillis;
    }

}
