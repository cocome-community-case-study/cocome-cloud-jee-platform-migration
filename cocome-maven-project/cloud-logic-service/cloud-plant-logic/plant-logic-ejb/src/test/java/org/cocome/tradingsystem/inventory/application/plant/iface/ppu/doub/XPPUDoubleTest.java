package org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub;

import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * Runs interface tests on the double
 *
 * @author Rudolf Biczok
 */
@Ignore
public class XPPUDoubleTest extends AbstractPUTestCase {

    @BeforeClass
    public static void initDouble() {
        ppuDevice = new XPPUDouble(100);
    }

}