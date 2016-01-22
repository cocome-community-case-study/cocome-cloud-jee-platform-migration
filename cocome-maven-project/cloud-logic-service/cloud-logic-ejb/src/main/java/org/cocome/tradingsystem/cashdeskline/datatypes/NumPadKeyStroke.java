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
 * Enumerates the keys that can be pressed on the cash box keyboard.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 */
@XmlType(name = "NumPadKeyStroke")
@XmlEnum
public enum NumPadKeyStroke {
	@XmlEnumValue("ZERO")
	ZERO("0"),
	@XmlEnumValue("ONE")
	ONE("1"),
	@XmlEnumValue("TWO")
	TWO("2"),
	@XmlEnumValue("THREE")
	THREE("3"),
	@XmlEnumValue("FOUR")
	FOUR("4"),
	@XmlEnumValue("FIVE")
	FIVE("5"),
	@XmlEnumValue("SIX")
	SIX("6"),
	@XmlEnumValue("SEVEN")
	SEVEN("7"),
	@XmlEnumValue("EIGHT")
	EIGHT("8"),
	@XmlEnumValue("NINE")
	NINE("9"),
	@XmlEnumValue("COMMA")
	COMMA("."),
	@XmlEnumValue("ENTER")
	ENTER("ENTER");

	//

	private final String __label;

	//

	NumPadKeyStroke(final String label) {
		__label = label;
	}

	public String label() {
		return __label;
	}

}
