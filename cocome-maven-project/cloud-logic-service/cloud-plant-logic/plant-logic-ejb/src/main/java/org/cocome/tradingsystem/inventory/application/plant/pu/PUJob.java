package org.cocome.tradingsystem.inventory.application.plant.pu;

import java.util.List;
import java.util.UUID;

/**
 * Represents the data used to handle o job for a specific production unit
 *
 * @author Rudolf Biczok
 */
public class PUJob {
    private final List<String> operations;

    public UUID getJobId() {
        return jobId;
    }

    private final UUID jobId;

    /**
     * Canonical constructor
     *
     * @param operations the list of operations to execute for this job
     */
    PUJob(final UUID jobId, List<String> operations) {
        this.jobId = jobId;
        this.operations = operations;
    }

    /**
     * @return the list of operations to execute for this job
     */
    public List<String> getOperations() {
        return operations;
    }
}
