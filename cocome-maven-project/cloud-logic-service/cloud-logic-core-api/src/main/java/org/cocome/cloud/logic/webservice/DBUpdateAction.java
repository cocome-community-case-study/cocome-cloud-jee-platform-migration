package org.cocome.cloud.logic.webservice;

import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;

@FunctionalInterface
public interface DBUpdateAction {

    /**
     * Executes an DB CUD operation (Create / Update / Delete)
     */
    void perform() throws UpdateException;
}
