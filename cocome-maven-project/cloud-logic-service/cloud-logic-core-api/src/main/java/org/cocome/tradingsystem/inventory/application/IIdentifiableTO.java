package org.cocome.tradingsystem.inventory.application;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Marker interface for entity classes that have an id.
 * @author Rudolf Biczok
 */
@XmlTransient
public interface IIdentifiableTO extends Serializable {
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
