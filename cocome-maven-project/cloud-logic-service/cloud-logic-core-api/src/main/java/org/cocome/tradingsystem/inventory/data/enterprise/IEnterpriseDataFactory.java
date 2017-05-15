package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.ProductTO;
import org.cocome.tradingsystem.inventory.application.store.ProductWithSupplierTO;
import org.cocome.tradingsystem.inventory.application.store.SupplierTO;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

public interface IEnterpriseDataFactory {

	public IProduct getNewProduct();

	public IProductSupplier getNewProductSupplier();

	public ITradingEnterprise getNewTradingEnterprise();

	public IProduct convertToProduct(ProductTO productTO);

	public ITradingEnterprise convertToEnterprise(EnterpriseTO enterpriseTO);

	public IProductSupplier convertToSupplier(SupplierTO supplierTO);

	public SupplierTO fillSupplierTO(IProductSupplier supplier);

	public EnterpriseTO fillEnterpriseTO(ITradingEnterprise enterprise);

	public ProductTO fillProductTO(IProduct product);
	
	public ProductWithSupplierTO fillProductWithSupplierTO(
			IProduct product) throws NotInDatabaseException;

}