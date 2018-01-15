package org.cocome.cloud.logic.webservice;

import javax.ejb.EJBException;
import javax.enterprise.event.ObserverException;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.webservice.exception.UnhandledException;
import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.inventory.application.store.NoSuchProductException;
import org.cocome.tradingsystem.inventory.application.store.ProductOutOfStockException;
import org.cocome.tradingsystem.util.exception.BaseException;


/**
 * Represents an Action to be executed within the context of an active CashDeskSession.
 * Every subclass has to implement the {@code safeExecute} method.
 * Should an {@code IllegalCashDeskStateException} occur during execution,
 * this class will convert it to a WS-Fault.
 *
 * @param <T> the type returned when executing the action
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public abstract class AbstractCashDeskAction<T> implements IContextAction<T> {
    private static final Logger LOG = Logger.getLogger(AbstractCashDeskAction.class);

    /**
     * Create a new action.
     */
    protected AbstractCashDeskAction() {
    }

    /**
     * Invokes the {@code checkedExecute} method and catches any {@code IllegalCashDeskStateException}
     * that occur and converts it to an {@code IllegalCashDeskStateWSException}.
     *
     * @return the return value of {@code checkedExecute}
     */
    public T execute() throws IllegalCashDeskStateException,
            NoSuchProductException, ProductOutOfStockException,
            UnhandledException {
        T result;
        try {
            result = checkedExecute();
        } catch (BaseException | EJBException | ObserverException e) {
            Throwable cause = e;

            while (cause.getCause() != null) {
                cause = cause.getCause();
            }

            if (cause instanceof IllegalCashDeskStateException) {
                LOG.error("Got IllegalCashDeskStateException: " + cause);
                cause.printStackTrace();
                throw (IllegalCashDeskStateException) cause;
            }

            if (cause instanceof NoSuchProductException) {
                LOG.error("Got NoSuchProductException: " + cause);
                cause.printStackTrace();
                throw (NoSuchProductException) cause;
            }

            if (cause instanceof ProductOutOfStockException) {
                LOG.error("Got ProductOutOfStockException: " + cause);
                cause.printStackTrace();
                throw (ProductOutOfStockException) cause;
            }

            LOG.error("Got exception with unhandled cause: " + cause);
            cause.printStackTrace();
            UnhandledException unhandled = new UnhandledException();
            unhandled.initCause(cause);
            throw unhandled;
        }
        return result;
    }

    /**
     * The actual actions to be performed by this action.
     *
     * @return a user defined value
     */
    public abstract T checkedExecute() throws BaseException;

}
