package org.cocome.cloud.logic.webservice.cashdeskline.cashdeskservice.configurator;

import org.cocome.cloud.logic.webservice.AbstractCashDeskAction;
import org.cocome.cloud.logic.webservice.NamedCashDeskService;
import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.logic.webservice.cashdeskline.cashdeskservice.configuratorservice.IConfigurator;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox.IllegalInputException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.configurator.IConfiguratorModel;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.mvc.IContentChangedListener;
import org.cocome.tradingsystem.util.scope.IContextRegistry;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import java.util.List;
import java.util.Set;

/**
 * Webservice interface for the configurator of a cash desk.
 *
 * @author Rudolf Biczok
 */
@WebService(serviceName = "IConfiguratorService",
        name = "IConfigurator",
        endpointInterface = "org.cocome.logic.webservice.cashdeskline.cashdeskservice.configuratorservice.IConfigurator",
        targetNamespace = "http://configurator.cashdesk.cashdeskline.webservice.logic.cocome.org/")
@Stateless
public class Configurator extends NamedCashDeskService implements IConfigurator {

    @Inject
    private IConfiguratorModel configuratorModel;

    @Inject
    private IContentChangedListener contentChanged;

    @Override
    public Set<Class<?>> sendParameterValues(String cashDeskName, long storeID,
                                             final List<ParameterValueTO> parameterValues)
            throws IllegalCashDeskStateException,
            NoSuchProductException, UnhandledException, ProductOutOfStockException {
        IContextRegistry context = getContextRegistry(cashDeskName, storeID);

        AbstractCashDeskAction<Set<Class<?>>> action = new AbstractCashDeskAction<Set<Class<?>>>() {
            @Override
            public Set<Class<?>> checkedExecute() {
                configuratorModel.sendParameterValues(parameterValues);
                return contentChanged.getChangedModels();
            }
        };

        try {
            return this.invokeInContext(context, action);
        } catch (IllegalInputException e) {
            throw new UnhandledException(e);
        }
    }

}
