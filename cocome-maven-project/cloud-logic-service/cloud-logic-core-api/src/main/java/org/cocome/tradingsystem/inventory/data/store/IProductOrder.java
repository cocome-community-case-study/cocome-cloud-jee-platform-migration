package org.cocome.tradingsystem.inventory.data.store;

import org.cocome.tradingsystem.inventory.data.IIdentifiable;

import java.util.Collection;
import java.util.Date;

public interface IProductOrder extends IIdentifiable {

    /**
     * @return A unique identifier for ProductOrder objects
     */
    @Override
    long getId();

    /**
     * @param id A unique identifier for ProductOrder objects
     */
    @Override
    void setId(long id);

    /**
     * @return A list of OrderEntry objects (pairs of Product-Amount-pairs)
     */
    Collection<IOrderEntry> getOrderEntries();

    /**
     * @param orderEntries A list of OrderEntry objects (pairs of Product-Amount-pairs)
     */
    void setOrderEntries(Collection<IOrderEntry> orderEntries);

    /**
     * @return The date of ordering.
     */
    Date getOrderingDate();

    /**
     * @param orderingDate the date of ordering
     */
    void setOrderingDate(Date orderingDate);

    /**
     * The delivery date is used for computing the mean time to delivery
     *
     * @return The date of order fulfillment.
     */
    Date getDeliveryDate();

    /**
     * The delivery date is used for computing the mean time to delivery
     *
     * @param deliveryDate the date of order fulfillment
     */
    void setDeliveryDate(Date deliveryDate);

    /**
     * @return The store where the order is placed.
     */
    IStore getStore();

    /**
     * @param store the store where the order is placed
     */
    void setStore(IStore store);

    String getStoreName();

    void setStoreName(String storeName);

    String getStoreLocation();

    void setStoreLocation(String storeLocation);

}