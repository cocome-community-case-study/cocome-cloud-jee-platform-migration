package org.cocome.tradingsystem.inventory.data.enterprise;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;

@Dependent
public class EnterpriseDatatypesFactory implements IEnterpriseDataFactory {
    private static final Logger LOG = Logger.getLogger(EnterpriseDatatypesFactory.class);

    @Inject
    private Provider<Plant> plantProvider;

    @Inject
    @RegularProduct
    private Provider<Product> productProvider;

    @Inject
    private Provider<ProductSupplier> productSupplierProvider;

    @Inject
    private Provider<TradingEnterprise> enterpriseProvider;

    @Inject
    private Provider<ICustomProduct> customProductProvider;

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
    public ICustomProduct getNewCustomProduct() {
        return customProductProvider.get();
    }

    @Override
    public ICustomProduct convertToCustomProduct(CustomProductTO customProductTO) {
        return (ICustomProduct) convertToProduct(customProductTO);
    }

    @Override
    public CustomProductTO fillCustomProductTO(ICustomProduct product) {
        return (CustomProductTO) fillProductTO(product);
    }

    @Override
    public IProduct convertToProduct(ProductTO productTO) {
        IProduct product = getProductInstance(productTO);
        product.setBarcode(productTO.getBarcode());
        product.setId(productTO.getId());
        product.setName(productTO.getName());
        product.setPurchasePrice(productTO.getPurchasePrice());
        return product;
    }


    private IProduct getProductInstance(ProductTO productTO) {
        if (productTO instanceof CustomProductTO) {
            return getNewCustomProduct();
        }
        return getNewProduct();
    }

    @Override
    public ProductTO fillProductTO(IProduct product) {
        ProductTO productTO = getProductTOInstance(product);
        productTO.setBarcode(product.getBarcode());
        productTO.setId(product.getId());
        productTO.setName(product.getName());
        productTO.setPurchasePrice(product.getPurchasePrice());
        return productTO;
    }

    private ProductTO getProductTOInstance(IProduct product) {
        if (product instanceof ICustomProduct) {
            return new CustomProductTO();
        }
        return new ProductTO();
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

        result.setProductTO(fillProductTO(product));
        result.setSupplierTO(fillSupplierTO(product.getSupplier()));

        return result;
    }

    @Override
    public IPlant getNewPlant() {
        return plantProvider.get();
    }

    @Override
    public IPlant convertToPlant(PlantTO plantTO) {
        IPlant plant = getNewPlant();
        plant.setName(plantTO.getName());
        plant.setLocation(plantTO.getLocation());
        plant.setId(plantTO.getId());
        plant.setEnterpriseId(plantTO.getEnterpriseTO().getId());
        return plant;
    }

    @Override
    public PlantTO fillPlantTO(IPlant plant)
            throws NotInDatabaseException {
        final PlantTO result = new PlantTO();
        result.setId(plant.getId());
        result.setName(plant.getName());
        result.setLocation(plant.getLocation());
        result.setEnterpriseTO(fillEnterpriseTO(plant.getEnterprise()));

        LOG.debug(String.format("Got plant with id %d, name %s and enterprise %s.",
                result.getId(),
                result.getName(),
                result.getEnterpriseTO().getName()));

        return result;
    }
}
