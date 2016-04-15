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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A transfer object class for exchanging basic product information and
 * additional stock item information. It references a single stock item for a
 * specific store. between client and the service-oriented application layer. It
 * contains either copies of persisted data which are transferred to the client,
 * or data which is transferred from the client to the application layer to be
 * processed and persisted.
 * 
 * @author Sebastian Herold
 * @author Lubomir Bulej
 */
@XmlType(name="ProductWithStockItemTO", namespace="http://store.application.inventory.tradingsystem.cocome.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductWithStockItemTO
		extends ProductTO {

	private static final long serialVersionUID = 3541228255270656771L;

	//

	@XmlElement(name="stockItemTO", required=true)
	private StockItemTO __stockItemTO;

	/*
	 * @see StockItemTOAccessor#getStockItemTO()
	 */
	public StockItemTO getStockItemTO() {
		return __stockItemTO;
	}

	/*
	 * @see StockItemTOAccessor#setStockItemTO(StockItemTO)
	 */
	public void setStockItemTO(final StockItemTO stockItemTO) {
		__stockItemTO = stockItemTO;
	}
	
	public boolean equals(Object e) {
		if (e instanceof ProductWithStockItemTO) {
			ProductWithStockItemTO prod = (ProductWithStockItemTO) e;
			return prod.getBarcode() == this.getBarcode() 
					&& prod.getStockItemTO().getId() == this.getStockItemTO().getId();
		}
		return false;
	}

}
