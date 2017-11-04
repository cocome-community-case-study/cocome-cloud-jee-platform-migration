package org.cocome.tradingsystem.inventory.application.plant.pu;

import org.cocome.tradingsystem.inventory.data.plant.expression.EvaluationContext;
import org.cocome.tradingsystem.inventory.data.plant.expression.IExpression;
import org.cocome.tradingsystem.inventory.data.plant.expression.IPUInstruction;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationOrderEntry;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates all {@link PUWorkingPackage} to fulfill one particular plant operation order.
 *
 * @author Rudolf Biczok
 */
public class PlantJob {

    private final IPlantOperationOrderEntry orderEntry;
    private final List<PUWorkingPackage> workingPackages;

    public PlantJob(final IPlantOperationOrderEntry orderEntry) throws NotInDatabaseException {
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

    public IPlantOperationOrderEntry getOrderEntry() {
        return orderEntry;
    }

    public List<PUWorkingPackage> getWorkingPackages() {
        return workingPackages;
    }
}
