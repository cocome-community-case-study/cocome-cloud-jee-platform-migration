package org.cocome.tradingsystem.inventory.application.plant.recipe;

import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.enterprise.parameter.CustomProductParameterTO;
import org.cocome.tradingsystem.inventory.application.plant.parameter.PlantOperationParameterTO;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Used to connect parameters from {@link CustomProductTO} and {@link PlantOperationTO}.
 * Other subsystems are supposed to copy the customer's parameter values to the plant
 * operation based on this mapping
 *
 * @author Rudolf Biczok
 */
@XmlType(
        name = "ParameterInteractionTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "ParameterInteractionTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterInteractionTO extends InteractionEntityTO<
        CustomProductParameterTO,
        PlantOperationParameterTO> {
    private static final long serialVersionUID = 1L;
}
