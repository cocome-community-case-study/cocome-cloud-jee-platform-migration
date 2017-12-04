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

package org.cocome.logic.webservice.storeservice;

import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.exception.RecipeException;

import javax.ejb.CreateException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@WebService(targetNamespace = "http://store.webservice.logic.cocome.org/")
public interface IStoreManager {

    @WebMethod
    long accountSale(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "sale") SaleTO sale)
            throws ProductOutOfStockException, NotInDatabaseException, UpdateException, RecipeException;

    @WebMethod
    StoreWithEnterpriseTO getStore(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID)
            throws NotInDatabaseException;

    @WebMethod
    List<ProductWithItemTO> getProductsWithLowStock(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID)
            throws NotInDatabaseException;

    @WebMethod
    List<ProductWithSupplierTO> getAllStoreProducts(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID)
            throws NotInDatabaseException;

    @WebMethod
    List<ProductWithSupplierAndStockItemTO> getProductsWithStockItems(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID)
            throws NotInDatabaseException;

    @WebMethod
    List<ComplexOrderTO> orderProducts(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "complexOrder") ComplexOrderTO complexOrder)
            throws NotInDatabaseException, CreateException, UpdateException;

    @WebMethod
    ComplexOrderTO getOrder(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "orderID") long orderId)
            throws NotInDatabaseException;

    @WebMethod
    List<ComplexOrderTO> getOutstandingOrders(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID)
            throws NotInDatabaseException;

    @WebMethod
    void rollInReceivedOrder(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "orderID") long orderId)
            throws InvalidRollInRequestException, NotInDatabaseException, UpdateException;

    @WebMethod
    ProductWithItemTO changePrice(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "itemTO") ProductWithItemTO itemTO)
            throws NotInDatabaseException, UpdateException;

    @WebMethod
    void updateItem(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "itemTO") ProductWithItemTO itemTO)
            throws NotInDatabaseException, UpdateException;

    @WebMethod
    long createItem(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "itemTO") ProductWithItemTO itemTO)
            throws NotInDatabaseException, CreateException;

    @WebMethod
    void deleteItem(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "itemTO") ProductWithItemTO itemTO)
            throws NotInDatabaseException, UpdateException;

    @WebMethod
    void markProductsUnavailableInStock(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "movedProductAmounts") ProductMovementTO movedProductAmounts)
            throws ProductOutOfStockException, NotInDatabaseException, UpdateException;

    @WebMethod
    ComplexOrderEntryTO[] getStockItems(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "requiredProductTOs") ProductTO[] requiredProductTOs)
            throws NotInDatabaseException, NotImplementedException;

    @WebMethod
    ProductWithItemTO getProductWithStockItem(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "productBarcode") long productBarcode)
            throws NoSuchProductException, NotInDatabaseException;

    /* Plant Operation callbacks **************/

    @WebMethod
    void onProductionFinish(
            @XmlElement(required = true)
            @WebParam(name = "productionOrderEntryID") final long productionOrderEntryId);

    @WebMethod
    void onProductionOrderFinish(
            @XmlElement(required = true)
            @WebParam(name = "productionOrderID") final long productionOrderId);

    @WebMethod
    void onProductionOrderEntryFinish(
            @XmlElement(required = true)
            @WebParam(name = "productionOrderEntryID") final long productioinOrderEntryId);
}