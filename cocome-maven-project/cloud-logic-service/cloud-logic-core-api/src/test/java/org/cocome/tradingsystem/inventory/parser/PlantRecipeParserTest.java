package org.cocome.tradingsystem.inventory.parser;

import org.cocome.tradingsystem.inventory.parser.ast.ConditionalExpressionInfo;
import org.cocome.tradingsystem.inventory.parser.ast.PUOperationInfo;
import org.cocome.tradingsystem.inventory.parser.ast.PlantOperationInfo;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Test class for {@link PlantRecipeParser}
 *
 * @author Rudolf Biczok
 */
public class PlantRecipeParserTest {

    private PlantRecipeParser parser = new PlantRecipeParser();

    @Test
    public void testToString() throws Exception {

        final String expectedJson = readResourceFile(
                PlantRecipeParserTest.class,
                "plant_recipe_parser_out.json");

        final String text = parser.toString(new PlantOperationInfo(Arrays.asList(
                new PUOperationInfo("PUC1", "op1"),
                new PUOperationInfo("PUC1", "op1"),
                new ConditionalExpressionInfo("Param1", "true",
                        Arrays.asList(
                                new PUOperationInfo("PUC2", "op2"),
                                new PUOperationInfo("PUC2", "op2"),
                                new PUOperationInfo("PUC2", "op2")
                        ),
                        Arrays.asList(
                                new PUOperationInfo("PUC1", "op2"),
                                new PUOperationInfo("PUC1", "op3")
                        )),
                new PUOperationInfo("PUC1", "op2")
        )));

        Assert.assertEquals(text, expectedJson);

        Assert.assertEquals(parser.toString(this.parser.parse(expectedJson)), expectedJson);
    }

    private String readResourceFile(final Class<?> clazz, final String resourceName) throws IOException {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(
                getResourceFromPackage(clazz, resourceName)))) {
            final StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                builder.append("\n");
                line = reader.readLine();
            }
            if (builder.length() > 0) {
                //simulate EOF by removing last element
                builder.deleteCharAt(builder.length() - 1);
            }
            return builder.toString();
        }
    }

    private InputStream getResourceFromPackage(final Class<?> clazz, final String resourceName) {
        return clazz.getResourceAsStream(String.format("/%s/%s",
                clazz.getPackage().getName().replace('.', '/'),
                resourceName));
    }

}