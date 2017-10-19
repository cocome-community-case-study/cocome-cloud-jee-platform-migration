package org.cocome.tradingsystem.inventory.application.plant.ppu;

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
}
