package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IProduct extends IAbstractProduct {

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