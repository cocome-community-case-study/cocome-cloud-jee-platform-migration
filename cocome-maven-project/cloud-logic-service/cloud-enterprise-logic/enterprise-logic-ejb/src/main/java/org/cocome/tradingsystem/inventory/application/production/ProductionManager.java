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

package org.cocome.tradingsystem.inventory.application.production;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.*;
import org.cocome.tradingsystem.inventory.application.plant.parameter.ParameterValueTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationOrderEntryTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationOrderTO;
import org.cocome.tradingsystem.inventory.application.production.events.PlantOperationOrderFinishedEvent;
import org.cocome.tradingsystem.inventory.data.enterprise.EnterpriseDatatypesFactory;
import org.cocome.tradingsystem.inventory.data.persistence.IPersistenceContext;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.inventory.data.plant.PlantClientFactory;
import org.cocome.tradingsystem.inventory.data.plant.PlantDatatypesFactory;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.cocome.tradingsystem.inventory.data.plant.recipe.exec.RecipeExecutionGraphNode;
import org.cocome.tradingsystem.inventory.data.store.StoreClientFactory;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.exception.RecipeException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.*;

/**
 * Generates the actual interface classes for access the production unit services
 *
 * @author Rudolf Biczok
 */
@Singleton
@LocalBean
public class ProductionManager {

    private static final Logger LOG = Logger.getLogger(ProductionManager.class);

    @Inject
    private StoreClientFactory storeClientFactory;

    @Inject
    private PlantClientFactory plantClientFactory;

    @Inject
    private ProductionJobPool jobPool;

    @Inject
    private IPersistenceContext persistenceContext;

    @Inject
    private EnterpriseDatatypesFactory enterpriseDatatypesFactory;

    @Inject
    private PlantDatatypesFactory plantDatatypesFactory;

    private Map<Long, ProductionJobContext> plantOrderProductionJobMapping = new HashMap<>();

    public void submitOrder(final IProductionOrder order) throws NotInDatabaseException, RecipeException {
        for (final IProductionOrderEntry orderEntry : order.getOrderEntries()) {
            for (int i = 0; i < orderEntry.getAmount(); i++) {
                final IStoreManager storeManager = storeClientFactory.createClient(
                        order.getStore().getId());
                final ProductionJob job = new ProductionJob(storeManager, order, orderEntry);
                jobPool.addJob(job);
                startJob(job);
            }
        }
    }

    public void onOperationFinished(@Observes PlantOperationOrderFinishedEvent event) {
        LOG.info("Plant Operation Finished: " + event.getPlantOperationOrderID());
        if (!plantOrderProductionJobMapping.containsKey(event.getPlantOperationOrderID())) {
            throw new IllegalStateException("Unknown plant operation order: " + event.getPlantOperationOrderID());
        }
        try {
            final ProductionJobContext jobContext = plantOrderProductionJobMapping.get(event.getPlantOperationOrderID());
            plantOrderProductionJobMapping.remove(event.getPlantOperationOrderID());
            processJob(jobContext);
        } catch (final UpdateException_Exception | NotInDatabaseException | UpdateException |
                CreateException_Exception | NotInDatabaseException_Exception e) {
            LOG.error("Exception occurred while processing pu job", e);
            throw new IllegalStateException(e);
        }
    }

    private void startJob(ProductionJob job) throws NotInDatabaseException {
        try {
            submitPlantOperatins(job, job.getExecutionGraph().getStartNodes());
        } catch (final UpdateException_Exception | CreateException_Exception | NotInDatabaseException_Exception e) {
            LOG.error("Exception occurred while processing pu job", e);
            throw new IllegalStateException(e);
        }
    }

