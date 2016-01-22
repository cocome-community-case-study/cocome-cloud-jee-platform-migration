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

package org.cocome.tradingsystem.cashdeskline.datatypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Enumerates the control keys that can be pressed on the cash box keyboard.
 * 
 * @author Lubomir Bulej
 */
@XmlType(name = "ControlKeyStroke")
@XmlEnum
public enum ControlKeyStroke {
	@XmlEnumValue("START_SALE")
	START_SALE,
	@XmlEnumValue("FINISH_SALE")
	FINISH_SALE,
	@XmlEnumValue("CASH_PAYMENT")
	CASH_PAYMENT,
	@XmlEnumValue("CREDIT_CARD_PAYMENT")
	CREDIT_CARD_PAYMENT,
	@XmlEnumValue("DISABLE_EXPRESS_MODE")
	DISABLE_EXPRESS_MODE;
}
