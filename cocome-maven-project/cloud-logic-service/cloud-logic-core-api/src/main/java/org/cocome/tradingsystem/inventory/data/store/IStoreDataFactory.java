/*
 ***************************************************************************
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
 ***************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.store;

import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

/**
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */
public interface IStoreDataFactory {
    IOrderEntry getNewOrderEntry();

    IProductOrder getNewProductOrder();

    IStockItem getNewStockItem();

    IOnDemandItem getNewOnDemandItem();

    IStore getNewStore();

    IOrderEntry convertToOrderEntry(ComplexOrderEntryTO entryTO);

    ComplexOrderEntryTO fillComplexOrderEntryTO(IOrderEntry entry) throws NotInDatabaseException;

    IProductOrder convertToProductOrder(ComplexOrderTO orderTO);

    ComplexOrderTO fillComplexOrderTO(IProductOrder order) throws NotInDatabaseException;

    IOnDemandItem convertToOnDemandItem(OnDemandItemTO onDemandItemTO);

    IItem convertToItem(ItemTO itemTO);

    IStockItem convertToStockItem(StockItemTO stockItemTO);

    ItemTO fillItemTO(IItem item);

    StockItemTO fillStockItemTO(IStockItem stockItem);

    OnDemandItemTO fillOnDemandItemTO(IOnDemandItem onDemandItem);

    IStore convertToStore(StoreTO storeTO);

    StoreTO fillStoreTO(IStore store);

    OrderEntryTO fillOrderEntryTO(IOrderEntry entry);

    OrderTO fillOrderTO(IProductOrder order);

    ProductWithItemTO fillProductWithItemTO(IItem stockItem);

    ProductWithSupplierAndItemTO fillProductWithSupplierAndStockItemTO(
            IStockItem stockItem) throws NotInDatabaseException;


    ProductWithSupplierAndItemTO fillProductWithSupplierAndOnDemandItemTO(
            IOnDemandItem onDemandItem) throws NotInDatabaseException;

    StoreWithEnterpriseTO fillStoreWithEnterpriseTO(
            IStore store) throws NotInDatabaseException;
}
