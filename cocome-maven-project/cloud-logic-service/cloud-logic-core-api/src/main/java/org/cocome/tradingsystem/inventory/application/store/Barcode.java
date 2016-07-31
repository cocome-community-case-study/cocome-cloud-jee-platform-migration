package org.cocome.tradingsystem.inventory.application.store;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@XmlType(name="Barcode", namespace="http://store.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name="Barcode")
@XmlAccessorType(XmlAccessType.FIELD)
public class Barcode implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2577328715744776645L;
    
    @XmlElement(name="id", required=true)
    private long id;

    @XmlElement(name="code", required=true)
    private long code;
    
    public Barcode() {
    
    }
    
    public Barcode(long code) {
        this.code = code;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }
    
    /**
     * Sets identifier.
     * 
     * @param id
     *            Identifier value.
     */
    public void setId(long id) {
        this.id = id;
    }

    public static Barcode parseInput(String input) {
        return new Barcode(Long.parseLong(input));
    }

}
