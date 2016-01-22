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

package org.cocome.tradingsystem.cashdeskline.cashdesk.userdisplay;

import javax.ejb.Local;

/**
 * Defines user display model actions that can be triggered from outside.
 * 
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 */
@Local
public interface IUserDisplayLocal {

	/**
	 * Sets the content of the user display to the given message.
	 * 
	 * @param messageKind
	 * 		the message kind of the message
	 * 
	 * @param message
	 * 		the message to display
	 */
	public void setContent(MessageKind messageKind, String message);

	/**
	 * 
	 * @return
	 * 		the currently displayed message on this display
	 */
	public String getMessage();

	/**
	 * 
	 * @return
	 * 		the message kind of the current message
	 */
	public MessageKind getMessageKind();

}
