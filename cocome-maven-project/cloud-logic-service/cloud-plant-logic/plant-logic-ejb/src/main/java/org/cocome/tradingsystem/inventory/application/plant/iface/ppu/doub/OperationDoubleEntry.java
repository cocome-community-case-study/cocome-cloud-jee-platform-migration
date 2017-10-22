package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

import org.cocome.tradingsystem.inventory.application.plant.iface.OperationEntry;

/**
 * Specialized variant of {@link OperationEntry} containing the execution time as well
 *
 * @author Rudolf Biczok
 */
public class OperationDoubleEntry extends OperationEntry {
    private long execTimeInMillis;

    public long getExecutionDurationInMillis() {
        return execTimeInMillis;
    }

    public void setExecutionDurationInMillis(long execTimeInMillis) {
        this.execTimeInMillis = execTimeInMillis;
    }
}
