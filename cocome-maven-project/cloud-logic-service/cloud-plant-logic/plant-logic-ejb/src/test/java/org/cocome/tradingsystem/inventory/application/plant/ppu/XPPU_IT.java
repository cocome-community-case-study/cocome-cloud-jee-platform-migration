package org.cocome.tradingsystem.inventory.application.plant.ppu;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.cocome.tradingsystem.inventory.application.plant.ppu.doub.AbstractPUTestCase;
import org.cocome.tradingsystem.inventory.application.plant.ppu.iface.IPickAndPlaceUnit;
import org.junit.BeforeClass;
import org.junit.Ignore;

import java.util.Collections;

/**
 * Integration test for {@link IPickAndPlaceUnit}
 *
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
@Ignore
public class XPPU_IT extends AbstractPUTestCase {

    @BeforeClass
    public static void ensureManualMode() {
        ppuDevice = JAXRSClientFactory.create("http://129.187.88.30:4567/", IPickAndPlaceUnit.class,
                Collections.singletonList(new JacksonJsonProvider()));
        ppuDevice.switchToManualMode();
    }

}
