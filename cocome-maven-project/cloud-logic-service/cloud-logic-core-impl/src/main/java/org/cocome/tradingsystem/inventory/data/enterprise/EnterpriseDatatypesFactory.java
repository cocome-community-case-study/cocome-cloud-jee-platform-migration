package org.cocome.tradingsystem.inventory.data.enterprise;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

@Dependent
class EnterpriseDatatypesFactory implements IEnterpriseDataFactory {
	
	@Inject
	Provider<Product> productProvider;
	
	@Inject
	Provider<ProductSupplier> productSupplierProvider;
	
	@Inject
	Provider<TradingEnterprise> enterpriseProvider;
	

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#getNewProduct()
	 */
	@Override
	public IProduct getNewProduct() {
		return productProvider.get();
	}
	
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#getNewProductSupplier()
	 */
	@Override
	public IProductSupplier getNewProductSupplier() {
		return productSupplierProvider.get();
	}
	
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#getNewTradingEnterprise()
	 */
	@Override
	public ITradingEnterprise getNewTradingEnterprise() {
		return enterpriseProvider.get();
	}
	
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#convertToProduct(org.cocome.tradingsystem.inventory.application.store.ProductTO)
	 */
	@Override
	public IProduct convertToProduct(ProductTO productTO) {
		IProduct product = getNewProduct();
		product.setBarcode(productTO.getBarcode());
		product.setId(productTO.getId());
		product.setName(productTO.getName());
		product.setPurchasePrice(productTO.getPurchasePrice());
		return product;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#fillProductTO(org.cocome.tradingsystem.inventory.data.enterprise.IProduct)
	 */
	@Override
	public ProductTO fillProductTO(IProduct product) {
		ProductTO productTO = new ProductTO();
		productTO.setBarcode(product.getBarcode());
		productTO.setId(product.getId());
		productTO.setName(product.getName());
		productTO.setPurchasePrice(product.getPurchasePrice());
		return productTO;
	}
	

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#convertToEnterprise(org.cocome.tradingsystem.inventory.application.store.EnterpriseTO)
	 */
	@Override
	public ITradingEnterprise convertToEnterprise(EnterpriseTO enterpriseTO) {
		ITradingEnterprise enterprise = getNewTradingEnterprise();
		enterprise.setId(enterpriseTO.getId());
		enterprise.setName(enterpriseTO.getName());
		return enterprise;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#fillEnterpriseTO(org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise)
	 */
	@Override
	public EnterpriseTO fillEnterpriseTO(ITradingEnterprise enterprise) {
		EnterpriseTO enterpriseTO = new EnterpriseTO();
		enterpriseTO.setId(enterprise.getId());
		enterpriseTO.setName(enterprise.getName());
		return enterpriseTO;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#convertToSupplier(org.cocome.tradingsystem.inventory.application.store.SupplierTO)
	 */
	@Override
	public IProductSupplier convertToSupplier(SupplierTO supplierTO) {
		IProductSupplier supplier = getNewProductSupplier();
		supplier.setId(supplierTO.getId());
		supplier.setName(supplierTO.getName());
		return supplier;
	}
	
	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#fillSupplierTO(org.cocome.tradingsystem.inventory.data.enterprise.IProductSupplier)
	 */
	@Override
	public SupplierTO fillSupplierTO(IProductSupplier supplier) {
		SupplierTO supplierTO = new SupplierTO();
		if (supplier != null) {
			supplierTO.setId(supplier.getId());
			supplierTO.setName(supplier.getName());
		}
		return supplierTO;
	}

	/* (non-Javadoc)
	 * @see org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory#fillProductWithSupplierTO(org.cocome.tradingsystem.inventory.data.enterprise.IProduct)
	 */
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
