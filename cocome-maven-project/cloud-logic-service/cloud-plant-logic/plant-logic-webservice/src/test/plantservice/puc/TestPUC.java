package plantservice.puc;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Test class, just for alleviating the work with plant-specific data structures in test cases
 *
 * @author Rudolf Biczok
 */
public class TestPUC {

    private ProductionUnitClassTO puc;

    private Map<TestPUCOperation, ProductionUnitOperationTO> operators;

    public TestPUC(final String pucName,
                   final TestPUCOperation[] operations,
                   final PlantTO plant,
                   final IPlantManager pm) throws CreateException_Exception {

        puc = new ProductionUnitClassTO();
        puc.setName(pucName);
        puc.setPlant(plant);
        puc.setId(pm.createProductionUnitClass(puc));

        operators = Arrays.stream(operations).collect(Collectors.toMap(Function.identity(),
                e -> {
                    final ProductionUnitOperationTO operation = new ProductionUnitOperationTO();
                    operation.setOperationId(e.getOperationId());
                    operation.setProductionUnitClass(puc);
                    try {
                        operation.setId(pm.createProductionUnitOperation(
                                operation));
                    } catch (CreateException_Exception e1) {
                        throw new IllegalArgumentException("Cannot create production unit operation: "
                                + e1.getMessage(),
                                e1);
                    }
                    return operation;
                }));
    }

    public ProductionUnitOperationTO getOperation(final TestPUCOperation op) {
        return this.operators.get(op);
    }

}
