package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.webservice.StreamUtil;
import org.cocome.tradingsystem.inventory.application.plant.expression.EvaluationContext;
import org.cocome.tradingsystem.inventory.application.plant.expression.PUInstruction;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Encapsulates all {@link PUWorkingPackage} to fulfill one particular plant operation order.
 *
 * @author Rudolf Biczok
 */
public class PlantJob {

    private final UUID uuid;
    private final IEnterpriseManager enterpriseManager;
    private final IPlantOperationOrder order;
    private final IPlantOperationOrderEntry orderEntry;
    private final Queue<PUWorkingPackage> workingPackages;

    public PlantJob(final IEnterpriseManager enterpriseManager,
                    final Collection<IProductionUnitClass> pucList,
                    final IPlantOperationOrder order,
                    final IPlantOperationOrderEntry orderEntry) throws NotInDatabaseException {
        this.uuid = UUID.randomUUID();
        this.enterpriseManager = enterpriseManager;
        this.order = order;
        this.orderEntry = orderEntry;

        Map<String, IProductionUnitClass> pucMapping = StreamUtil.ofNullable(pucList)
                .collect(Collectors.toMap(IProductionUnitClass::getName, Function.identity()));
        final List<PUInstruction> instructionList =
                orderEntry.getPlantOperation().getMarkup()
                        .evaluate(new EvaluationContext(orderEntry.getParameterValues()));

        this.workingPackages = new LinkedList<>();
        List<String> currentList = new LinkedList<>();
        IProductionUnitClass currentPUC = null;
        for (final PUInstruction inst : instructionList) {
            if (!pucMapping.containsKey(inst.getPUCName())) {
                throw new IllegalArgumentException(String.format("Unknown PUC in instruction list: %s (known: %s) "
                        , inst.getPUCName(), pucMapping.keySet()));
            }
            if (currentPUC != null && currentPUC.getId() != pucMapping.get(inst.getPUCName()).getId()) {
                this.workingPackages.add(new PUWorkingPackage(currentPUC, currentList));
                currentList = new LinkedList<>();
            }
            currentPUC = pucMapping.get(inst.getPUCName());
            currentList.add(inst.getOperationId());
        }
        if(!currentList.isEmpty()) {
            this.workingPackages.add(new PUWorkingPackage(currentPUC, currentList));
        }
    }

    /**
     * @return the enterprise manager client
     */
    public IEnterpriseManager getEnterpriseManager() {
        return enterpriseManager;
    }

    /**
     * @return the associated order
     */
    public IPlantOperationOrder getOrder() {
        return order;
    }

    /**
     * @return the unique id of this job
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @return returns the associated order entry
     */
    public IPlantOperationOrderEntry getOrderEntry() {
        return orderEntry;
    }

    /**
     * @return the working packages
     */
    public Queue<PUWorkingPackage> getWorkingPackages() {
        return workingPackages;
    }
}
