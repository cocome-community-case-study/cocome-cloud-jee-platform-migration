package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IEnterpriseDataFactory {

    IProduct getNewProduct();

    IProductSupplier getNewProductSupplier();

    ITradingEnterprise getNewTradingEnterprise();

    IProduct convertToProduct(ProductTO productTO);

    ITradingEnterprise convertToEnterprise(EnterpriseTO enterpriseTO);

    IProductSupplier convertToSupplier(SupplierTO supplierTO);

    SupplierTO fillSupplierTO(IProductSupplier supplier);

    EnterpriseTO fillEnterpriseTO(ITradingEnterprise enterprise);

    ProductTO fillProductTO(IProduct product);

    ProductWithSupplierTO fillProductWithSupplierTO(
            IProduct product) throws NotInDatabaseException;

}