package org.cocome.cloud.logic.webservice;

import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;

@FunctionalInterface
public interface DBUpdateAction {

    /**
     * Executes an operation that updates or deletes an entity in the persistence layer
     */
    void perform() throws UpdateException;
}
