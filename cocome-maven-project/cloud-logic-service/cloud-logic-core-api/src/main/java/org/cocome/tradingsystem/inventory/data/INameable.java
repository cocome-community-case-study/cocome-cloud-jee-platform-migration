package org.cocome.tradingsystem.inventory.data;

/**
 * Interface for data objects that have an id and a name.
 *
 * @author Rudolf Biczok
 */
public interface INameable extends IIdentifiable {
    /**
     * @return The data object name
     */
    String getName();

    /**
     * @param name the data object name
     */
    void setName(final String name);
}
