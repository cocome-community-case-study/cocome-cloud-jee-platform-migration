package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IProduct extends INameable {

    /**
     * Gets identifier value
     *
     * @return The id.
     */
    @Override
    long getId();

    /**
     * Sets identifier.
     *
     * @param id Identifier value.
     */
    @Override
    void setId(long id);

    /**
     * @return The barcode of the product
     */
    long getBarcode();

    /**
     * @param barcode The barcode of the product
     */
    void setBarcode(long barcode);

    /**
     * @return The name of the product
     */
    @Override
    String getName();

    /**
     * @param name The name of the product
     */
    @Override
    void setName(String name);

    /**
     * @return The ProductSupplier of this product
     * @throws NotInDatabaseException
     */
    IProductSupplier getSupplier() throws NotInDatabaseException;

    /**
     * @param supplier The ProductSupplier of this product
     */
    void setSupplier(IProductSupplier supplier);

    /**
     * @return The purchase price of this product
     */
    double getPurchasePrice();

    /**
     * @param purchasePrice The purchase price of this product
     */
    void setPurchasePrice(double purchasePrice);

}