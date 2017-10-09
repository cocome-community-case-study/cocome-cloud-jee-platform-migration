package org.cocome.tradingsystem.inventory.data.enterprise;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.BooleanCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.CustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.NorminalCustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.EntryPointTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.ICustomProductParameter;
import org.cocome.tradingsystem.inventory.data.enterprise.parameter.INorminalCustomProductParameter;
import org.cocome.tradingsystem.inventory.data.plant.IPlant;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPoint;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IEntryPoint;
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
    private Provider<Product> productProvider;

    @Inject
    private Provider<ProductSupplier> productSupplierProvider;

    @Inject
    private Provider<EntryPoint> entryPointProvider;

    @Inject
    private Provider<TradingEnterprise> enterpriseProvider;

    @Inject
    private Provider<ICustomProduct> customProductProvider;

    @Inject
    private Provider<IBooleanCustomProductParameter> booleanCustomProductParameterProvider;

    @Inject
    private Provider<INorminalCustomProductParameter> norminalCustomProductParameterProvider;

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
    public IEntryPoint getNewEntryPoint() {
        return entryPointProvider.get();
    }

    @Override
    public ICustomProduct getNewCustomProduct() {
        return customProductProvider.get();
    }

    @Override
    public ICustomProduct convertToCustomProduct(CustomProductTO customProductTO) {
        ICustomProduct customProduct = getNewCustomProduct();
        customProduct.setBarcode(customProductTO.getBarcode());
        customProduct.setId(customProductTO.getId());
        customProduct.setName(customProductTO.getName());
        customProduct.setPurchasePrice(customProductTO.getPurchasePrice());
        return customProduct;
    }

    @Override
    public CustomProductTO fillCustomProductTO(ICustomProduct product) {
        CustomProductTO customProductTO = new CustomProductTO();
        customProductTO.setBarcode(product.getBarcode());
        customProductTO.setId(product.getId());
        customProductTO.setName(product.getName());
        customProductTO.setPurchasePrice(product.getPurchasePrice());
        return customProductTO;
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

    @Override
    public EntryPointTO fillEntryPointTO(IEntryPoint entryPoint) {
        final EntryPointTO entryPointTO = new EntryPointTO();
        entryPointTO.setId(entryPoint.getId());
        entryPointTO.setName(entryPoint.getName());

        return entryPointTO;
    }

    @Override
    public IEntryPoint convertToEntryPoint(EntryPointTO entryPointTO) {
        final IEntryPoint entryPoint = getNewEntryPoint();
        entryPoint.setId(entryPointTO.getId());
        entryPoint.setName(entryPointTO.getName());

        return entryPoint;
    }

    @Override
    public CustomProductParameterTO fillCustomProductParameterTO(ICustomProductParameter parameter)
            throws NotInDatabaseException {
        if (IBooleanCustomProductParameter.class.isAssignableFrom(parameter.getClass())) {
            return this.fillBooleanCustomProductParameterTO(
                    (IBooleanCustomProductParameter) parameter);
        } else if (INorminalCustomProductParameter.class.isAssignableFrom(parameter.getClass())) {
            return this.fillNorminalCustomProductParameterTO(
                    (INorminalCustomProductParameter) parameter);
        }
        throw new IllegalArgumentException("Unknown class to handle: " + parameter.getClass());
    }

    @Override
    public IBooleanCustomProductParameter getNewBooleanCustomProductParameter() {
        return booleanCustomProductParameterProvider.get();
    }

    @Override
    public BooleanCustomProductParameterTO fillBooleanCustomProductParameterTO(IBooleanCustomProductParameter
                                                                                       booleanCustomProductParameter) throws NotInDatabaseException {
        final BooleanCustomProductParameterTO result = new BooleanCustomProductParameterTO();
        result.setId(booleanCustomProductParameter.getId());
        result.setName(booleanCustomProductParameter.getName());
        result.setCategory(booleanCustomProductParameter.getCategory());
        result.setCustomProduct(fillCustomProductTO(booleanCustomProductParameter.getCustomProduct()));

        return result;
    }

    @Override
    public IBooleanCustomProductParameter convertToBooleanCustomProductParameter(BooleanCustomProductParameterTO
                                                                                         booleanCustomProductParameterTO) {
        final IBooleanCustomProductParameter result = getNewBooleanCustomProductParameter();
        result.setId(booleanCustomProductParameterTO.getId());
        result.setName(booleanCustomProductParameterTO.getName());
        result.setCategory(booleanCustomProductParameterTO.getCategory());
        result.setCustomProductId(booleanCustomProductParameterTO.getCustomProduct().getId());

        return result;
    }

    @Override
    public INorminalCustomProductParameter getNewNorminalCustomProductParameter() {
        return norminalCustomProductParameterProvider.get();
    }

    @Override
    public NorminalCustomProductParameterTO fillNorminalCustomProductParameterTO(INorminalCustomProductParameter
                                                                                         norminalCustomProductParameter) throws NotInDatabaseException {
        final NorminalCustomProductParameterTO result = new NorminalCustomProductParameterTO();
        result.setId(norminalCustomProductParameter.getId());
        result.setName(norminalCustomProductParameter.getName());
        result.setCategory(norminalCustomProductParameter.getCategory());
        result.setOptions(norminalCustomProductParameter.getOptions());
        result.setCustomProduct(fillCustomProductTO(norminalCustomProductParameter.getCustomProduct()));

        return result;
    }

    @Override
    public INorminalCustomProductParameter convertToNorminalCustomProductParameter(NorminalCustomProductParameterTO
                                                                                           norminalCustomProductParameterTO) {
        final INorminalCustomProductParameter result = getNewNorminalCustomProductParameter();
        result.setId(norminalCustomProductParameterTO.getId());
        result.setName(norminalCustomProductParameterTO.getName());
        result.setCategory(norminalCustomProductParameterTO.getCategory());
        result.setOptions(norminalCustomProductParameterTO.getOptions());
        result.setCustomProductId(norminalCustomProductParameterTO.getCustomProduct().getId());

        return result;
    }
}
