package de.kit.ipd.java.utils.parsing.csv;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rudolf Biczok
 */
public class CSVParserTest {

    @Test
    public void parseHeaderOnlyTables() throws Exception {
        CSVParser parser = new CSVParser();
        parser.parse("EnterpriseId;EnterpriseName;Test");
        Assert.assertEquals(0, parser.getModel().getRows().size());
    }
}