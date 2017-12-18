package org.cocome.tradingsystem.remote.access.connection;

public interface IBackendQuery {

    String getMessage();

    void setMessage(String message);

    String getProducts(String cond);

    String getEnterprises(String cond);

    String getStores(String cond);

    String getProductOrder(String cond);

    String getProductSupplier(String cond);

    String getEntity(String entity, String cond);

    String getUser(String cond);

    String getCustomer(String cond);
}