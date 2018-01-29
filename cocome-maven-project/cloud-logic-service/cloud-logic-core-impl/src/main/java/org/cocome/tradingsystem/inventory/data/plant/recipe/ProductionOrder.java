/*
 **************************************************************************
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
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.inventory.data.store.IStore;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * The class represents an order of a {@link IRecipe} in the database.
 *
 * @author Rudolf Biczok
 */

@Dependent
public class ProductionOrder implements IProductionOrder, Serializable {

    private static final long serialVersionUID = -8340585715760459030L;

    private long id;
    private Date deliveryDate;
    private Date orderingDate;
    private IStore store;
    private ITradingEnterprise enterprise;
    private Collection<IProductionOrderEntry> orderEntries;
    private boolean finished;

    private long storeId;
    private long enterpriseId;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        store = null;
        enterprise = null;
        orderEntries = null;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public Date getOrderingDate() {
        return this.orderingDate;
    }

    @Override
    public void setOrderingDate(final Date orderingDate) {
        this.orderingDate = orderingDate;
    }

    @Override
    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    @Override
    public void setDeliveryDate(final Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public Collection<IProductionOrderEntry> getOrderEntries() {
        return this.orderEntries;
    }

    @Override
    public void setOrderEntries(Collection<IProductionOrderEntry> orderEntries) {
        this.orderEntries = orderEntries;
    }

    @Override
    public ITradingEnterprise getEnterprise() throws NotInDatabaseException {
        if (enterprise == null) {
            enterprise = enterpriseQuery.queryEnterpriseById(enterpriseId);
        }
        return this.enterprise;
    }

    @Override
    public void setEnterprise(ITradingEnterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Override
    public long getEnterpriseId() {
        return enterpriseId;
    }

    @Override
    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public IStore getStore() throws NotInDatabaseException {
        if (store == null) {
            store = enterpriseQuery.queryStoreByID(storeId);
        }
        return this.store;
    }

    @Override
    public void setStore(final IStore store) {
        this.store = store;
    }

    @Override
    public long getStoreId() {
        return storeId;
    }

    @Override
    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
