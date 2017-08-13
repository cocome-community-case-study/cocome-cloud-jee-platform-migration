package org.cocome.logic.webservice.storeservice;

import org.cocome.tradingsystem.inventory.application.store.*;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.CreateException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@WebService(targetNamespace = "http://store.webservice.logic.cocome.org/")
public interface IStoreManager {

    @WebMethod
    void accountSale(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "sale") SaleTO sale)
            throws ProductOutOfStockException, NotInDatabaseException, UpdateException;

    @WebMethod
    StoreWithEnterpriseTO getStore(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID)
            throws NotInDatabaseException;

    @WebMethod
    List<ProductWithStockItemTO> getProductsWithLowStock(
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
    ProductWithStockItemTO changePrice(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "stockItemTO") StockItemTO stockItemTO)
            throws NotInDatabaseException, UpdateException;

    @WebMethod
    void updateStockItem(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "stockItemTO") StockItemTO stockItemTO)
            throws NotInDatabaseException, UpdateException;

    @WebMethod
    void createStockItem(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "stockItemTO") ProductWithStockItemTO stockItemTO)
            throws NotInDatabaseException, CreateException;

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
    ProductWithStockItemTO getProductWithStockItem(
            @XmlElement(required = true) @WebParam(name = "storeID") long storeID,
            @XmlElement(required = true) @WebParam(name = "productBarcode") long productBarcode)
            throws NoSuchProductException, NotInDatabaseException;

}