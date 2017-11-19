package org.cocome.tradingsystem.inventory.parser.ast;

import java.util.List;

/**
 * Represents the parsed information (abstract syntax tree) of a plant operation recipe
 *
 * @author Rudolf Biczok
 */
public class PlantOperationInfo {
    private List<ExpressionInfo> expressions;

    /**
     * Canonical constructor
     *
     * @param expressions the list of expressions this recipe consists of
     */
    public PlantOperationInfo(final List<ExpressionInfo> expressions) {
        this.expressions = expressions;
    }

    public PlantOperationInfo() {
    }

    public List<ExpressionInfo> getExpressions() {
        return expressions;
    }

}
