package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * Superinterface for other product classes
 *
 * @author Rudolf Biczok
 */
public interface IAbstractProduct extends INameable {

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

}