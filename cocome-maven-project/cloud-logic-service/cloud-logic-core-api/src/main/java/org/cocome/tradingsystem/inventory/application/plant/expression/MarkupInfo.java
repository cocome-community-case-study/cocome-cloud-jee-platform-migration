package org.cocome.tradingsystem.inventory.application.plant.expression;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Represents the parsed information (abstract syntax tree) of a plant operation recipe
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "MarkupInfo",
        namespace = "http://expression.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "MarkupInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class MarkupInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "expressions", required = true)
    private List<ExpressionInfo> expressions;

    /**
     * Canonical constructor
     *
     * @param expressions the list of expressions this recipe consists of
     */
    public MarkupInfo(final List<ExpressionInfo> expressions) {
        this.expressions = expressions;
    }

    public MarkupInfo() {
    }

    public List<ExpressionInfo> getExpressions() {
        return expressions;
    }

    public List<PUInstruction> evaluate(EvaluationContext evaluationContext) throws NotInDatabaseException {
        return ExpressionInfo.evaluateList(this.expressions, evaluationContext);
    }
}
