package org.cocome.tradingsystem.inventory.application.plant.parameter;

import org.cocome.tradingsystem.inventory.application.enterprise.parameter.IBooleanParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.PlantOperationTO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Abstract class of {@link IBooleanParameterTO} for {@link PlantOperationTO}
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "BooleanPlantOperationParameterTO",
        namespace = "http://parameter.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "BooleanPlantOperationParameterTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanPlantOperationParameterTO extends PlantOperationParameterTO implements IBooleanParameterTO {
    private static final long serialVersionUID = -2577328715744776645L;
}
