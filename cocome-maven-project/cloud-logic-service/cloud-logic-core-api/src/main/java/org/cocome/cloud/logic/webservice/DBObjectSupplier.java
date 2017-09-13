package org.cocome.cloud.logic.webservice;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

@FunctionalInterface
public interface DBObjectSupplier<T> {

    /**
     * @return an entity from the persistence layer
     * @throws NotInDatabaseException if the entity could not be found in the persistence layer
     */
    T get() throws NotInDatabaseException;
}
