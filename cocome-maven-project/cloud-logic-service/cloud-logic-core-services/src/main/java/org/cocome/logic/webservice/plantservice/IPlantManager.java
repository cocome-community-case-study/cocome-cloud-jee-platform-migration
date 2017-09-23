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

    /**
     * @param enterpriseId the unique identifier of a TradingEnterprise entity
     * @return A collection of {@link ProductionUnitOperationTO} objects belonging to the given enterprise.
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
    @WebMethod
    Collection<ProductionUnitOperationTO> queryProductionUnitOperationsByEnterpriseID(
                    @XmlElement(required = true)
                    @WebParam(name = "enterpriseID")
                    long enterpriseId) throws NotInDatabaseException;

    /**
     * @param productionUnitOperationId the unique identifier of a {@link ProductionUnitOperationTO} entity
     * @return A {@link ProductionUnitOperationTO} object that belongs to the given identifier
     * @throws NotInDatabaseException if a trading enterprise with the given id could not be found
     */
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
