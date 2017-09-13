package org.cocome.tradingsystem.inventory.application;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Interface for TOs that have an id and a name.
 * @author Rudolf Biczok
 */
@XmlTransient
public interface INameableTO extends IIdentifiableTO {
    /**
     * @return The TO name
     */
    String getName();

    /**
     * @param name the TO name
     */
    void setName(final String name);
}
