package org.cocome.tradingsystem.inventory.application.plant.productionunit;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.xml.bind.annotation.*;

/**
 * Represents an atomic operation on a production unit
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ProductionUnitOperationTO",
        namespace = "http://productionunit.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ProductionUnitOperationTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductionUnitOperationTO implements IIdentifiableTO {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "enterprise", required = true)
    private long id;
    @XmlElement(name = "enterprise", required = true)
    private String operationId;
    @XmlElement(name = "enterprise", required = true)
    private ProductionUnitClassTO productionUnitClass;

    /**
     * Gets identifier value
     *
     * @return The identifier value.
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * Sets identifier.
     *
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The operation id unique to the production plant
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * @param operationId The operation id unique to the production plant
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return the associated {@link ProductionUnitClassTO}
     */
    public ProductionUnitClassTO getProductionUnitClass() {
        return productionUnitClass;
    }

    /**
     * @param productionUnitClass the associated production unit class
     */
    public void setProductionUnitClass(ProductionUnitClassTO productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }
}
