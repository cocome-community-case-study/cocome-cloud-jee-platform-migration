package org.cocome.tradingsystem.inventory.data;

/**
 * Interface for data objects that have an id.
 *
 * @author Rudolf Biczok
 */
public interface IIdentifiable {
    /**
     * Gets identifier value
     *
     * @return The id.
     */
    long getId();

    /**
     * @param id the identifier value
     */
    void setId(final long id);
}
