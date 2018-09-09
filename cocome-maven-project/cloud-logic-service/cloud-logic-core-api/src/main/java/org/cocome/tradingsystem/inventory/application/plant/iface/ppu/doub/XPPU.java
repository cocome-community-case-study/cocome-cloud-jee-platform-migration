package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.PUCOperationMeta;

/**
 * eXtended Pick & Place Unit
 *
 * @author Rudolf Biczok
 */
public enum XPPU implements PUCOperationMeta {
    ACT_ToBase("_1_2_1_P1_O2", 2),
    ACT_DriveFromBaseToRamp1("_1_2_1_P1_O3", 2),
    ACT_DriveFromBaseToRamp2("_1_2_1_P1_O4", 2),
    ACT_DriveFromBaseToEnd("_1_2_1_P1_O5", 2),
    Crane_ACT_Init("_1_2_1_P2_O1", 1),
    Crane_ACT_PickUpWP("_1_2_1_P2_O2", 5),
    Crane_ACT_PutDownWP("_1_2_1_P2_O3", 5),
    Crane_ACT_TurnToConveyor("_1_2_1_P2_O4", 5),
    Crane_ACT_TurnToStack("_1_2_1_P2_O5", 5),
    Crane_ACT_TurnToStamp("_1_2_1_P2_O6", 5),
    Stamp_ACT_Init("_1_2_1_P3_O1", 1),
    Stamp_ACT_Stamp("_1_2_1_P3_O2", 1),
    Stack_ACT_Init("_1_2_1_P4_O1", 1),
    Stack_ACT_ProvideWP("_1_2_1_P4_O2", 2);

    private String operationId;
    private long execTimeInMillis;

    XPPU(final String operationId, final long execTimeInMillis) {
        this.operationId = operationId;
        this.execTimeInMillis = execTimeInMillis;
    }

    @Override
    public String getOperationId() {
        return operationId;
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
