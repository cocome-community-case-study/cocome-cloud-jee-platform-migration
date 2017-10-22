package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.PUCOperationMeta;

/**
 * Test class is used for production unit doubles. In addition to the attributes
 *
 * @author Rudolf Biczok
 */
public interface PUOperationDoubleMeta extends PUCOperationMeta {

    /**
     * @return return the expected number of milliseconds this double operation is supposed to take
     */
    long getExecutionDurationInMillis();
}
