/*
 *************************************************************************
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
 *************************************************************************
 */

package org.cocome.tradingsystem.inventory.application.store;

import org.cocome.tradingsystem.inventory.data.store.IStockItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents an stock item that can be produced and delivered on demands.
 *
 * @author Sebastian Herold
 * @author Lubomir Bulej
 * @see IStockItem
 */
@XmlType(name = "OnDemandItemTO", namespace = "http://store.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name = "OnDemandItemTO")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class OnDemandItemTO extends ItemTO {

    private static final long serialVersionUID = 5874806761123366899L;

}
