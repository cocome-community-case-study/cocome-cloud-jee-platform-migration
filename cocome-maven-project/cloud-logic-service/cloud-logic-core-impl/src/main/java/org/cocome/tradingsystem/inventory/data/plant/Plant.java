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

package org.cocome.tradingsystem.inventory.data.plant;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.enterprise.ITradingEnterprise;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Represents a plant in the database.
 *
 * @author Rudolf Biczok
 */

@Dependent
class Plant implements Serializable, IPlant {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(Plant.class);

    private long id;

    private long enterpriseId;

    private String name;

    private String location;

    private ITradingEnterprise enterprise;

    @Inject
    Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        enterprise = null;
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
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(final String location) {
        this.location = location;
    }

    @Override
    public ITradingEnterprise getEnterprise() throws NotInDatabaseException {
        if (enterprise == null) {
            enterprise = enterpriseQuery.queryEnterpriseById(enterpriseId);
            LOG.debug(String.format("Retrieved enterprise [%d, %s] for plant %s", enterprise.getId(), enterprise.getName(), name));
        }
        return enterprise;
    }

    @Override
    public void setEnterprise(final ITradingEnterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Override
    public String toString() {
        return "[Id:" + getId() + ",Name:" + this.getName() + ",Location:" + this.getLocation() + "]";
    }

    @Override
    public long getEnterpriseId() {
        return enterpriseId;
    }

    @Override
    public void setEnterpriseId(long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

}
