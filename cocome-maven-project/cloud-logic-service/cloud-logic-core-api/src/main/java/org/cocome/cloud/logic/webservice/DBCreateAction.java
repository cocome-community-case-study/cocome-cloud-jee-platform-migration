package org.cocome.cloud.logic.webservice;

import javax.ejb.CreateException;

@FunctionalInterface
public interface DBCreateAction {

    /**
     * Executes an operation that inserts entities into the persistence layer
     */
    void perform() throws CreateException;
}
