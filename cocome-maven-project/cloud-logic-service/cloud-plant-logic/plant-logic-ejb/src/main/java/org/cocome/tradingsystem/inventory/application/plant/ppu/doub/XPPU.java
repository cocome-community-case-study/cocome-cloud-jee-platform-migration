package org.cocome.tradingsystem.inventory.application.plant.ppu.doub;

/**
 * eXtended Pick & Place Unit
 *
 * @author Rudolf Biczok
 */
public enum XPPU implements PUOperationDoubleMeta {
    ACT_DriveFromBaseToRamp1("_1_2_1_P1_O3", 1000),
    ACT_DriveFromBaseToRamp2("_1_2_1_P1_O5", 1000),
    ACT_DriveFromBaseToEnd("_1_2_1_P1_O6", 1000),
    ACT_PushToRamp1("_1_2_1_P1_O7", 1000),
    ACT_PushToRamp2("_1_2_1_P1_O8", 1000),
    Crane_ACT_Init("_1_2_1_P2_O1", 100),
    Crane_ACT_PickUpWP("_1_2_1_P2_O2", 1000),
    Crane_ACT_PutDownWP("_1_2_1_P2_O3", 1000),
    Crane_ACT_TurnToConveyor("_1_2_1_P2_O4", 1000),
    Crane_ACT_TurnToStack("_1_2_1_P2_O5", 1000),
    Crane_ACT_TurnToStamp("_1_2_1_P2_O6", 1000),
    Stamp_ACT_Init("_1_2_1_P3_O1", 100),
    Stamp_ACT_Stamp("_1_2_1_P3_O2", 1000),
    Stack_ACT_Init("_1_2_1_P4_O1", 100),
    //TODO Whats its name????!!!!!
    Stack_ACT_Unknown_Name("_UNKNOWN", 100),
    Stack_ACT_ProvideWP("_1_2_1_P4_O2", 1000);

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
