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
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.IPlantQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Represents a single {@link IPlantOperationOrder} entry in the database.
 *
 * @author Rudolf Biczok
 */
@Dependent
public class PlantOperationOrderEntry implements Serializable, IPlantOperationOrderEntry {

    private static final long serialVersionUID = -7683436740437770058L;

    private long id;
    private long amount;
    private IPlantOperation operation;
    private IPlantOperationOrder order;

    private long operationId;
    private long orderId;

    @Inject
    private Instance<IPlantQuery> plantQueryInstance;

    private IPlantQuery plantQuery;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        plantQuery = plantQueryInstance.get();
        operation = null;
        order = null;
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
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(final long amount) {
        this.amount = amount;
    }

    @Override
    public IPlantOperationOrder getOrder() throws NotInDatabaseException {
        if (order == null) {
            order = plantQuery.queryPlantOperationOrderById(orderId);
        }
        return this.order;
    }

    @Override
    public void setOrder(final IPlantOperationOrder productOrder) {
        this.order = productOrder;
    }

    @Override
    public long getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public IPlantOperation getOperation() throws NotInDatabaseException {
        if (operation == null) {
            operation = enterpriseQuery.queryPlantOperationByID(operationId);
        }
        return this.operation;
    }

    @Override
    public void setOperation(final IPlantOperation operation) {
        this.operation = operation;
    }

    @Override
    public long getOperationId() {
        return operationId;
    }

    @Override
    public void setOperatioId(long operationId) {
        this.operationId = operationId;
    }
}
