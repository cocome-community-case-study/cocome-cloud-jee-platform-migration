package org.cocome.tradingsystem.inventory.application.plant.iface.ppu;

/**
 * Describes the elemental parts of an operation provided by an production unit interface.
 *
 * @author Rudolf Biczok
 */
public interface PUCOperationMeta {

    /**
     * @return returns the operation id
     */
    String getOperationId();

    /**
     * @return returns a human readable name
     */
    String getName();

    /**
     * @return return the expected number of milliseconds this double operation is supposed to take
     */
    long getExecutionDurationInMillis();
}
