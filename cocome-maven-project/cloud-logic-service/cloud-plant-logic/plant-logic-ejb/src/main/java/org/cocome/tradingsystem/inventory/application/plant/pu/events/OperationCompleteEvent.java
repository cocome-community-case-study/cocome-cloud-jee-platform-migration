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

package org.cocome.tradingsystem.inventory.application.plant.pu.events;

import org.cocome.tradingsystem.inventory.application.store.SaleTO;

import java.io.Serializable;


/**
 * Event emitted by the cash desk on the store topic when a sale is finished and
 * should be registered in the inventory. It contains details of the sale so
 * that it can be accounted for in the inventory system.
 * 
 * @author Yannick Welsch
 */
public class OperationCompleteEvent implements Serializable {

	private static final long serialVersionUID = -5441935251526952790L;

	//

	private final SaleTO __sale;

	private final long __storeID;

	//

	public OperationCompleteEvent(final long storeID, final SaleTO sale) {
		__storeID = storeID;
		__sale = sale;
	}

	public SaleTO getSale() {
		return __sale;
	}
	
	public long getStoreID() {
		return __storeID;
	}

}
