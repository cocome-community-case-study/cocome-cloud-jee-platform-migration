package plantservice.puc;

/**
 * eXtended Pick & Place Unit
 *
 * @author Rudolf Biczok
 */
public enum XPPU implements TestPUCOperation {
    ACT_DriveFromBaseToRamp1("_1_2_1_P1_O3"),
    ACT_DriveFromBaseToRamp2("_1_2_1_P1_O5"),
    ACT_DriveFromBaseToEnd("_1_2_1_P1_O6"),
    ACT_PushToRamp1("_1_2_1_P1_O7"),
    ACT_PushToRamp2("_1_2_1_P1_O8"),
    Crane_ACT_Init("_1_2_1_P2_O1"),
    Crane_ACT_PickUpWP("_1_2_1_P2_O2"),
    Crane_ACT_PutDownWP("_1_2_1_P2_O3"),
    Crane_ACT_TurnToConveyor("_1_2_1_P2_O4"),
    Crane_ACT_TurnToStack("_1_2_1_P2_O5"),
    Crane_ACT_TurnToStamp("_1_2_1_P2_O6"),
    Stamp_ACT_Init("_1_2_1_P3_O1"),
    Stamp_ACT_Stamp("_1_2_1_P3_O2"),
    Stack_ACT_Init("_1_2_1_P4_O1"),
    Stack_ACT_ProvideWP("_1_2_1_P4_O2");

    private String operationId;

    XPPU(final String operationId) {
        this.operationId = operationId;
    }

    @Override
    public String getOperationId() {
        return operationId;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
