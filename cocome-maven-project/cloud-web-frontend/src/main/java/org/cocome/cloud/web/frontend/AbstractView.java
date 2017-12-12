package org.cocome.cloud.web.frontend;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.ThrowingProcedure;
import org.cocome.cloud.web.data.AbstractDAO;
import org.cocome.cloud.web.data.ViewData;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;
import org.cocome.tradingsystem.inventory.application.IIdentifiableTO;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Contains reusable logic for every kind of page-wide view
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractView<TTargetContent extends IIdentifiableTO> implements Serializable {
    private static final Logger LOG = Logger.getLogger(AbstractView.class);

    public String save(@NotNull ViewData<TTargetContent> viewData)
            throws NotInDatabaseException_Exception {
        if (viewData.isNewInstance()) {
            return this.create(viewData);
        }
        return this.update(viewData);
    }

    public String create(@NotNull ViewData<TTargetContent> viewData) throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> getDAO().create(viewData),
                Messages.get("message.create.success", getObjectName()),
                Messages.get("message.create.failed", getObjectName()),
                getNextNavigationElement());
    }

    public String update(@NotNull ViewData<TTargetContent> viewData)
            throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> getDAO().update(viewData),
                Messages.get("message.update.success", getObjectName()),
                Messages.get("message.update.failed", getObjectName()),
                getNextNavigationElement());
    }

    public String delete(@NotNull ViewData<TTargetContent> viewData)
            throws NotInDatabaseException_Exception {
        return processFacesAction(
                () -> getDAO().delete(viewData),
                Messages.get("message.delete.success", getObjectName()),
                Messages.get("message.delete.failed", getObjectName()),
                getNextNavigationElement());
    }

    protected String processFacesAction(final ThrowingProcedure<Exception> proc,
                                        final String onSuccessMessage,
                                        final String onFailureMessage,
                                        final NavigationElements nextNavigationElement) {
        try {
            proc.run();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            onSuccessMessage, null));
            if (nextNavigationElement != null) {
                return nextNavigationElement.getNavigationOutcome();
            }
        } catch (final Exception e) {
            LOG.error(e);
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            onFailureMessage, null));
        }
        return null;
    }

    protected abstract AbstractDAO<?, TTargetContent> getDAO();

    protected abstract NavigationElements getNextNavigationElement();

    protected abstract String getObjectName();
}
