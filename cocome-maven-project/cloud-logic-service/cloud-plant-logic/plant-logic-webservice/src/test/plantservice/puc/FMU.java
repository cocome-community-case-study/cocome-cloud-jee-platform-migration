package plantservice.puc;

import org.cocome.tradingsystem.inventory.application.plant.ppu.PUCOperationMeta;

/**
 * Imaginary "Fill & Mix Unit".
 *
 * @author Rudolf Biczok
 */
public enum FMU implements PUCOperationMeta {
    Gate1_Init,
    Gate1_Open,
    Gate1_Close,
    Gate2_Init,
    Gate2_Open,
    Gate2_Close,
    Gate3_Init,
    Gate3_Open,
    Gate3_Close,
    MainGate_Init,
    MainGate_Release,
    Mixer_Init,
    Mixer_Start,
    Mixer_Stop;

    @Override
    public String getOperationId() {
        return String.format("_OPERATION_%d", this.ordinal());
    }

    @Override
    public String getName() {
        return this.name();
    }
}
