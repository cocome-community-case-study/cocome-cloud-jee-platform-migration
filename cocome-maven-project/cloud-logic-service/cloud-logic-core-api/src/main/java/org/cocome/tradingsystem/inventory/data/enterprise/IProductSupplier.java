package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.data.INameable;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.Collection;

public interface IProductSupplier extends INameable {


    /**
     * @return A unique identifier for ProductSupplier objects
     */
    @Override
    long getId();

    /**
     * @param id A unique identifier for ProductSupplier objects
     */
    @Override
    void setId(long id);

    /**
     * @return The name of the ProductSupplier
     */
    @Override
    String getName();

    /**
     * @param name The name of the ProductSupplier
     */
    @Override
    void setName(String name);

    /**
     * @return The list of Products provided by the ProductSupplier
     * @throws NotInDatabaseException
     */
    Collection<IProduct> getProducts() throws NotInDatabaseException;

    /**
     * @param products The list of Products provided by the ProductSupplier
     */
    void setProducts(Collection<IProduct> products);

}