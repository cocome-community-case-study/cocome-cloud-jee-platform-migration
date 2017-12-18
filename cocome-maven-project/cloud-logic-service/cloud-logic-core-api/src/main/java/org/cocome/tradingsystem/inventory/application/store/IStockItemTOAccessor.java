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

/**
 * Interface provide access to the {@link StockItemTO} transfer object. This is
 * necessary to allow creating code that need to handle access to {@link ProductWithItemTO}, {@link ProductWithSupplierTO}, and
 * {@link ProductWithSupplierAndItemTO} instances in a generic way.
 * 
 * 
 * @author Lubomir Bulej
 */
public interface IStockItemTOAccessor {

	/**
	 * Returns the {@link StockItemTO} transfer object.
	 * 
	 * @return
	 *         {@link StockItemTO} transfer object.
	 */
	StockItemTO getStockItemTO();

	/**
	 * Sets the {@link StockItemTO} transfer object.
	 * 
	 * @param stockitemTO
	 *            the {@link StockItemTO} transfer object.
	 */
	void setStockItemTO(final StockItemTO stockitemTO);

}
