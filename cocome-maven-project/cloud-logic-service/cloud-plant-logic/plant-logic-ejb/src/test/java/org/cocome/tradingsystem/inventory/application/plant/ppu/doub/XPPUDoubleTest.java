package org.cocome.tradingsystem.inventory.application.plant.ppu.doub;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class XPPUDoubleTest extends AbstractPUTestCase {

    private static XPPUDouble ppuDouble = new XPPUDouble();

    @BeforeClass
    public static void ensureManualMode() {
        ppuDevice = ppuDouble;
    }

    @AfterClass
    public static void stopDummyInterface() throws InterruptedException {
        ppuDouble.close();
    }
}