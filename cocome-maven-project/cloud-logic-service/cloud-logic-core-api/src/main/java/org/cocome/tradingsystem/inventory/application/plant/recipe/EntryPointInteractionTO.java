package org.cocome.tradingsystem.inventory.application.plant.recipe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rudolf Biczok
 */
@XmlType(
        name = "EntryPointInteractionTO",
        namespace = "http://recipe.plant.application.inventory.tradingsystem.cocome.org")
@XmlRootElement(name = "EntryPointInteractionTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntryPointInteractionTO extends InteractionEntityTO<EntryPointTO, EntryPointTO> {
    private static final long serialVersionUID = 1L;
}
