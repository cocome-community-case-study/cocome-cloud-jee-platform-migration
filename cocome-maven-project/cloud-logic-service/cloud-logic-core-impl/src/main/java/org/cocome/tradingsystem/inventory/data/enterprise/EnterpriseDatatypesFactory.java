package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;

@Dependent
class EnterpriseDatatypesFactory implements IEnterpriseDataFactory {

    @Inject
    private Provider<Product> productProvider;

    @Inject
    private Provider<ProductSupplier> productSupplierProvider;

    @Inject
    private Provider<TradingEnterprise> enterpriseProvider;

    @Override
    public IProduct getNewProduct() {
        return productProvider.get();
    }

    @Override
    public IProductSupplier getNewProductSupplier() {
        return productSupplierProvider.get();
    }

    @Override
    public ITradingEnterprise getNewTradingEnterprise() {
        return enterpriseProvider.get();
    }

    @Override
    public IProduct convertToProduct(ProductTO productTO) {
        IProduct product = getNewProduct();
        product.setBarcode(productTO.getBarcode());
        product.setId(productTO.getId());
        product.setName(productTO.getName());
        product.setPurchasePrice(productTO.getPurchasePrice());
        return product;
    }

    @Override
    public ProductTO fillProductTO(IProduct product) {
        ProductTO productTO = new ProductTO();
        productTO.setBarcode(product.getBarcode());
        productTO.setId(product.getId());
        productTO.setName(product.getName());
        productTO.setPurchasePrice(product.getPurchasePrice());
        return productTO;
    }

    @Override
    public ITradingEnterprise convertToEnterprise(EnterpriseTO enterpriseTO) {
        ITradingEnterprise enterprise = getNewTradingEnterprise();
        enterprise.setId(enterpriseTO.getId());
        enterprise.setName(enterpriseTO.getName());
        return enterprise;
    }

    @Override
    public EnterpriseTO fillEnterpriseTO(ITradingEnterprise enterprise) {
        EnterpriseTO enterpriseTO = new EnterpriseTO();
        enterpriseTO.setId(enterprise.getId());
        enterpriseTO.setName(enterprise.getName());
        return enterpriseTO;
    }

    @Override
    public IProductSupplier convertToSupplier(SupplierTO supplierTO) {
        IProductSupplier supplier = getNewProductSupplier();
        supplier.setId(supplierTO.getId());
        supplier.setName(supplierTO.getName());
        return supplier;
    }

    @Override
    public SupplierTO fillSupplierTO(IProductSupplier supplier) {
        SupplierTO supplierTO = new SupplierTO();
        if (supplier != null) {
            supplierTO.setId(supplier.getId());
            supplierTO.setName(supplier.getName());
        }
        return supplierTO;
    }

    @Override
    public ProductWithSupplierTO fillProductWithSupplierTO(IProduct product) throws NotInDatabaseException {
        final ProductWithSupplierTO result = new ProductWithSupplierTO();
        result.setId(product.getId());
        result.setBarcode(product.getBarcode());
        result.setName(product.getName());
        result.setPurchasePrice(product.getPurchasePrice());
        result.setSupplierTO(fillSupplierTO(product.getSupplier()));

        return result;
    }

}
