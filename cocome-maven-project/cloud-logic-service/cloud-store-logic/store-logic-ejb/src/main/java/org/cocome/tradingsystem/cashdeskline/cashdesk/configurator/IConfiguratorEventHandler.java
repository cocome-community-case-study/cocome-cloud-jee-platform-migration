package org.cocome.tradingsystem.cashdeskline.cashdesk.configurator;

import org.cocome.tradingsystem.cashdeskline.events.CustomProductEnteredEvent;

/**
 * Event handler for {@link IConfiguratorModel}
 *
 * @author Rudolf Biczok
 */
public interface IConfiguratorEventHandler {

    /**
     * Used to fetch the parameters of a custom product specified by the customer
     *
     * @param event the event to be handled
     */
    void onEvent(final CustomProductEnteredEvent event);

}
