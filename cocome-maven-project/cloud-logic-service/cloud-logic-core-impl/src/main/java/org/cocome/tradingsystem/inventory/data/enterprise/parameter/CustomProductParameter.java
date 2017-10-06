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

package org.cocome.tradingsystem.inventory.data.enterprise.parameter;

import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Implementation of {@link ICustomProductParameter}
 *
 * @author Rudolf Biczok
 */
public abstract class CustomProductParameter implements Serializable, ICustomProductParameter {
    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private String category;
    private long customProductId;
    private ICustomProduct customProduct;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void init() {
        enterpriseQuery = enterpriseQueryInstance.get();
        customProduct = null;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public long getCustomProductId() {
        return customProductId;
    }

    @Override
    public void setCustomProductId(long customProductId) {
        this.customProductId = customProductId;
    }

    @Override
    public ICustomProduct getCustomProduct() throws NotInDatabaseException {
        if (customProduct == null) {
            customProduct = enterpriseQuery.queryCustomProductByID(customProductId);
        }
        return customProduct;
    }

    @Override
    public void setCustomProduct(ICustomProduct customProduct) {
        this.customProduct = customProduct;
    }

}