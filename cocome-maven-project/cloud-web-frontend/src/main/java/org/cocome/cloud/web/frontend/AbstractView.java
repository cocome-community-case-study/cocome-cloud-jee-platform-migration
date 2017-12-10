package org.cocome.cloud.web.frontend;

import org.apache.log4j.Logger;
import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.logic.webservice.ThrowingProcedure;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Contains reusable logic for every kind of page-wide view
 *
 * @author Rudolf Biczok
 */
public class AbstractView implements Serializable {
    private static final Logger LOG = Logger.getLogger(AbstractView.class);

    protected String createAction(final ThrowingProcedure<Exception> proc,
                                  final String objectNameKey,
                                  final NavigationElements nextNavigationElement)
            throws NotInDatabaseException_Exception {
        return processFacesAction(
                proc,
                Messages.get("message.create.success", Messages.get(objectNameKey)),
                Messages.get("message.create.failed", Messages.get(objectNameKey)),
                nextNavigationElement);
    }

    protected String updateAction(final ThrowingProcedure<Exception> proc,
                                  final String objectNameKey,
                                  final NavigationElements nextNavigationElement)
            throws NotInDatabaseException_Exception {
        return processFacesAction(
                proc,
                Messages.get("message.update.success", Messages.get(objectNameKey)),
                Messages.get("message.update.failed", Messages.get(objectNameKey)),
                nextNavigationElement);
    }

    protected String deleteAction(final ThrowingProcedure<Exception> proc,
                                  final String objectNameKey,
                                  final NavigationElements nextNavigationElement)
            throws NotInDatabaseException_Exception {
        return processFacesAction(proc,
                Messages.get("message.delete.success", Messages.get(objectNameKey)),
                Messages.get("message.delete.failed", Messages.get(objectNameKey)),
                nextNavigationElement);
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

}
