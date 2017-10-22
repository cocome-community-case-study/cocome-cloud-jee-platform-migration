package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationParameterValue;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperationParameterValue;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test for {@link ConditionalExpression}
 * @author Rudolf Biczok
 */
public class ConditionalExpressionTest {

    @Test
    public void testEvaluate() throws NotInDatabaseException {
        final BooleanPlantOperationParameter param = new BooleanPlantOperationParameter();
        param.setName("Some Parameter");

        final ProductionUnitClass puc1 = new ProductionUnitClass();
        puc1.setName("PUC1");

        final ProductionUnitOperation op1 = new ProductionUnitOperation();
        op1.setName("NameOfOp1");
        op1.setOperationId("_OP1");
        op1.setProductionUnitClass(puc1);

        final ProductionUnitOperation op2 = new ProductionUnitOperation();
        op2.setName("NameOfOp2");
        op2.setOperationId("_OP2");
        op2.setProductionUnitClass(puc1);

        final ProductionUnitClass puc2 = new ProductionUnitClass();
        puc2.setName("PUC2");

        final ProductionUnitOperation op3 = new ProductionUnitOperation();
        op3.setName("NameOfOp3");
        op3.setOperationId("_OP3");
        op3.setProductionUnitClass(puc2);

        final ProductionUnitOperation op4 = new ProductionUnitOperation();
        op4.setName("NameOfOp4");
        op4.setOperationId("_OP4");
        op4.setProductionUnitClass(puc2);

        final ConditionalExpression conditionalExpression = new ConditionalExpression();
        conditionalExpression.setParameter(param);
        conditionalExpression.setParameterValue(IBooleanParameter.TRUE_VALUE);
        conditionalExpression.setOnTrueExpressions(Arrays.asList(op1,op2));
        conditionalExpression.setOnFalseExpressions(Arrays.asList(op3,op4));

        final IPlantOperationParameterValue paramValue = new PlantOperationParameterValue();
        paramValue.setValue(IBooleanParameter.FALSE_VALUE);
        paramValue.setParameter(param);

        final EvaluationContext context = new EvaluationContext(Collections.singletonList(paramValue));

        final List<IPUInstruction> inst = conditionalExpression.evaluate(context);

        Assert.assertNotNull(inst);
        Assert.assertEquals(inst.size(), 2);
        Assert.assertEquals(inst.get(0).getPUC().getName(), puc2.getName());
        Assert.assertEquals(inst.get(0).getOperationId(), op3.getOperationId());
        Assert.assertEquals(inst.get(1).getPUC().getName(), puc2.getName());
        Assert.assertEquals(inst.get(1).getOperationId(), op4.getOperationId());
    }
}