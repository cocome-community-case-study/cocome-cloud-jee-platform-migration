package org.cocome.tradingsystem.inventory.data.plant;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IPlant extends INameable {

    /**
     * @return A unique identifier for Store objects
     */
    @Override
    long getId();

    /**
     * @param id A unique identifier for Store objects
     */
    @Override
    void setId(long id);

    /**
     * Returns the name of the store.
     *
     * @return Store name.
     */
    @Override
    String getName();

    /**
     * @param name the name of the Store
     */
    @Override
    void setName(String name);

    /**
     * Returns the location of the store.
     *
     * @return Store location.
     */
    String getLocation();

    /**
     * Sets the location of the store.
     *
     * @param location store location
     */
    void setLocation(String location);

    /**
     * @return The enterprise which the Store belongs to
     */
    ITradingEnterprise getEnterprise() throws NotInDatabaseException;

    /**
     * @param enterprise The enterprise which the Store belongs to
     */
    void setEnterprise(ITradingEnterprise enterprise);

    long getEnterpriseId();

    void setEnterpriseId(long enterpriseId);

}