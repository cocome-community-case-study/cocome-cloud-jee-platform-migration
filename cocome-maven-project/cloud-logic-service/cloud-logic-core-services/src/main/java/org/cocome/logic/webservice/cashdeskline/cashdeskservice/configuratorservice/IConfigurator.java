package org.cocome.logic.webservice.cashdeskline.cashdeskservice.configuratorservice;

import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Set;

/**
 * Interface for the configurator
 *
 * @author Rudolf Biczok
 */
@WebService(targetNamespace = "http://configurator.cashdesk.cashdeskline.webservice.logic.cocome.org/")
public interface IConfigurator {
    @WebMethod
    Set<Class<?>> sendParameterValues(@XmlElement(required = true)
                                      @WebParam(name = "cashDeskName")
                                              String cashDeskName,
                                      @XmlElement(required = true)
                                      @WebParam(name = "storeID")
                                              long storeID,
                                      @XmlElement(required = true)
                                      @WebParam(name = "parameterValues") final List<ParameterValueTO> parameterValues)
            throws IllegalCashDeskStateException, NoSuchProductException,
            UnhandledException, ProductOutOfStockException;
}
