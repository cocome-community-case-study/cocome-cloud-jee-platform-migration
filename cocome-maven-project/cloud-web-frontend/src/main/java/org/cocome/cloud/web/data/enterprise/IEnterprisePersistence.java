package org.cocome.cloud.web.data.enterprise;

import javax.validation.constraints.NotNull;

public interface IEnterprisePersistence {
	public String createEnterprise(@NotNull String name);
	
	public String updateEnterprise(@NotNull Enterprise enterprise);
	
	public String updateProduct(@NotNull String name, long barcode, double purchasePrice);
	
	public String createProduct(@NotNull String name, long barcode, double purchasePrice);
}
