package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

import org.junit.BeforeClass;

/**
 * Runs interface tests on the double
 *
 * @author Rudolf Biczok
 */
public class XPPUDoubleTest extends AbstractPUTestCase {

    @BeforeClass
    public static void initDouble() {
        ppuDevice = new XPPUDouble(100);
    }

}