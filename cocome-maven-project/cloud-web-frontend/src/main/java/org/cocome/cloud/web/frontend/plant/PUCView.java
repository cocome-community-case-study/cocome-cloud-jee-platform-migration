package org.cocome.cloud.web.frontend.plant;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.data.plantdata.PUCWrapper;
import org.cocome.cloud.web.data.plantdata.ProductionUnitClassDAO;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Holds information about the currently active plant.
 *
 * @author Rudolf Biczok
 */
@Named
@ViewScoped
public class PUCView implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(PUCView.class);

    @Inject
    private ProductionUnitClassDAO productionUnitClassDAO;

    @Inject
    private PlantInformation plantInformation;

    private List<PUCWrapper> pucs;

    @PostConstruct
    public void queryPUCs() {
        LOG.info("Query PUCs");
        try {
            this.pucs = productionUnitClassDAO.queryAll(plantInformation.getActivePlant());
        } catch (NotInDatabaseException_Exception e) {
            LOG.error("Unable to load PUC list", e);
        }
    }

    public List<PUCWrapper> getPucs() {
        return pucs;
    }

    public void setPucs(final List<PUCWrapper> pucs) {
        this.pucs = pucs;
    }

}
