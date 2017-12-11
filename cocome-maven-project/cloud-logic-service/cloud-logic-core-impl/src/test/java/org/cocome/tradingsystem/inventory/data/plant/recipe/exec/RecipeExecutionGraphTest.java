package org.cocome.tradingsystem.inventory.data.plant.recipe.exec;

import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.plant.parameter.BooleanParameter;
import org.cocome.tradingsystem.inventory.data.plant.parameter.NominalParameter;
import org.cocome.tradingsystem.inventory.data.plant.recipe.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Test class for {@link RecipeExecutionGraph}
 *
 * @author Rudolf Biczok
 */
public class RecipeExecutionGraphTest {

    private IRecipe recipe;

    private static long nextId;

    @Before
    public void init() {

        /* Plant Operations */

        final EntryPoint op1out1 = new EntryPoint();
        op1out1.setName("ISO 12345 Cargo");
        op1out1.setId(getNextID());

        final BooleanParameter opr1param1 = new BooleanParameter();
        opr1param1.setCategory("Ingredient");
        opr1param1.setName("Organic");
        opr1param1.setId(getNextID());

        final PlantOperation operation1 = new PlantOperation();
        operation1.setName("Produce Yogurt");
        operation1.setParameters(Collections.singletonList(opr1param1));
        operation1.setInputEntryPoint(new ArrayList<>());
        operation1.setOutputEntryPoint(Collections.singletonList(op1out1));
        operation1.setId(getNextID());

        final EntryPoint op2out1 = new EntryPoint();
        op2out1.setName("ISO 33333 Bottle");
        op2out1.setId(getNextID());

        final NominalParameter opr2param = new NominalParameter();
        opr2param.setCategory("Packaging");
        opr2param.setOptions(new HashSet<>(Arrays.asList("Glass", "Plastic")));
        opr2param.setName("Bottle");
        opr2param.setId(getNextID());

        final PlantOperation operation2 = new PlantOperation();
        operation2.setName("Fill Yogurt");
        operation2.setParameters(Collections.singletonList(opr2param));
        operation2.setInputEntryPoint(new ArrayList<>());
        operation2.setOutputEntryPoint(Collections.singletonList(op2out1));
        operation2.setId(getNextID());

        final EntryPoint op3in1 = new EntryPoint();
        op3in1.setName("ISO 12345 Cargo");
        op3in1.setId(getNextID());

        final EntryPoint op3in2 = new EntryPoint();
        op3in2.setName("ISO 33333 Bottle");
        op3in2.setId(getNextID());

        final EntryPoint op3out1 = new EntryPoint();
        op3out1.setName("ISO 321 Package");
        op3out1.setId(getNextID());

        final PlantOperation operation3 = new PlantOperation();
        operation3.setName("Package Yogurt");
        operation3.setParameters(new ArrayList<>());
        operation3.setInputEntryPoint(Arrays.asList(op3in1, op3in2));
        operation3.setOutputEntryPoint(Collections.singletonList(op3out1));
        operation3.setId(getNextID());

        /* Custom product */

        final CustomProduct customProduct = new CustomProduct();
        customProduct.setBarcode(new Date().getTime());
        customProduct.setName("Yogurt");
        customProduct.setPurchasePrice(10);
        customProduct.setId(getNextID());

        /* Recipe creation */

        final EntryPoint recipeOut1 = new EntryPoint();
        recipeOut1.setName("ISO 321 Package");
        recipeOut1.setId(getNextID());

        this.recipe = new Recipe();
        recipe.setName("Yogurt Recipe");
        recipe.setCustomProduct(customProduct);
        recipe.setInputEntryPoint(new ArrayList<>());
        recipe.setOutputEntryPoint(Collections.singletonList(recipeOut1));
        recipe.setId(getNextID());

        final BooleanParameter cparam1 = new BooleanParameter();
        cparam1.setCategory("Ingredients");
        cparam1.setName("Organic");
        cparam1.setOperation(recipe);
        cparam1.setId(getNextID());

        final NominalParameter cparam2 = new NominalParameter();
        cparam2.setCategory("Packaging");
        cparam2.setName("Bottle");
        cparam2.setOptions(new HashSet<>(Arrays.asList("Glass", "Plastic")));
        cparam2.setOperation(recipe);
        cparam2.setId(getNextID());

        final ParameterInteraction interaction1 = new ParameterInteraction();
        interaction1.setFrom(cparam1);
        interaction1.setTo(opr1param1);
        interaction1.setId(getNextID());

        final ParameterInteraction interaction2 = new ParameterInteraction();
        interaction2.setFrom(cparam2);
        interaction2.setTo(opr2param);
        interaction2.setId(getNextID());

        final EntryPointInteraction epInteraction1 = new EntryPointInteraction();
        epInteraction1.setFrom(op1out1);
        epInteraction1.setTo(op3in1);
        epInteraction1.setId(getNextID());

        final EntryPointInteraction epInteraction2 = new EntryPointInteraction();
        epInteraction2.setFrom(op2out1);
        epInteraction2.setTo(op3in2);
        epInteraction2.setId(getNextID());

        final EntryPointInteraction epInteraction3 = new EntryPointInteraction();
        epInteraction3.setFrom(op3out1);
        epInteraction3.setTo(recipeOut1);
        epInteraction3.setId(getNextID());

        final RecipeNode node1 = new RecipeNode();
        node1.setOperation(operation1);
        node1.setRecipe(recipe);
        node1.setId(getNextID());

        final RecipeNode node2 = new RecipeNode();
        node2.setOperation(operation2);
        node2.setRecipe(recipe);
        node2.setId(getNextID());

        final RecipeNode node3 = new RecipeNode();
        node3.setOperation(operation3);
        node3.setRecipe(recipe);
        node3.setId(getNextID());

        recipe.setEntryPointInteractions(Arrays.asList(epInteraction1, epInteraction2, epInteraction3));
        recipe.setParameterInteractions(Arrays.asList(interaction1, interaction2));
        recipe.setNodes(Arrays.asList(node1, node2, node3));
    }

    @Test
    public void testCreateSimpleGraph() throws Exception {
        final RecipeExecutionGraph graph = new RecipeExecutionGraph(this.recipe);
        Assert.assertNotNull(graph.getStartNodes());
        Assert.assertEquals(graph.getStartNodes().size(), 2);
    }


    public long getNextID() {
        nextId++;
        return nextId;
    }
}