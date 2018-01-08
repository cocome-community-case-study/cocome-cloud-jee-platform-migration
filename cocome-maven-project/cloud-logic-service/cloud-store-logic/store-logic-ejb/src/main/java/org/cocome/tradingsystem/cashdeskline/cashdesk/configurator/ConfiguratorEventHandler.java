package org.cocome.tradingsystem.cashdeskline.cashdesk.configurator;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.events.CustomProductEnteredEvent;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;

import javax.inject.Inject;

@CashDeskSessionScoped
public class ConfiguratorEventHandler implements IConfiguratorEventHandler {

    private static final Logger LOG = Logger.getLogger(ConfiguratorEventHandler.class);

    @Inject
    private IConfiguratorModel configuratorModel;

    @Override
    public void onEvent(final CustomProductEnteredEvent event) {
        try {
            configuratorModel.setParameters(event.getCustomProduct().getRecipe().getParameters());
        } catch (NotInDatabaseException e) {
            LOG.error("Error while loading recipe for product: " + event.getCustomProduct().getName(), e);
            e.printStackTrace();
        }
    }
}
