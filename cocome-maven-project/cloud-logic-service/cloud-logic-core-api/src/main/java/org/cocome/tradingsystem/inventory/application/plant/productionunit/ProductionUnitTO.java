package org.cocome.tradingsystem.inventory.application.plant.productionunit;

import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;

import javax.xml.bind.annotation.*;

/**
 * This class represents a Product in the database
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ProductionUnitTO",
        namespace = "http://productionunit.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ProductionUnitTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductionUnitTO implements IIdentifiableTO {

    private static final long serialVersionUID = -2577328715744776645L;

    @XmlElement(name = "id", required = true)
    private long id;
    @XmlElement(name = "location", required = true)
    private String location;
    @XmlElement(name = "interfaceUrl", required = true)
    private String interfaceUrl;
    @XmlElement(name = "plant", required = true)
    private PlantTO plant;
    @XmlElement(name = "productionUnitClass", required = true)
    private ProductionUnitClassTO productionUnitClass;

    /**
     * Gets identifier value
     *
     * @return The id.
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
     * @return Production unit location.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @param location Production unit location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * @return The URL location used to communicate with the device
     */
    public String getInterfaceUrl() {
        return this.interfaceUrl;
    }

    /**
     * @param interfaceUrl The URL location used to communicate with the device
     */
    public void setInterfaceUrl(final String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    /**
     * @return the production unit class
     */
    public ProductionUnitClassTO getProductionUnitClass() {
        return productionUnitClass;
    }

    /**
     * @param productionUnitClass the production unit class
     */
    public void setProductionUnitClass(ProductionUnitClassTO productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }

    /**
     * @return the plant that owns this production unit
     */
    public PlantTO getPlant() {
        return plant;
    }

    /**
     * @param plant the plant that owns this production unit
     */
    public void setPlant(PlantTO plant) {
        this.plant = plant;
    }
}
