package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.tradingsystem.inventory.data.plant.expression.EvaluationContext;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IPUInstruction;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrder;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

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
                    final IPlantOperationOrder order,
                    final IPlantOperationOrderEntry orderEntry) throws NotInDatabaseException {
        this.uuid = UUID.randomUUID();
        this.enterpriseManager = enterpriseManager;
        this.order = order;
        this.orderEntry = orderEntry;

        final List<IPUInstruction> instructionList = IExpression.evaluateList(
                orderEntry.getPlantOperation().getExpressions(),
                new EvaluationContext(orderEntry.getParameterValues()));

        this.workingPackages = new LinkedList<>();
        List<String> currentList = new LinkedList<>();
        IProductionUnitClass currentPUC = null;
        for (final IPUInstruction inst : instructionList) {
            if (currentPUC != null && currentPUC.getId() != inst.getPUC().getId()) {
                this.workingPackages.add(new PUWorkingPackage(currentPUC, currentList));
                currentList = new LinkedList<>();
            }
            currentPUC = inst.getPUC();
            currentList.add(inst.getOperationId());
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
