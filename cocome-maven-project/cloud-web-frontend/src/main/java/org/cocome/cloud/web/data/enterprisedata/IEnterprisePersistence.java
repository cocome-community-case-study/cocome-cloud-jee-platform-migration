package org.cocome.cloud.web.data.enterprisedata;

import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;

public interface IEnterprisePersistence {
	public String createEnterprise(@NotNull String name) throws NotInDatabaseException_Exception;
	
	public String updateEnterprise(@NotNull EnterpriseViewData enterprise);
	
	public String updateProduct(@NotNull String name, long barcode, double purchasePrice);
	
	public String createProduct(@NotNull String name, long barcode, double purchasePrice) throws NotInDatabaseException_Exception;
}
