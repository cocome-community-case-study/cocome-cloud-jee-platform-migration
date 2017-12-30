package org.cocome.tradingsystem.inventory.parser.plant;

import org.cocome.tradingsystem.inventory.application.plant.expression.ConditionalExpressionInfo;
import org.cocome.tradingsystem.inventory.application.plant.expression.PUOperationInfo;
import org.cocome.tradingsystem.inventory.application.plant.expression.MarkupInfo;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Test class for {@link MarkupParser}
 *
 * @author Rudolf Biczok
 */
public class MarkupParserTest {

    private MarkupParser parser = new MarkupParser();

    @Test
    public void testToString() throws Exception {

        final String expectedJson = readResourceFile(
                MarkupParserTest.class,
                "plant_markup_parser_out.json");

        final String text = parser.toString(new MarkupInfo(Arrays.asList(
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

        Assert.assertEquals(expectedJson, text);

        Assert.assertEquals(expectedJson, parser.toString(this.parser.parse(expectedJson)));
    }

    private String readResourceFile(final Class<?> clazz, final String resourceName) throws IOException {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(
                getResourceFromPackage(clazz, resourceName)))) {
            final StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
                line = reader.readLine();
            }
            if (builder.length() > 0) {
                //simulate EOF by removing last element
                for (int i = 0; i < System.lineSeparator().length(); i++) {
                    builder.deleteCharAt(builder.length() - 1);
                }
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