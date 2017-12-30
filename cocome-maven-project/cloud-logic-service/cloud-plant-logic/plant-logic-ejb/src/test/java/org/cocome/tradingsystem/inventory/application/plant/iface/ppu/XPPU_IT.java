package org.cocome.tradingsystem.inventory.application.plant.iface.ppu;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.doub.AbstractPUTestCase;
import org.cocome.tradingsystem.inventory.application.plant.iface.IPUInterface;
import org.junit.BeforeClass;
import org.junit.Ignore;

import java.util.Collections;

/**
 * Runs interface tests on the real device
 *
 * @author Rudolf Biczok, rudolf.biczok@student.kit.edu
 */
@Ignore
public class XPPU_IT extends AbstractPUTestCase {

    @BeforeClass
    public static void ensureManualMode() {
        ppuDevice = JAXRSClientFactory.create("http://129.187.88.30:4567/", IPUInterface.class,
                Collections.singletonList(new JacksonJsonProvider()));
        ppuDevice.switchToManualMode();
    }

}
