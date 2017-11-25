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

package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author Rudolf Biczok
 */
@Dependent
public class EntryPointInteraction extends InteractionEntity<IEntryPoint, IEntryPoint>
        implements IEntryPointInteraction {
    private static final long serialVersionUID = 1L;

    private IEntryPoint from;
    private IEntryPoint to;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void initPlant() {
        enterpriseQuery = enterpriseQueryInstance.get();
        from = null;
        to = null;
    }

    @Override
    public IEntryPoint getFrom() throws NotInDatabaseException {
        if (from == null) {
            from = enterpriseQuery.queryEntryPointByID(fromId);
        }
        return from;
    }

    @Override
    public void setFrom(IEntryPoint from) {
        this.from = from;
    }

    @Override
    public IEntryPoint getTo() throws NotInDatabaseException {
        if (to == null) {
            to = enterpriseQuery.queryEntryPointByID(toId);
        }
        return to;
    }

    @Override
    public void setTo(IEntryPoint to) {
        this.to = to;
    }
}
