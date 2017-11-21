package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.PUCOperationMeta;

/**
 * Fill & Mix Unit
 *
 * @author Rudolf Biczok
 */
public enum FMU implements PUCOperationMeta {
    Silo0_ACT_Init(1),
    Silo0_ACT_Open(1),
    Silo0_ACT_Close(1),
    Silo1_ACT_Init(2),
    Silo1_ACT_Open(1),
    Silo1_ACT_Close(1),
    Silo2_ACT_Init(1),
    Silo2_ACT_Open(1),
    Silo2_ACT_Close(1),
    Mixer_ACT_Init(1),
    Mixer_ACT_Run1min(5);

    private long execTimeInMillis;

    FMU(final long execTimeInMillis) {
        this.execTimeInMillis = execTimeInMillis;
    }

    @Override
    public String getOperationId() {
        return String.format("_%d_OPR", this.ordinal());
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public long getExecutionDurationInMillis() {
        return this.execTimeInMillis;
    }

}
