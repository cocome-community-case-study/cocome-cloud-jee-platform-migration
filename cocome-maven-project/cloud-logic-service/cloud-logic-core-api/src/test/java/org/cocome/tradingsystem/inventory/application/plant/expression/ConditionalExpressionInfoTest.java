package org.cocome.tradingsystem.inventory.application.plant.expression;

import org.cocome.tradingsystem.inventory.data.enterprise.parameter.IBooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.IBooleanPlantOperationParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperationParameterValue;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConditionalExpressionInfoTest {

    @Test
    public void testEvaluate() throws NotInDatabaseException {
        final IBooleanPlantOperationParameter param = Mockito.mock(IBooleanPlantOperationParameter.class);
        Mockito.when(param.getName()).thenReturn("Some Parameter");

        final PUOperationInfo op1 = new PUOperationInfo("PUC1", "_OP1");
        final PUOperationInfo op2 = new PUOperationInfo("PUC1", "_OP2");
        final PUOperationInfo op3 = new PUOperationInfo("PUC2", "_OP3");
        final PUOperationInfo op4 = new PUOperationInfo("PUC2", "_OP4");

        final ConditionalExpressionInfo conditionalExpression = new ConditionalExpressionInfo();
        conditionalExpression.setParameterName(param.getName());
        conditionalExpression.setTestValue(IBooleanParameter.TRUE_VALUE);
        conditionalExpression.setOnTrueExpressions(Arrays.asList(op1, op2));
        conditionalExpression.setOnFalseExpressions(Arrays.asList(op3, op4));

        final IPlantOperationParameterValue paramValue = Mockito.mock(IPlantOperationParameterValue.class);
        Mockito.when(paramValue.getValue()).thenReturn(IBooleanParameter.FALSE_VALUE);
        Mockito.when(paramValue.getParameter()).thenReturn(param);

        final EvaluationContext context = new EvaluationContext(Collections.singletonList(paramValue));

        final List<PUInstruction> inst = conditionalExpression.evaluate(context);

        Assert.assertNotNull(inst);
        Assert.assertEquals(inst.size(), 2);
        Assert.assertEquals(inst.get(0).getPUCName(), "PUC2");
        Assert.assertEquals(inst.get(0).getOperationId(), op3.getOperationId());
        Assert.assertEquals(inst.get(1).getPUCName(), "PUC2");
        Assert.assertEquals(inst.get(1).getOperationId(), op4.getOperationId());
    }

}