/***************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package org.cocome.tradingsystem.inventory.application.store;

import javax.ejb.Local;


/**
 * Provides remote access to the store inventory. This interface is used by a
 * cash desk to retrieve product descriptions for a given barcode.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 */
@Local
public interface IStoreInventoryLocal {

	/**
	 * Determine product and a corresponding store stock item by product
	 * barcode. Used for realization of UC 1 and UC 4
	 * 
	 * @param productBarcode
	 *            the product barcode
	 * @return
	 *         An instance of {@link ProductWithStockItemTO} transfer object
	 *         containing a product and stock item description corresponding
	 *         to the given barcode.
	 * 
	 * @throws NoSuchProductException
	 *             if there is no product (or a stock item for product) with the
	 *             given barcode in the store
	 */
	public ProductWithStockItemTO getProductWithStockItem(long storeID, 
			long productBarcode
			) throws NoSuchProductException;

}
