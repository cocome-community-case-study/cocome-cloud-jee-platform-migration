package org.cocome.cloud.web.data.enterprisedata;

import org.cocome.cloud.logic.stub.IEnterpriseManager;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.EnterpriseQuery;
import org.cocome.cloud.web.data.AbstractQuery;
import org.cocome.cloud.web.frontend.enterprise.EnterpriseInformation;
import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.inject.Inject;

/**
 * Abstract class for query objects that rely on a enterprise service
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractEnterpriseQuery<TTargetContent extends IIdentifiableTO>
        extends AbstractQuery<IEnterpriseManager, TTargetContent> {

    @Inject
    private EnterpriseQuery enterpriseQuery;

    @Inject
    private EnterpriseInformation enterpriseInformation;

    @Override
    public IEnterpriseManager createServiceClient() throws NotInDatabaseException_Exception {
        return this.enterpriseQuery.lookupEnterpriseManager(enterpriseInformation.getActiveEnterprise().getId());
    }
}
