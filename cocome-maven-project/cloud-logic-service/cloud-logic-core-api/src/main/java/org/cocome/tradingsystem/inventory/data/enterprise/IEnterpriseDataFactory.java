package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IEnterpriseDataFactory {

    /* Product *****/

    IProduct getNewProduct();

    IProduct convertToProduct(ProductTO productTO);

    ProductTO fillProductTO(IProduct product);

    /* Enterprise *****/

    ITradingEnterprise getNewTradingEnterprise();

    ITradingEnterprise convertToEnterprise(EnterpriseTO enterpriseTO);

    EnterpriseTO fillEnterpriseTO(ITradingEnterprise enterprise);

    /* Product Supplier *****/

    IProductSupplier getNewProductSupplier();

    IProductSupplier convertToSupplier(SupplierTO supplierTO);

    SupplierTO fillSupplierTO(IProductSupplier supplier);

    ProductWithSupplierTO fillProductWithSupplierTO(
            IProduct product) throws NotInDatabaseException;

    /* Custom Product *****/

    ICustomProduct getNewCustomProduct();

    ICustomProduct convertToCustomProduct(CustomProductTO customProductTO);

    CustomProductTO fillCustomProductTO(ICustomProduct product);

    /* Plant *****/

    IPlant getNewPlant();

    IPlant convertToPlant(PlantTO plantTO);

    PlantTO fillPlantTO(IPlant plant) throws NotInDatabaseException;
}