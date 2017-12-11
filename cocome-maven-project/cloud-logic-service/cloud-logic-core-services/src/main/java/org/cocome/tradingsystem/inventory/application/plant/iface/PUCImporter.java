package org.cocome.tradingsystem.inventory.application.plant.iface;

import org.cocome.cloud.logic.stub.CreateException_Exception;
import org.cocome.cloud.logic.stub.IPlantManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.expression.PUOperationInfo;
import org.cocome.tradingsystem.inventory.application.plant.iface.ppu.PUCOperationMeta;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationOrderTO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Test class, just for alleviating the work with plant-specific data structures in test cases
 *
 * @author Rudolf Biczok
 */
public class PUCImporter {

    private ProductionUnitClassTO puc;

    private Map<String, ProductionUnitOperationTO> operators;

    public PUCImporter(final String pucName,
                       final PlantTO plant,
                       final IPlantManager pm) throws CreateException_Exception,
            NotInDatabaseException_Exception {

        final long pucId =
                pm.importProductionUnitClass("Test", "http://129.187.88.30:4567", plant);

        puc = pm.queryProductionUnitClassByID(pucId);

        final List<ProductionUnitOperationTO> oprs = pm.queryProductionUnitOperationsByProductionUnitClassID(pucId);

        operators = oprs.stream().collect(Collectors.toMap(
                ProductionUnitOperationTO::getOperationId,
                Function.identity()));
    }


    public PUCImporter(final String pucName,
                       final PUCOperationMeta[] operations,
                       final PlantTO plant,
                       final IPlantManager pm) throws CreateException_Exception {

        puc = new ProductionUnitClassTO();
        puc.setName(pucName);
        puc.setPlant(plant);
        puc.setId(pm.createProductionUnitClass(puc));

        operators = Arrays.stream(operations).collect(Collectors.toMap(PUCOperationMeta::getOperationId,
                e -> {
                    final ProductionUnitOperationTO operation = new ProductionUnitOperationTO();
                    operation.setName(e.getName());
                    operation.setOperationId(e.getOperationId());
                    operation.setExecutionDurationInMillis(e.getExecutionDurationInMillis());
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

    public ProductionUnitClassTO getProductionUnitClass() {
        return puc;
    }

    public PUOperationInfo getOperation(final PUCOperationMeta op) {
        final ProductionUnitOperationTO oprTO = this.operators.get(op.getOperationId());
        return new PUOperationInfo(this.puc.getName(), oprTO.getOperationId());
    }

}