    private void submitPlantOperatins(final ProductionJob job, final List<RecipeExecutionGraphNode> nodes)
            throws NotInDatabaseException, CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception {

        final Map<Long, Map<Long, PlantOperationOrderEntryTO>> plantOperationOrderEntryMapping = new HashMap<>();

        for (final RecipeExecutionGraphNode node : nodes) {
            if (node.getOperation() instanceof IPlantOperation) {
                final IPlantOperation plantOperation = (IPlantOperation) node.getOperation();
                plantOperationOrderEntryMapping.putIfAbsent(plantOperation.getPlant().getId(), new HashMap<>());
                if (!plantOperationOrderEntryMapping.get(plantOperation.getPlant().getId())
                        .containsKey(node.getOperation().getId())) {
                    final PlantOperationOrderEntryTO entry = new PlantOperationOrderEntryTO();
                    entry.setPlantOperation(plantDatatypesFactory.fillPlantOperationTO(plantOperation));
                    entry.setAmount(1);
                    entry.setParameterValues(extractParameters(
                            job.getOrderEntry().getParameterValues(),
                            node.getParameterInteractions()));
                    plantOperationOrderEntryMapping.get(plantOperation.getPlant().getId())
                            .put(plantOperation.getId(), entry);
                    continue;
                }
                final PlantOperationOrderEntryTO orderEntry = plantOperationOrderEntryMapping
                        .get(plantOperation.getPlant().getId())
                        .get(plantOperation.getId());
                orderEntry.setAmount(orderEntry.getAmount() + 1);
            } else {
                throw new IllegalArgumentException("Unknown operation type: " + node.getOperation().getClass().getName());
            }
        }

        for (final Map.Entry<Long, Map<Long, PlantOperationOrderEntryTO>> entry : plantOperationOrderEntryMapping.entrySet()) {
            final PlantOperationOrderTO operationOrder = new PlantOperationOrderTO();
            operationOrder.setEnterprise(enterpriseDatatypesFactory.fillEnterpriseTO(
                    job.getOrder().getStore().getEnterprise()));
            operationOrder.setOrderEntries(new ArrayList<>(entry.getValue().values()));
            operationOrder.setPlant(operationOrder.getOrderEntries().iterator().next().getPlantOperation().getPlant());

            final IPlantManager pm = plantClientFactory.createClient(entry.getKey());
            this.plantOrderProductionJobMapping.put(pm.orderOperation(operationOrder), new ProductionJobContext(job, nodes));
        }
    }

    private Collection<ParameterValueTO> extractParameters(
            final Collection<IParameterValue> parameterValues,
            final List<IParameterInteraction> parameterInteractions) throws NotInDatabaseException {
        final List<ParameterValueTO> plantValueParameter = new ArrayList<>(parameterInteractions.size());
        for (final IParameterInteraction parameterInteraction : parameterInteractions) {
            for (final IParameterValue parameterValue : parameterValues) {
                if (parameterInteraction.getFrom().getId() == parameterValue.getParameter().getId()) {
                    final ParameterValueTO plantOperationParameterValue = new ParameterValueTO();
                    plantOperationParameterValue.setValue(parameterValue.getValue());
                    plantOperationParameterValue.setParameter(
                            plantDatatypesFactory.fillParameterTO(parameterInteraction.getTo()));
                    plantValueParameter.add(plantOperationParameterValue);
                    break;
                }
            }
        }
        return plantValueParameter;
    }

    private void processJob(final ProductionJobContext jobContext) throws NotInDatabaseException, UpdateException,
            CreateException_Exception, NotInDatabaseException_Exception, UpdateException_Exception {
        final List<RecipeExecutionGraphNode> nextNodes = jobContext.getJob().getNextNodes(jobContext.getAssociatedNodes());
        if (jobContext.getJob().hasFinished()) {
            jobPool.removeJob(jobContext.getJob());
            LOG.info("Production Job finished: " + jobContext.getJob().getUUID());

            jobContext.getJob().getStoreManager().onProductionFinish(jobContext.getJob().getOrderEntry().getId());
            if (!jobPool.hasJobs(jobContext.getJob().getOrder(), jobContext.getJob().getOrderEntry())) {
                LOG.info("Production order entry finished: " + jobContext.getJob().getOrderEntry());
                jobContext.getJob().getStoreManager().onProductionOrderEntryFinish(jobContext.getJob().getOrderEntry().getId());
            }
            if (!jobPool.hasJobs(jobContext.getJob().getOrder())) {
                LOG.info("Production Order finished: " + jobContext.getJob().getOrderEntry());
                jobContext.getJob().getOrder().setDeliveryDate(new Date());
                persistenceContext.updateEntity(jobContext.getJob().getOrder());
                jobContext.getJob().getStoreManager().onProductionOrderFinish(jobContext.getJob().getOrder().getId());
            }
            return;
        }
        submitPlantOperatins(jobContext.getJob(), nextNodes);
    }
}
