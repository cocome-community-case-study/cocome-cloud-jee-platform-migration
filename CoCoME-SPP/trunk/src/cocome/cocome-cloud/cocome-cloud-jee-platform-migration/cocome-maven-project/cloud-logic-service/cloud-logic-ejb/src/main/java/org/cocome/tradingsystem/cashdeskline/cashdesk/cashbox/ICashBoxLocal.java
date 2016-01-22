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

package org.cocome.tradingsystem.cashdeskline.cashdesk.cashbox;

import javax.ejb.Local;

import org.cocome.tradingsystem.cashdeskline.cashdesk.IllegalCashDeskStateException;
import org.cocome.tradingsystem.cashdeskline.datatypes.ControlKeyStroke;
import org.cocome.tradingsystem.cashdeskline.datatypes.NumPadKeyStroke;

/**
 * Defines cash box model actions that can be triggered from outside.
 * 
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 */
@Local
public interface ICashBoxLocal {

	/**
	 * Opens this cash box to enable the completion of the payment process.
	 */
	public void open();

	/**
	 * Closes the cash box and signals the completion of payment.
	 */
	public void close() throws IllegalCashDeskStateException;
	
	/**
	 * 
	 * @return the status of this cash desk
	 */
	public boolean isOpen();

	/**
	 * Handles the pressing of a control key. 
	 * For possible control keys {@link ControlKeyStroke}.
	 * 
	 * @param keystroke
	 * 		the pressed key
	 * @throws IllegalInputException 
	 */
	public void pressControlKey(ControlKeyStroke keystroke) throws IllegalCashDeskStateException, IllegalInputException;

	/**
	 * Handles the pressing of a numpad key. This is only valid 
	 * during the insertion of a cash payment amount. For possible 
	 * numpad keys see {@link NumPadKeyStroke}.
	 * 
	 * @param key
	 * 		the pressed key
	 */
	public void pressNumpadKey(NumPadKeyStroke key) throws IllegalCashDeskStateException, IllegalInputException;

	/**
	 * Closes this cash box silently, that means without sending an 
	 * event to signal the comppletion of payment.
	 */
	public void closeSilently();
}
