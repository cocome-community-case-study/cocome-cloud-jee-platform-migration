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

package org.cocome.tradingsystem.cashdeskline.cashdesk.barcodescanner;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cocome.tradingsystem.cashdeskline.events.ProductBarcodeScannedEvent;

/**
 * Implements the cash desk barcode scanner model. The scanner does not consume
 * any events, instead it only produces {@link ProductBarcodeScannedEvent} events whenever a barcode is scanned.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Stateless
public class BarcodeScannerModel implements IBarcodeScannerLocal {
	
	@Inject
	private Event<ProductBarcodeScannedEvent> productBarcodeScannedEvent;


	//
	// Barcode scanner model methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendProductBarcode(final long barcode) {
		productBarcodeScannedEvent.fire(new ProductBarcodeScannedEvent(barcode));
	}
}
