/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package org.cocome.logic.webservice.plantservice;

import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitOperationTO;
import org.cocome.tradingsystem.inventory.data.persistence.UpdateException;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.ejb.CreateException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.io.IOException;
import java.util.Collection;

/**
 * Interface for all plant-related web services.
 *
 * @author Rudolf Biczok
 */
@WebService(targetNamespace = "http://plant.webservice.logic.cocome.org/")
public interface IPlantManager {

    /* CRUD for {@link ProductionUnitClassTO} **************/

    @WebMethod
    Collection<ProductionUnitClassTO> queryProductionUnitClassesByPlantID(
            @XmlElement(required = true)
            @WebParam(name = "plantID")
                    long plantId)
            throws NotInDatabaseException;

    @WebMethod
    ProductionUnitClassTO queryProductionUnitClassByID(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitClassID")
                    long productionUnitClassId)
            throws NotInDatabaseException;

    @WebMethod
    void createProductionUnitClass(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitClassTO")
                    ProductionUnitClassTO productionUnitClassTO)
            throws CreateException;

    @WebMethod
    void updateProductionUnitClass(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitClassTO")
                    ProductionUnitClassTO productionUnitClassTO)
            throws UpdateException, NotInDatabaseException;

    @WebMethod
    void deleteProductionUnitClass(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitClassTO")
                    ProductionUnitClassTO productionUnitClassTO)
            throws UpdateException, NotInDatabaseException;

    /* CRUD for {@link ProductionUnitOperationTO} **************/

    @WebMethod
    Collection<ProductionUnitOperationTO> queryProductionUnitOperationsByProductionUnitClassID(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitClassID")
                    long productionUnitClassId)
            throws NotInDatabaseException;

    @WebMethod
    ProductionUnitOperationTO queryProductionUnitOperationByID(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitOperationID")
                    long productionUnitOperationId)
            throws NotInDatabaseException;

    @WebMethod
    void createProductionUnitOperation(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitOperationTO")
                    ProductionUnitOperationTO productionUnitOperationTO)
            throws CreateException;

    @WebMethod
    void updateProductionUnitOperation(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitOperationTO")
                    ProductionUnitOperationTO productionUnitOperationTO)
            throws NotInDatabaseException, UpdateException;

    @WebMethod
    void deleteProductionUnitOperation(
            @XmlElement(required = true)
            @WebParam(name = "productionUnitOperationTO")
                    ProductionUnitOperationTO productionUnitOperationTO)
            throws NotInDatabaseException, UpdateException;
}
