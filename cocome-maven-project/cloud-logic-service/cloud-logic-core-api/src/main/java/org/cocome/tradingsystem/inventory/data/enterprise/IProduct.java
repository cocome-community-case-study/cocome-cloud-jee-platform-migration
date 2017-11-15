package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IProduct extends INameable {

    /**
     * @return The barcode of the product
     */
    long getBarcode();

    /**
     * @param barcode The barcode of the product
     */
    void setBarcode(long barcode);

    /**
     * @return The purchase price of this product
     */
    double getPurchasePrice();

    /**
     * @param purchasePrice The purchase price of this product
     */
    void setPurchasePrice(double purchasePrice);

    /**
     * @return The ProductSupplier of this product
     * @throws NotInDatabaseException if the nested data object has no equivalence in the database
     */
    IProductSupplier getSupplier() throws NotInDatabaseException;

    /**
     * @param supplier The ProductSupplier of this product
     */
    void setSupplier(IProductSupplier supplier);
}