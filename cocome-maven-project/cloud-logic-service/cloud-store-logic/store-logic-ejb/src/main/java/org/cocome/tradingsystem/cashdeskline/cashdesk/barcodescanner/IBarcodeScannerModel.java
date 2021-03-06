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

import javax.ejb.Local;

/**
 * Defines barcode scanner actions that can be triggered from outside.
 * 
 * @author Holger Klus
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
@Local
public interface IBarcodeScannerModel {

	/**
	 * Sends the event that a product barcode was scanned.
	 * 
	 * @param barcode
	 * 		the scanned barcode
	 */
	void sendProductBarcode(long barcode);

}
