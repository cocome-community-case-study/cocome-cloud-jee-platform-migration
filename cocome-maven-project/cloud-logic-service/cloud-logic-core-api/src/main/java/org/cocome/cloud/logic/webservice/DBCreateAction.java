package org.cocome.cloud.logic.webservice;

import javax.ejb.CreateException;

@FunctionalInterface
public interface DBCreateAction {

    /**
     * Executes an DB CUD operation (Create / Update / Delete)
     */
    void perform() throws CreateException;
}
